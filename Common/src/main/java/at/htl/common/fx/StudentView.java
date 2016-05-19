package at.htl.common.fx;

import javafx.scene.control.ListView;
import javafx.scene.image.ImageView;

/**
 * @timeline StudentView
 * 27.10.2015: PHI 015  Erstellung dieser Singleton-Klasse
 * 28.10.2015: PHI 005  Erweitert um die ListView
 */
public class StudentView {

    private static StudentView instance = null;

    private ImageView iv = null;    //Screenshot-Image
    private ListView lv = null;

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

    public ListView getLv() {
        return lv;
    }

    public void setLv(ListView lv) {
        this.lv = lv;
    }
}
