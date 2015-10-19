package at.htl.remotecontrol.gui.controller;

import at.htl.remotecontrol.entity.Time;
import at.htl.remotecontrol.gui.Threader;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.*;

/**
 * Created by Philipp on 15.10.15.
 *
 * Philipp:  15.Oktober.2015  Screenshot-Verzögerungs-Zeiteingabe durch Gui ermöglicht
 * Philipp:  19.Oktober.2015  Liste der verbundenen Studenten
 */
public class ControllerTeacher implements Initializable{

    @FXML
    public TextField tfTimeSS;

    @FXML
    public ListView lvStudents;

    public ControllerTeacher() {

    }

    public void initialize(URL location, ResourceBundle resources) {
        lvStudents.setItems(Time.getInstance().getObservableList());
    }

    public void changeTime(ActionEvent actionEvent) {
        Time.getInstance().setTime(Integer.parseInt(tfTimeSS.getText()));

        Thread t1 = new Thread(new Threader());

        t1.start();
    }
}
