package at.htl.remotecontrol.entity;

import at.htl.remotecontrol.packets.HandOutPacket;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.TextField;

import java.io.File;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.LinkedList;

/**
 * @timeline Text
 * 15.10.2015: GNA 001  Klasse erstellt
 * 15.10.2015: GNA 010  Verwaltung der Gui-Eingabewerte inplementiert
 * 19.10.2015: PHI 015  Erweiterung um eine Liste der verbundenen Studenten
 * 24.10.2015: PHI 003  Erweiterung um den String "pathOfImages"
 * 26.10.2015: MET 005  Singleton-Pattern korrigiert und Klasse von Time auf Session umbenannt
 * 26.10.2015: MET 003  ObservableList von Studenten statt von String
 * 27.10.2015: PHI 035  Students werden nach dem Logout von der Liste entfernt
 * 30.10.2015: PHI 030  fixe/zufällige Zeitspanne zwischen Screenshots erstellt
 * 30.10.2015: MET 005  fixe/zufällige Zeitspanne zwischen Screenshots erstellt
 * 31.10.2015: MET 010  Funktion implementiert: Testbeginn und Testende festlegen
 * 31.10.2015: MET 005  Erweiterung um handOutFile und getHandOutPacket()
 * 06.11.2015: PON 005  Erweiterung um password
 * 29.11.2015: PHI 040  Hinzufügen und Entfernen von Studenten geändert (farbige)TestField
 * 10.12.2015: PHI 025  Einbinden von Funktionen, die für die Lines of Code benötigt werden
 * 12.12.2015: PHI 035  kommentieren von Methoden und die Klassenstruktur geändert
 * 22.12.2015: PHI 020  ändern der Anzeige von Schülern beim Lehrer.
 */
public class Session {

    private static Session instance = null;
    private ObservableList<TextField> students;
    private File handOutFile;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Interval interval;
    private String path;
    private String pathOfImages;
    private String pathOfHandOutFiles;
    private String password;
    private XYChart.Series series;
    private LocalDateTime starting = null;
    private LineChart<Number, Number> chart;
    private String[] endings;
    private List<TextField> preStudents = new LinkedList<>();

    protected Session() {
        students = FXCollections.observableList(new LinkedList<>());
    }

    public static Session getInstance() {
        if (instance == null) {
            instance = new Session();
        }
        return instance;
    }

    //region Getter and Setter

    /**
     * @return the list of students.
     */
    public ObservableList<TextField> getObservableList() {
        return students;
    }

    /**
     * @return the file for the test with the test-questions.
     */
    public File getHandOutFile() {
        return handOutFile;
    }

    /**
     * @param handOutFile Specialises the file for the test.
     */
    public void setHandOutFile(File handOutFile) {
        this.handOutFile = handOutFile;
    }

    /**
     * @return the password.
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param password Specialises the password.
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * @return the package of information for the student.
     */
    public HandOutPacket getHandOutPacket() {
        // Prüfung, ob nötige Daten vorhanden fehlt
        // funktioniert noch nicht
        return new HandOutPacket(handOutFile, endTime, "Viel Glück!");
    }

    /**
     * @return the time when the test starts.
     */
    public LocalDateTime getStartTime() {
        return startTime;
    }

    /**
     * @param startTime Specialises the time when the test starts.
     */
    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    /**
     * @return the time when the test ends.
     */
    public LocalDateTime getEndTime() {
        return endTime;
    }

    /**
     * @param endTime Specialises the time when the test ends.
     */
    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    /**
     * @return the time to wait for the next screenshot.
     */
    public long getInterval() {
        return interval.getValue();
    }

    /**
     * @param interval Specialises the class with the calculations for the next interval-time.
     */
    public void setInterval(Interval interval) {
        this.interval = interval;
    }

    /**
     * @return the root-path of the directory of the images and finished tests directory.
     */
    public String getPath() {
        return path;
    }

    /**
     * @return the path of the directory of screenshots
     */
    public String getPathOfImages() {
        return pathOfImages;
    }

    /**
     * @return the path of the directory of finished tests
     */
    public String getPathOfHandInFiles() {
        return pathOfHandOutFiles;
    }

    /**
     * sets the path for the directory of the screenshots and finished tests.
     *
     * @param path Specifies the root-path of the screenshots and finished tests
     */
    public void setPath(String path) {
        this.path = path;
        pathOfImages = path + "/Sceenshots";
        Directory.create(pathOfImages);
        pathOfHandOutFiles = path + "/Abgabe";
        Directory.create(pathOfHandOutFiles);

        System.out.println(pathOfImages);
    }

    /**
     * @param chart Specialises the chart which is shown on the screen of the teacher.
     */
    public void setChart(LineChart<Number, Number> chart) {
        this.chart = chart;
    }

    /**
     * @return the endings of the files (in which the lines are counted).
     */
    public String[] getEndings() {
        return endings;
    }

    /**
     * @param endings The endings of files in which we count the lines.
     */
    public void setEndings(String[] endings) {
        this.endings = endings;
    }

    //endregion

    //region Methods

    /**
     * Adds a student to the list of all students and colours him green.
     *
     * @param student Specialises the student which will be added.
     */
    public void addStudent(final Student student) {
        Platform.runLater(() -> {
            TextField tf = new TextField(student.getName());
            tf.setEditable(false);
            tf.setStyle("-fx-background-color: crimson");
            students.add(tf);
            //preStudents.add(tf);
        });
    }

    /**
     * Notifies the teacher that the student has logged in.
     *
     * @param student   the student which logged in.
     */
    public void loginStudent(final Student student) {
        Platform.runLater(() -> {
            for (TextField tf : students) {
                if (tf.getText().equals(student.getName())) {
                    students.add(tf);
                    tf.setStyle("-fx-background-color: greenyellow");
                    break;
                }
            }
        });
    }

    /**
     * Removes a student from the list of all students and/or colours him red.
     *
     * @param studentName Specialises the student to remove from the list.
     */
    public void removeStudent(final String studentName) {
        Platform.runLater(() -> {
            for (TextField tf : students) {
                if (tf.getText().equals(studentName)) {
                    //students.remove(tf);
                    tf.setStyle("-fx-background-color: crimson");
                    break;
                }
            }
        });
    }

    /**
     * creates a new line in the chart.
     *
     * @param name Specialises the name of the new line.
     */
    public void newSeries(String name) {
        series = new XYChart.Series();
        series.setName(name);

        Platform.runLater(() -> chart.getData().add(series));
    }

    /**
     * Add the Number of Lines in the code to the chart.
     *
     * @param _loc Specialises the number of lines in the code.
     */
    public void addValue(Long _loc) {
        if (starting == null) {
            starting = LocalDateTime.now();
        }
        LocalDateTime now = starting;

        Long _hours = now.until(LocalDateTime.now(), ChronoUnit.HOURS);
        now = now.plusHours(_hours);

        Long _minutes = now.until(LocalDateTime.now(), ChronoUnit.MINUTES);
        now = now.plusMinutes(_minutes);

        Long _seconds = now.until(LocalDateTime.now(), ChronoUnit.SECONDS);

        Long _time = _seconds + _minutes * 60 + _hours * 60 * 60;

        Platform.runLater(() -> {
            chart.getData().remove(series);
            series.getData().add(new XYChart.Data<Number, Number>(_time, _loc));
            chart.getData().add(series);
        });
    }

    //endregion
}