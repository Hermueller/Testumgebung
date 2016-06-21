package at.htl.client;

import at.htl.client.view.Controller;
import at.htl.common.MyUtils;
import at.htl.common.actions.RobotAction;
import at.htl.common.actions.RobotActionQueue;
import at.htl.common.fx.FxUtils;
import at.htl.common.io.DocumentsTransfer;
import at.htl.common.io.FileUtils;
import at.htl.common.transfer.HandOutPackage;
import at.htl.common.transfer.LoginPackage;
import javafx.application.Platform;
import org.apache.logging.log4j.Level;

import java.awt.*;
import java.io.*;
import java.net.Socket;
import java.time.LocalTime;

/**
 * @timeline Client
 * 24.10.2015: PON 010  students can log out - thereby no longer be sent screenshots
 * 26.10.2015: MET 002  renamed this class from "Student" to "Client"
 * 31.10.2015: MET 075  function "download specification" implemented
 * 01.11.2015: MET 015  hand in a watched folder automatically zipped
 * 01.11.2015: MET 005  Bug found: hand in only immediately after login
 * 08.02.2016: GNA 005  Added Errors to LogFilesdfdf
 * 11.06.2016: PHI 020  Implemented the new Object-Stream.
 * 11.06.2016: PHI 030  implemented the endTime.
 * 21.06.2016: PHI 002  logs the student out if the connection is lost.
 */
public class Client {

    private ObjectOutputStream out;
    private final ObjectInputStream in;
    private final Robot robot;
    private RobotActionQueue jobs;
    private Socket socket;
    private final ProcessorThread processor;
    private final ReaderThread reader;
    private final LoginPackage loginPackage;
    private LocalTime endTime = null;
    private boolean signedIn = false;
    private Controller controller;

    public Client(LoginPackage loginPackage, Controller controller)
            throws IOException, AWTException {
        this.loginPackage = loginPackage;
        socket = new Socket(loginPackage.getServerIP(), loginPackage.getPort());
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
        this.controller = controller;
    }

    public ObjectOutputStream getOut() {
        return out;
    }

    public void setOut(ObjectOutputStream out) {
        this.out = out;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public boolean isSignedIn() {
        return signedIn;
    }

    /**
     * gets the file from the teacher for the test and saves it
     */
    public void loadFiles() {
        HandOutPackage handOutPackage = null;
        try {
            Object obj = in.readObject();
            handOutPackage = DocumentsTransfer.receiveObject(
                    obj, loginPackage.getDirOfWatch(), "angabe");
        } catch (IOException | ClassNotFoundException e) {
            FileUtils.log(this, Level.ERROR, "Failed to receive: " + MyUtils.exToStr(e));
        }
        if (handOutPackage != null) {
            endTime = handOutPackage.getEndTime();
            signedIn = true;
        } else {
            signedIn = false;
        }
        processor.start();
        reader.start();
    }

    /**
     * compromises the working-directory and sends it to the teacher
     *
     * @return the success of it
     */
    @SuppressWarnings({"unused deprecated", "deprecation"})
    public boolean handIn() {
        if (processor.isInterrupted() && reader.isInterrupted()) {
            String zipFileName = "handInFile.zip";
            System.out.println(loginPackage.getDirOfWatch());
            FileUtils.delete(loginPackage.getDirOfWatch() + "/" + loginPackage.getLastname() + "/angabe.zip");
            FileUtils.delete(loginPackage.getDirOfWatch() + "/" + loginPackage.getLastname() + "/handInFile.zip");
            FileUtils.zip(loginPackage.getDirOfWatch(), zipFileName);
            DocumentsTransfer.send(getOut(), new File(String.format("%s/%s",
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
                Object obj = in.readObject();
                while (obj.getClass().toString().contains(HandOutPackage.class.toString())) {
                    obj = in.readObject();
                }
                RobotAction action = (RobotAction) obj;
                do {
                    if (!action.equals(jobs.peekLast())) {
                        jobs.add(action);
                    } else {
                        FileUtils.log(this, Level.ERROR, "Discarding duplicate request");
                    }
                } while ((action = (RobotAction) in.readObject()) != null);
            } catch (EOFException eof) {
                //FileUtils.log(this, Level.ERROR, "Connection closed " + MyUtils.exToStr(eof));
            } catch (Exception ex) {
                FileUtils.log(this, Level.ERROR, "Send Boolean " + MyUtils.exToStr(ex));
            } finally {
                Platform.runLater(() -> {
                    FxUtils.showPopUp("LOST CONNECTION", false);
                    controller.logout();
                });
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
                while (!isInterrupted() && socket.isConnected()) {
                    RobotAction action = jobs.take();
                    Object result = action.execute(robot);
                    if (result != null) {
                        getOut().writeObject(result);
                        getOut().reset();
                        getOut().flush();
                    }
                }
            } catch (InterruptedException e) {
                interrupt();
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
     * called when the client logs out.
     * stop all streams from this client.
     */
    public void stop() {

        /*RobotAction action = new LittleHarvester(
                loginPackage.getLastname(), loginPackage.getDirOfWatch(), new String[0]);

        try {
            Object result = action.execute(robot);
            if (result != null) {
                getOut().writeObject(result);
                getOut().reset();
                getOut().flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }*/

        processor.interrupt();
        reader.interrupt();
        //handIn();
        closeOut();
    }
}