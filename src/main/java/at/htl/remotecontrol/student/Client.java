package at.htl.remotecontrol.student;

import at.htl.remotecontrol.actions.RobotAction;
import at.htl.remotecontrol.actions.RobotActionQueue;
import at.htl.remotecontrol.entity.Directory;
import at.htl.remotecontrol.packets.LoginPacket;
import at.htl.remotecontrol.server.TeacherServer;

import java.awt.*;
import java.io.*;
import java.net.Socket;

/**
 * 26.10.2015   Tobias      Klasse von Student auf Client umbenannt
 */
public class Client {

    private final ObjectOutputStream out;
    private final ObjectInputStream in;
    private final Robot robot;
    private RobotActionQueue jobs;
    private final HandOutThread handOutThread;
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
        handOutThread = new HandOutThread();
        processor = new ProcessorThread();
        reader = new ReaderThread();
    }

    private class HandOutThread extends Thread {
        public void run() {
            try {
                File file = new File(loginPacket.getDirOfWatch() + "/angabe.zip");
                System.out.println(getClass() + " Fetching file... " + file);
                OutputStream outputStream = new FileOutputStream(file);
                byte[] buffer = new byte[16384];
                int len;
                while ((len = in.read(buffer)) > 0)
                    outputStream.write(buffer, 0, len);
                System.out.println(getClass() + " Fetching complete.");
                outputStream.close();
                processor.start();
                reader.start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
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
                out.close();
            } catch (IOException e) {
                System.out.println("Connection closed (" + e + ')');
            }
        }
    }

    public boolean handIn() {
        return true;
    }

    public void start() {
        handOutThread.start();
    }

    public void stop() {
        processor.interrupt();
        reader.interrupt();
    }

}