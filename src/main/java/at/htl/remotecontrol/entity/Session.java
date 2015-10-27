package at.htl.remotecontrol.entity;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.LinkedList;
import java.util.Random;

/**
 * Gnadlinger:  15.10.2015  Verwaltung der Gui-Eingabewerte inplementiert
 * Philipp   :  19.10.2015  Erweiterung um eine Liste der verbundenen Studenten
 * Patrick   :  24.10.2015  Erweiterung um den String "imagePath"
 * Tobias    :  26.10.2015  Singleton-Pattern korrigiert und Klasse umbenannt
 * Tobias    :  26.10.2015  ObservableList von Studenden statt von String
 * Philipp   :  27.10.2015  Students werden noch dem Logout von der Liste entfernt
 */
public class Session {

    private static Session instance = null;
    private ObservableList<Student> students;
    private long time;
    private String imagePath;

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

    public void addStudent(Student student) {
        students.add(student);
    }

    public void removeStudent(Student student) {
        students.remove(student);
    }

    //region Getter and Setter
    public ObservableList<Student> getObservableList() {
        return students;
    }

    public long getTime() {
        if (time > 0) {
            return time;
        }
        return getRandom();
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath + "/Sceenshots/";
    }
    //endregion

    public int getRandom() {
        Random rn = new Random();
        int maximum = 30000;
        int minimum = 1000;

        int n = maximum - minimum + 1;
        int i = rn.nextInt() % n;
        int randomNum =  minimum + i;

        return randomNum;
    }
}
