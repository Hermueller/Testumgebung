package at.htl.server;

import at.htl.common.MyUtils;
import at.htl.common.actions.LittleHarvester;
import at.htl.common.actions.RobotAction;
import at.htl.common.actions.RobotActionQueue;
import at.htl.common.fx.StudentView;
import at.htl.common.io.FileUtils;
import at.htl.common.io.ScreenShot;
import at.htl.common.transfer.DocumentsTransfer;
import at.htl.server.entity.Student;
import javafx.application.Platform;
import javafx.scene.control.Button;
import org.apache.logging.log4j.Level;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.concurrent.TimeUnit;

/**
 * Im SocketWriterTrhead wird die Wartezeit zwischen Screenshots kontrolliert
 *
 * @timeline SocketWriterThread
 * 27.10.2015: PHI 025  Student wird nach dem Logout aus der Liste entfernt
 * 31.10.2015: MET 060  Angabe zur Verfügung stellen
 * 11.12.2015: PHI 010  LoC werden immer mit dem Screenshot berechnet
 * 06.01.2016: PHI 005  Serie bekommt einen Namen zur vermeidung von doppelten Serien
 * 12.01.2016: PHI 055  Writer fragt jetzt auch nach dem Lines of Code.
 * 13.01.2016: PHI 045  Schweren Fehler behoben bei der Abfrage nach LinesOfCode & Screenshot (PipeÜberlastung)
 * 13.01.2016: PHI 015  Einführen des TransferPackets.
 * 06.06.2016: PHI 002  implemented the new Screenshot object.
 */
class SocketWriterThread extends Thread {

    private final Student student;
    private final ObjectOutputStream out;
    private final RobotActionQueue jobs;

    public SocketWriterThread(Student student,
                              ObjectOutputStream out) {
        super("Writer to " + student.getPupil().getLastName());
        this.student = student;
        this.out = out;
        this.jobs = new RobotActionQueue();
    }

    /**
     * the quickinfo to wait, before taking another screenshot.
     *
     * @return the quickinfo to wait.
     */
    public long getWaitTime() {
        return student.getInterval().getValue();
    }

    /**
     * adds a new job for the robot.
     * The Screenshot from the Student.
     * Counts the Lines of Code from the Student.
     *
     * @param student The client, of the screenshot and LinesOfCode
     */
    private void sendLittleHarvester(Student student) {
        ScreenShot screenShot = Settings.getInstance().getScreenShot();
        jobs.add(new LittleHarvester(student.getPupil().getLastName(),
                student.getPupil().getPathOfProject(),
                student.getFilter(),
                screenShot));
    }


    /**
     * sends the test-file to the students.
     */
    public void handOut() {
        DocumentsTransfer.sendObject(out, Settings.getInstance().getPacket());
    }

    public void run() {
        handOut();


        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
           // e.printStackTrace();
        }

        student.addSeries();
        sendLittleHarvester(student);

        Platform.runLater(() -> {
            Button selected = (Button)StudentView.getInstance().getLv().getSelectionModel().getSelectedItem();
            if (selected != null) {
                if (selected.getText().equals(student.getPupil().getLastName())) {
                    if (student.getSeries().size() > 0) {
                        student.addNewestToChart();
                    }
                }
            }
        });

        try {
            while (!isInterrupted()) {
                try {
                    RobotAction action = jobs.poll(
                            getWaitTime(), TimeUnit.SECONDS);
                    if (action == null) {
                        sendLittleHarvester(student);
                    } else {
                        out.writeObject(action);
                        out.reset();
                        out.flush();
                    }
                } catch (InterruptedException e) {
                    interrupt();
                    break;
                }
                catch (Exception ex) {
                }
            }
            out.close();
        } catch (IOException e) {
            FileUtils.log(this, Level.ERROR,"Connection to " + student.getPupil().getLastName()
                    + " closed" + MyUtils.exToStr(e));
        }
        FileUtils.log(this,Level.INFO,"Closing connection to " + student.getPupil().getLastName());
    }

}