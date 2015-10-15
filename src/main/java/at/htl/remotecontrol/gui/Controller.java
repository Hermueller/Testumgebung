package at.htl.remotecontrol.gui;

import at.htl.remotecontrol.entity.Time;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by Philipp on 15.10.15.
 */
public class Controller implements Initializable{

    @FXML
    TextField tfTimeSS;

    public Controller() {

    }

    public void initialize(URL location, ResourceBundle resources) {

    }

    public void changeTime(ActionEvent actionEvent) {
        System.out.println("getted");
        Time.getInstance().setTime(Integer.parseInt(tfTimeSS.getText()));

        Thread t1 = new Thread(new Threader());

        t1.start();
    }
}
