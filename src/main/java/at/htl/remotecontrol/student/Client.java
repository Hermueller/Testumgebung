package at.htl.remotecontrol.student;

import at.htl.remotecontrol.actions.RobotAction;
import at.htl.remotecontrol.actions.RobotActionQueue;
import at.htl.remotecontrol.entity.FileUtils;
import at.htl.remotecontrol.entity.FileStream;
import at.htl.remotecontrol.packets.LoginPackage;

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

    private ObjectOutputStream out;
    private final ObjectInputStream in;
    private final Robot robot;
    private RobotActionQueue jobs;
    private final ProcessorThread processor;
    private final ReaderThread reader;
    private final LoginPackage loginPackage;

    public Client(LoginPackage loginPackage)
            throws IOException, AWTException {
        this.loginPackage = loginPackage;
        Socket socket = new Socket(loginPackage.getServerIP(), loginPackage.getPort());
        FileUtils.createDirectory(loginPackage.getDirOfWatch());
        robot = new Robot();
        jobs = new RobotActionQueue();
        in = new ObjectInputStream(
                new BufferedInputStream(socket.getInputStream()));
        setOut(new ObjectOutputStream(socket.getOutputStream()));
        getOut().writeObject(loginPackage);
        getOut().flush();
        processor = new ProcessorThread();
        reader = new ReaderThread();
    }

    public ObjectOutputStream getOut() {
        return out;
    }

    public void setOut(ObjectOutputStream out) {
        this.out = out;
    }

    /**
     * gets the file from the teacher for the test and saves it
     */
    public void loadFiles() {
        FileStream.receive(in, loginPackage.getDirOfWatch() + "/angabe.zip");
        processor.start();
        reader.start();
    }

    /**
     * compromises the working-directory and sends it to the teacher
     *
     * @return the success of it
     */
    public boolean handIn() {
        if (processor.isInterrupted() && reader.isInterrupted()) {
            String zipFileName = "handInFile.zip";
            System.out.println(loginPackage.getDirOfWatch());
            FileUtils.delete(loginPackage.getDirOfWatch() + "/" + loginPackage.getUserName() + "/angabe.zip");
            FileUtils.delete(loginPackage.getDirOfWatch() + "/" + loginPackage.getUserName() + "/handInFile.zip");
            FileUtils.zip(loginPackage.getDirOfWatch(), zipFileName);
            FileStream.send(getOut(), new File(String.format("%s/%s",
                    loginPackage.getDirOfWatch(), zipFileName)));
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
                System.out.println("Send Boolean");
                /*try {
                    out.writeBoolean(isTestFinished.isSelected());
                } catch (IOException e) {
                    System.out.println("Can't send Boolean: " + e);
                }*/
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
                            getOut().writeObject(result);
                            getOut().reset();
                            getOut().flush();
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
            getOut().close();
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
        //Session.getInstance().removeStudent(loginPackage.getUserName());
        processor.interrupt();
        reader.interrupt();
    }

}