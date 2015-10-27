package at.htl.remotecontrol.entity;

import javafx.scene.image.ImageView;

/**
 * Philipp:  27.Oktober.2015  Erstellung dieser Singleton-Klasse
 */
public class StudentView {

    private static StudentView instance = null;

    private ImageView iv = null;    //Screenshot-Image
    /*   HIER werden Variablen für 'Lines Of Code' hinkommen   */

    private StudentView() {

    }

    public static StudentView getInstance() {
        if (instance == null) {
            instance = new StudentView();
        }
        return instance;
    }

    public ImageView getIv() {
        return iv;
    }

    public void setIv(ImageView iv) {
        this.iv = iv;
    }
}
