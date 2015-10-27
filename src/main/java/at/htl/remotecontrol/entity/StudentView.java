package at.htl.remotecontrol.entity;

/**
 * Philipp:  27.Oktober.2015  Erstellung dieser Singleton-Klasse
 */
public class StudentView {

    private static StudentView instance = null;



    private StudentView() {

    }

    public static StudentView getInstance() {
        if (instance == null) {
            instance = new StudentView();
        }
        return instance;
    }
}
