package at.htl.remotecontrol.entity;

import java.util.LinkedList;
import java.util.List;

/**
 * @timeline Text
 * 26.10.2015: MET 001  Klasse erstellt
 * 26.10.2015: MET 005  Name des Schülers, Verzeichnis der Screenshots
 * 31.10.2015: MET 005  Funktion für Verzeichnis der Screenshots verbessert
 * 31.12.2015: PHI 001  Getter und Setter für locs und times erstellt.
 */
public class Student {

    private String name;
    private String pathOfWatch;
    private String pathOfImages;
    private List<Long> locs = new LinkedList<>();
    private List<Long> times = new LinkedList<>();

    public Student(String name, String pathOfWatch) {
        this.name = name;
        this.pathOfWatch = pathOfWatch;
        setPathOfImages(Session.getInstance().getPathOfImages());
    }

    //region Getter and Setter
    public String getName() {
        return name;
    }

    public String getPathOfWatch() {
        return pathOfWatch;
    }

    public String getPathOfImages() {
        return pathOfImages;
    }

    private void setPathOfImages(String path) {
        path = String.format("%s/%s", path, name);
        if (Directory.create(path))
            this.pathOfImages = path;
    }

    public List<Long> getLocs() {
        return locs;
    }

    public List<Long> getTimes() {
        return times;
    }

    //endregion

    @Override
    public String toString() {
        return name;
    }

    /**
     * To remember the Lines of Code for exactly this student.
     * Saves the Lines of Code.
     *
     * @param _loc  Specifies the lines of code at an specific time.
     * @param _time Specifies the time when the program counted the lines.
     */
    public void addLoC_Time(Long _loc, Long _time) {
        locs.add(_loc);
        times.add(_time);
    }
}
