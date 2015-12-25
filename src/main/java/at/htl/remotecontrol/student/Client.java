package at.htl.remotecontrol.student;

import at.htl.remotecontrol.actions.RobotAction;
import at.htl.remotecontrol.actions.RobotActionQueue;
import at.htl.remotecontrol.entity.Directory;
import at.htl.remotecontrol.entity.FileStream;
import at.htl.remotecontrol.entity.Session;
import at.htl.remotecontrol.packets.LoginPacket;

import java.awt.*;
import java.io.*;
import java.net.Socket;

/**
 * @timeline Text
 * 26.10.2015: MET 002  Klasse von Student auf Client umbenannt
 * 31.10.2015: MET 075  Funktion "Angabe herunterladen" implementiert
 * 01.11.2015: MET 015  überwachter Ordner automatisch gezippt abgeben
 * 01.11.2015: MET 005  Bug festgestellt: Abgabe nur unmittelbar nach Login
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
        Socket socket = new Socket(loginPacket.getServerIP(), loginPacket.getPort());
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

    /**
     * gets the file from the teacher for the test and saves it
     */
    public void loadFiles() {
        FileStream.receive(in, loginPacket.getDirOfWatch() + "/angabe.zip");
        processor.start();
        reader.start();
    }

    /**
     * compromises the working-directory and sends it to the teacher
     *
     * @return the success of it
     */
    public boolean handIn() {
        //System.out.println("DELETED DIRECTORY");
        //Directory.delete(loginPacket.getDirOfWatch() + "/" + loginPacket.getUserName());//loginPacket.getDirOfWatch() + "/angabe.zip");
        if (processor.isInterrupted() && reader.isInterrupted()) {
            String zipFileName = "handInFile.zip";
            Directory.zip(loginPacket.getDirOfWatch(), zipFileName);
            FileStream.send(out, new File(String.format("%s/%s",
                    loginPacket.getDirOfWatch(), zipFileName)));
            return true;
        }
        return false;
    }

    /**
     * reads jobs from the stream and adds them as a new job
     */
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


    /**
     * takes all jobs from the stream and executes them.
     * Afterwords he sends the result from the job back with the stream.
     */
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

    /**
     * closes the stream
     */
    public void closeOut() {
        try {
            out.close();
        } catch (IOException e) {
            System.out.println("Error by closing of ObjectOutStream!");
        }
    }

    /**
     * start-point for the thread
     */
    public void start() {
        loadFiles();
    }

    /**
     * student logs out -> stop all streams from this student.
     */
    public void stop() {
        Session.getInstance().removeStudent(loginPacket.getUserName());
        processor.interrupt();
        reader.interrupt();
        boolean check = handIn();
        System.out.println("ERFOLGREICH ==> " + check);
        closeOut();
    }

}