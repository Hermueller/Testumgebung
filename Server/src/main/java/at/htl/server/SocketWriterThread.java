package at.htl.server;

import at.htl.common.MyUtils;
import at.htl.server.entity.Student;
import at.htl.common.actions.LittleHarvester;
import at.htl.common.actions.RobotAction;
import at.htl.common.actions.RobotActionQueue;
import at.htl.common.fx.StudentView;
import at.htl.common.io.FileStream;
import at.htl.common.io.FileUtils;
import javafx.application.Platform;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import org.apache.logging.log4j.Level;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.concurrent.TimeUnit;

/**
 * Im SocketWriterTrhead wird die Wartezeit zwischen Screenshots kontrolliert
 *
 * @timeline .
 * 27.10.2015: PHI 025  Student wird nach dem Logout aus der Liste entfernt
 * 31.10.2015: MET 060  Angabe zur Verfügung stellen
 * 11.12.2015: PHI 010  LoC werden immer mit dem Screenshot berechnet
 * 06.01.2016: PHI 005  Serie bekommt einen Namen zur vermeidung von doppelten Serien
 * 12.01.2016: PHI 055  Writer fragt jetzt auch nach dem Lines of Code.
 * 13.01.2016: PHI 045  Schweren Fehler behoben bei der Abfrage nach LinesOfCode & Screenshot (PipeÜberlastung)
 * 13.01.2016: PHI 015  Einführen des TransferPackets.
 */
class SocketWriterThread extends Thread {

    private final Student student;
    private final ObjectOutputStream out;
    private final RobotActionQueue jobs;

    public SocketWriterThread(Student student,
                              ObjectOutputStream out) {
        super("Writer to " + student.getName());
        this.student = student;
        this.out = out;
        this.jobs = new RobotActionQueue();
    }

    /**
     * the time to wait, before taking another screenshot.
     *
     * @return the time to wait.
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
        jobs.add(new LittleHarvester(student.getName(), student.getPathOfWatch(), student.getFilter()));
    }


    /**
     * sends the test-file to the students.
     */
    public void handOut() {
        FileStream.send(out, Settings.getInstance().getHandOutFile());
    }

    public void run() {
        handOut();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        student.addSeries();
        sendLittleHarvester(student);

        Platform.runLater(() -> {
            Button selected = (Button)StudentView.getInstance().getLv().getSelectionModel().getSelectedItem();
            if (selected != null) {
                if (selected.getText().equals(student.getName())) {
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
            }
            out.close();
        } catch (IOException e) {
            FileUtils.log(this, Level.ERROR,"Connection to " + student.getName() + " closed" + MyUtils.exToStr(e));
        }
        FileUtils.log(this,Level.INFO,"Closing connection to " + student.getName());
        Settings.getInstance().printErrorLine(
                Level.INFO, "Closing connection to " + student.getName());
    }

}