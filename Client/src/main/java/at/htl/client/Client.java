package at.htl.client;

import at.htl.common.MyUtils;
import at.htl.common.actions.RobotAction;
import at.htl.common.actions.RobotActionQueue;
import at.htl.common.fx.FxUtils;
import at.htl.common.io.FileUtils;
import at.htl.common.transfer.DocumentsTransfer;
import at.htl.common.transfer.Packet;
import javafx.application.Platform;
import org.apache.logging.log4j.Level;

import java.awt.*;
import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.time.LocalTime;

import static at.htl.common.transfer.Packet.Action;
import static at.htl.common.transfer.Packet.Resource;

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
 */
public class Client {

    private ObjectOutputStream out;
    private final ObjectInputStream in;
    private final Robot robot;
    private RobotActionQueue jobs;
    private Socket socket;
    private final ProcessorThread processor;
    private final ReaderThread reader;
    private LocalTime endTime = null;
    private boolean signedIn = false;

    public Client(Packet packet) throws IOException, AWTException {
        try {
            socket = new Socket(Exam.getInstance().getServerIP(), Exam.getInstance().getPort());
        } catch (Exception e) {
            FxUtils.showPopUp("ERROR: Connection to server failed!", false);
        }
        if (socket != null) {
            FileUtils.createDirectory(Exam.getInstance().getPupil().getPathOfProject());
            robot = new Robot();
            jobs = new RobotActionQueue();
            in = new ObjectInputStream(new BufferedInputStream(socket.getInputStream()));
            setOut(new ObjectOutputStream(socket.getOutputStream()));
            DocumentsTransfer.sendObject(out, packet);
            processor = new ProcessorThread();
            reader = new ReaderThread();
        } else {
            robot = null;
            in = null;
            processor = null;
            reader = null;
        }
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
     * extracts the information from the received HandOutPackage.
     * Saves the file from the package.
     */
    public void loadFiles() {
        try {
            Packet packet = (Packet) in.readObject();
            if (packet.getAction() == Action.HAND_OUT) {
                byte[] handout = (byte[]) packet.get(Resource.FILE);
                if (handout.length != 0) {
                    Files.write(new File(Exam.getInstance().getPupil().getPathOfProject()
                            + "/angabe." + packet.get(Resource.FILE_EXTENSION)).toPath(), handout);
                }
                System.out.println(packet.get(Resource.COMMENT));
                endTime = (LocalTime) packet.get(Resource.TIME);
                signedIn = true;
            }
        } catch (IOException | ClassNotFoundException e) {
            signedIn = false;
            FileUtils.log(this, Level.ERROR, "Failed to receive: " + MyUtils.exToStr(e));
        }
        processor.start();
        reader.start();
    }

    /**
     * compromises the working-directory and sends it to the teacher
     *
     * @return the success of it
     */
    public boolean handIn() {
        /*if (processor.isInterrupted() && reader.isInterrupted()) {
            String zipFileName = "handInFile.zip";
            System.out.println(packet.getDirOfWatch());
            FileUtils.delete(packet.getDirOfWatch() + "/" + packet.getLastname() + "/angabe.zip");
            FileUtils.delete(packet.getDirOfWatch() + "/" + packet.getLastname() + "/handInFile.zip");
            FileUtils.zip(packet.getDirOfWatch(), zipFileName);
            DocumentsTransfer.send(getOut(), new File(String.format("%s/%s",
                    packet.getDirOfWatch(), zipFileName)));
            return true;
        }
        return false;
        */
        return false;
    }

    /**
     * reads jobs from the stream and adds them as a new job
     */
    private class ReaderThread extends Thread {
        public void run() {
            try {
                Object obj = in.readObject();
                while (obj.getClass().toString().contains(Packet.class.toString())) {
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
                Platform.runLater(() -> FxUtils.showPopUp("LOST CONNECTION", false));
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
                    DocumentsTransfer.sendObject(getOut(), jobs.take().execute(robot));
                }
            } catch (InterruptedException e) {
                interrupt();
            } catch (IOException e) {
                FileUtils.log(this, Level.ERROR, "Connection closed " + MyUtils.exToStr(e));
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
    public boolean start() {
        if (processor != null && reader != null) {
            loadFiles();
            return true;
        } else {
            return false;
        }
    }

    /**
     * called when the client logs out.
     * stop all streams from this client.
     */
    public void stop() {
        if (processor != null && reader != null) {
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
}