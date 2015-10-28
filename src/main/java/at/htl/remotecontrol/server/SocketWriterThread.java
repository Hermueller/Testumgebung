package at.htl.remotecontrol.server;

import at.htl.remotecontrol.actions.*;
import at.htl.remotecontrol.entity.Session;
import at.htl.remotecontrol.entity.Student;
import javafx.application.Platform;

import java.awt.event.MouseEvent;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.concurrent.TimeUnit;

/**
 * Philipp:   27.10.2015   Student wird nach dem Logout aus der Liste entfernt
 */

class SocketWriterThread extends Thread {

    private final RobotActionQueue jobs = new RobotActionQueue();
    private final Student student;
    private final ObjectOutputStream out;
    private volatile boolean active = false;

    public SocketWriterThread(Student student, ObjectOutputStream out) {
        super("Writer to " + student.getName());
        this.student = student;
        Session.getInstance().addStudent(student);
        this.out = out;
    }

    public void setActive(boolean active) {
        this.active = active;
        askForScreenShot();
    }

    public long getWaitTime() {
        return Session.getInstance().getTime();
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

    public void run() {
        askForScreenShot();
        try {
            while (!isInterrupted()) {
                try {
                    RobotAction action = jobs.poll(
                            getWaitTime(),
                            TimeUnit.MILLISECONDS);
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
            System.out.println("Connection to " + student.getName() +
                    " closed (" + e + ')');
        }

        System.out.println("Closing connection to " + student.getName());
        Platform.runLater(new Runnable() {
            public void run() {
                Session.getInstance().removeStudent(student);
            }
        });
    }

}