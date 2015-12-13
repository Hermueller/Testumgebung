package at.htl.remotecontrol.server;

import at.htl.remotecontrol.actions.*;
import at.htl.remotecontrol.entity.FileStream;
import at.htl.remotecontrol.entity.LineCounter;
import at.htl.remotecontrol.entity.Session;
import at.htl.remotecontrol.entity.Student;

import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.concurrent.TimeUnit;

/**
 * Im SocketWriterTrhead wird die Wartezeit zwischen Screenshots kontrolliert
 *
 * 27.10.2015:  Philipp     ??? Student wird nach dem Logout aus der Liste entfernt
 * 31.10.2015:  Tobias      ??? Angabe zur Verfügung stellen
 * 11.12.2015:  Philipp     010 LoC werden immer mit dem Screenshot berechnet
 */
class SocketWriterThread extends Thread {

    private final Student student;
    private final ObjectOutputStream out;
    private final RobotActionQueue jobs;
    private volatile boolean active;

    public SocketWriterThread(Student student,
                              ObjectOutputStream out) {
        super("Writer to " + student.getName());
        this.student = student;
        this.out = out;
        this.jobs = new RobotActionQueue();
        this.active = false;
    }

    public void setActive(boolean active) {
        this.active = active;
        askForScreenShot();
    }

    /**
     * the time to wait, before taking another screenshot.
     * @return  the time to wait.
     */
    public long getWaitTime() {
        return Session.getInstance().getInterval();
    }

    /**
     * if teacher clicks on the screenshot, the click is also on the students-screen.
     * @param e Specialises the click.
     */
    public void clickEvent(MouseEvent e) {
        if (active) {
            jobs.add(new MoveMouse(e));
            jobs.add(new ClickMouse(e));
        }
        active = true;
        askForScreenShot();
    }

    /**
     * adds a new job for the robot.
     */
    private void askForScreenShot() {
        jobs.add(new ScreenShot(1.0));
    }

    /**
     * sends the test-file to the students.
     */
    public void handOut() {
        FileStream.send(out, Session.getInstance().getHandOutFile());
    }

    public void run() {
        handOut();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        askForScreenShot();
        LineCounter lc = new LineCounter();
        Session.getInstance().newSeries("LoC");
        try {
            while (!isInterrupted()) {
                try {
                    RobotAction action = jobs.poll(
                            getWaitTime(), TimeUnit.MILLISECONDS);
                    if (action == null) {
                        // we had a timeout, so do a screen capture
                        askForScreenShot();
                        //also we want to count the lines in the code
                        Long _loc = lc.listFilesForFolder(new File(student.getPathOfWatch()));
                        student.addLoC(_loc);
                        Session.getInstance().addValue(_loc);
                    } else {
                        out.writeObject(action);
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
            System.out.println("Connection to " + student.getName() + " closed (" + e + ')');
        }
        System.out.println("Closing connection to " + student.getName());
        Session.getInstance().removeStudent(student.getName());
    }

}