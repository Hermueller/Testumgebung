package at.htl.remotecontrol.student;

import at.htl.remotecontrol.actions.RobotAction;
import at.htl.remotecontrol.actions.RobotActionQueue;
import at.htl.remotecontrol.entity.Directory;
import at.htl.remotecontrol.entity.FileStream;
import at.htl.remotecontrol.packets.LoginPacket;
import at.htl.remotecontrol.server.TeacherServer;

import java.awt.*;
import java.io.*;
import java.net.Socket;

/**
 * 26.10.2015:  Tobias      Klasse von Student auf Client umbenannt
 * 31.10.2015:  Tobias      Funktion "Angabe herunterladen" implementiert
 * 01.10.2015:  Tobias      überwachter Ordner automatisch gezippt abgeben
 */
public class Client {

    private final ObjectOutputStream out;
    private final ObjectInputStream in;
    private final Robot robot;
    private RobotActionQueue jobs;
    private final ProcessorThread processor;
    private final ReaderThread reader;
    private final LoginPacket loginPacket;

    public Client(LoginPacket loginPacket)
            throws IOException, AWTException {
        this.loginPacket = loginPacket;
        Socket socket = new Socket(loginPacket.getServerIP(), TeacherServer.PORT);
        Directory.create(loginPacket.getDirOfWatch());
        robot = new Robot();
        jobs = new RobotActionQueue();
        in = new ObjectInputStream(
                new BufferedInputStream(socket.getInputStream()));
        out = new ObjectOutputStream(socket.getOutputStream());
        out.writeObject(loginPacket);
        out.flush();
        processor = new ProcessorThread();
        reader = new ReaderThread();
    }


    public void loadFiles() {
        FileStream.receive(in, loginPacket.getDirOfWatch() + "/angabe.zip");
        processor.start();
        reader.start();
    }

    public boolean handIn() {
        System.out.println("DELETE DIRECTORY:");
        Directory.delete(loginPacket.getDirOfWatch() + "/angabe.zip");
        if (processor.isInterrupted() && reader.isInterrupted()) {
            String zipFileName = "handInFile.zip";
            Directory.zip(loginPacket.getDirOfWatch(), zipFileName);
            FileStream.send(out, new File(String.format("%s/%s",
                    loginPacket.getDirOfWatch(), zipFileName)));
            return true;
        }
        return false;
    }

    private class ReaderThread extends Thread {
        public void run() {
            try {
                RobotAction action;
                while ((action = (RobotAction) in.readObject()) != null) {
                    if (!action.equals(jobs.peekLast())) {
                        jobs.add(action);
                        System.out.println("jobs = " + jobs);
                    } else {
                        System.out.println("Discarding duplicate request");
                    }
                }
            } catch (EOFException eof) {
                System.out.println("Connection closed");
            } catch (Exception ex) {
                System.out.println("Connection closed abruptly: " + ex);
            }
        }
    }


    private class ProcessorThread extends Thread {
        public ProcessorThread() {
            super("ProcessorThread");
            setDaemon(true);
        }

        public void run() {
            try {
                while (!isInterrupted()) {
                    try {
                        RobotAction action = jobs.take();
                        Object result = action.execute(robot);
                        if (result != null) {
                            out.writeObject(result);
                            out.reset();
                            out.flush();
                        }
                    } catch (InterruptedException e) {
                        interrupt();
                        break;
                    }
                }
            } catch (IOException e) {
                System.out.println("Connection closed (" + e + ')');
            }
        }
    }

    public void closeOut() {
        try {
            out.close();
        } catch (IOException e) {
            System.out.println("Error by closing of ObjectOutStream!");
        }
    }

    public void start() {
        loadFiles();
    }

    public void stop() {
        processor.interrupt();
        reader.interrupt();
        handIn();
        closeOut();
    }

}