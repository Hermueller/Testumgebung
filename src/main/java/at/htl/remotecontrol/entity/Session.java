package at.htl.remotecontrol.entity;

import at.htl.remotecontrol.packets.HandOutPacket;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.File;
import java.time.LocalDateTime;
import java.util.LinkedList;

/**
 * 15.10.2015:  Gnadi       Klasse erstellt
 * 15.10.2015:  Gnadi       Verwaltung der Gui-Eingabewerte inplementiert
 * 19.10.2015:  Philipp     Erweiterung um eine Liste der verbundenen Studenten
 * 24.10.2015:  Philipp     Erweiterung um den String "pathOfImages"
 * 26.10.2015:  Tobias      Singleton-Pattern korrigiert und Klasse von Time auf Session umbenannt
 * 26.10.2015:  Tobias      ObservableList von Studenten statt von String
 * 27.10.2015:  Philipp     Students werden nach dem Logout von der Liste entfernt
 * 30.10.2015:  Ph, Tobi    fixe/zufällige Zeitspanne zwischen Screenshots erstellt
 * 31.10.2015:  Tobias      Funktion implementiert: Testbeginn und Testende festlegen
 * 31.10.2015:  Tobias      Erweiterung um handOutFile und getHandOutPacket()
 */
public class Session {

    private static Session instance = null;
    private ObservableList<Student> students;
    private File handOutFile;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Interval interval;
    private String path;
    private String pathOfImages;
    private String pathOfHandOutFiles;

    protected Session() {
        students = FXCollections.observableList(new LinkedList<Student>());
    }

    /**
     * Eine Stunde mit 3000 Millisekunden sind ca. 1 200 MB von Screenshots
     */
    public static Session getInstance() {
        if (instance == null) {
            instance = new Session();
        }
        return instance;
    }

    //region Getter and Setter
    public ObservableList<Student> getObservableList() {
        return students;
    }

    public File getHandOutFile() {
        return handOutFile;
    }

    public void setHandOutFile(File handOutFile) {
        this.handOutFile = handOutFile;
    }

    public HandOutPacket getHandOutPacket() {
        // Prüfung, ob nötige Daten vorhanden fehlt
        // funktioniert noch nicht
        return new HandOutPacket(handOutFile, endTime, "Viel Glück!");
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public long getInterval() {
        return interval.getValue();
    }

    public void setInterval(Interval interval) {
        this.interval = interval;
    }

    public String getPath() {
        return path;
    }

    public String getPathOfImages() {
        return pathOfImages;
    }

    public String getPathOfHandInFiles() {
        return pathOfHandOutFiles;
    }

    public void setPath(String path) {
        this.path = path;
        pathOfImages = path + "/Sceenshots";
        Directory.create(pathOfImages);
        pathOfHandOutFiles = path + "/Abgabe";
        Directory.create(pathOfHandOutFiles);
    }
    //endregion

    public void addStudent(final Student student) {
        Platform.runLater(new Runnable() {
            public void run() {
                students.add(student);
            }
        });
    }

    public void removeStudent(final Student student) {
        Platform.runLater(new Runnable() {
            public void run() {
                students.remove(student);
            }
        });
    }

}
