package at.htl.remotecontrol.server;

import at.htl.remotecontrol.actions.*;
import at.htl.remotecontrol.entity.FileStream;
import at.htl.remotecontrol.entity.Session;
import at.htl.remotecontrol.entity.Student;

import java.awt.event.MouseEvent;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.concurrent.TimeUnit;

/**
 * Im SocketWriterTrhead wird die Wartezeit zwischen Screenshots kontrolliert
 *
 * 27.10.2015:  Philipp     Student wird nach dem Logout aus der Liste entfernt
 * 31.10.2015:  Tobias      Angabe zur Verfügung stellen
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

    public long getWaitTime() {
        return Session.getInstance().getInterval();
    }

    public void clickEvent(MouseEvent e) {
        if (active) {
            jobs.add(new MoveMouse(e));
            jobs.add(new ClickMouse(e));
        }
        active = true;
        askForScreenShot();
    }

    private void askForScreenShot() {
        jobs.add(new ScreenShot(1.0));
    }

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
        try {
            while (!isInterrupted()) {
                try {
                    RobotAction action = jobs.poll(
                            getWaitTime(), TimeUnit.MILLISECONDS);
                    if (action == null) {
                        // we had a timeout, so do a screen capture
                        askForScreenShot();
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