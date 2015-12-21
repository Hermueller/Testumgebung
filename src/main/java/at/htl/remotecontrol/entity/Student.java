package at.htl.remotecontrol.entity;

import java.util.LinkedList;
import java.util.List;

/**
 * @timeline Text
 * 26.10.2015: MET ???  Klasse erstellt
 * 26.10.2015: MET ???  Name des Schülers, Verzeichnis der Screenshots
 * 31.10.2015: MET ???  Funktion für Verzeichnis der Screenshots verbessert
 */
public class Student {

    private String name;
    private String pathOfWatch;
    private String pathOfImages;
    private List<Long> loc = new LinkedList<>();

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
    //endregion

    @Override
    public String toString() {
        return name;
    }

    /**
     * To remember the Lines of Code for exactly this student.
     * Saves the Lines of Code.
     *
     * @param lines Specifies the lines of code at an specific time
     */
    public void addLoC(Long lines) {
        loc.add(lines);
    }

}
