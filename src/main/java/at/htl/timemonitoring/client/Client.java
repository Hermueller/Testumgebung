package at.htl.timemonitoring.client;

import at.htl.timemonitoring.common.MyUtils;
import at.htl.timemonitoring.common.actions.RobotAction;
import at.htl.timemonitoring.common.actions.RobotActionQueue;
import at.htl.timemonitoring.common.io.FileStream;
import at.htl.timemonitoring.common.io.FileUtils;
import at.htl.timemonitoring.common.trasfer.LoginPackage;
import at.htl.timemonitoring.server.Settings;
import javafx.application.Platform;
import org.apache.logging.log4j.Level;

import java.awt.*;
import java.io.*;
import java.net.Socket;

/**
 * @timeline .
 * 24.10.2015: PON 010  students can log out - thereby no longer be sent screenshots
 * 26.10.2015: MET 002  renamed this class from "Student" to "Client"
 * 31.10.2015: MET 075  function "download specification" implemented
 * 01.11.2015: MET 015  hand in a watched folder automatically zipped
 * 01.11.2015: MET 005  Bug found: hand in only immediately after login
 * 08.02.2016: GNA 005  Added Errors to LogFile
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
            FileUtils.delete(loginPackage.getDirOfWatch() + "/" + loginPackage.getLastname() + "/angabe.zip");
            FileUtils.delete(loginPackage.getDirOfWatch() + "/" + loginPackage.getLastname() + "/handInFile.zip");
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
                        FileUtils.log(this, Level.ERROR, "Discarding duplicate request");
                    }
                }
            } catch (EOFException eof) {
                FileUtils.log(this, Level.ERROR, "Connection closed" + MyUtils.exToStr(eof));
            } catch (Exception ex) {
                FileUtils.log(this, Level.ERROR, "Send Boolean" + MyUtils.exToStr(ex));
            } finally {
                Platform.runLater(() -> Settings.getInstance().showPopUp("LOST CONNECTION", false));
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
                FileUtils.log(this, Level.ERROR, "Connection closed" + MyUtils.exToStr(e));
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
            FileUtils.log(this, Level.ERROR, "Error by closing of ObjectOutStream!" + MyUtils.exToStr(e));
        }
    }

    /**
     * start-point for the thread
     */
    public void start() {
        loadFiles();
    }

    /**
     * client logs out -> stop all streams from this client.
     */
    public void stop() {
        processor.interrupt();
        reader.interrupt();
        //handIn();
        closeOut();
    }

}