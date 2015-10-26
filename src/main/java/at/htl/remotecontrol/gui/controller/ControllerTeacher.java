package at.htl.remotecontrol.gui.controller;

import at.htl.remotecontrol.entity.Session;
import at.htl.remotecontrol.entity.Student;
import at.htl.remotecontrol.gui.Threader;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Philipp:  15.10.2015  Screenshot-Verzögerungs-Zeiteingabe durch Gui ermöglicht
 * Philipp:  19.10.2015  Liste der verbundenen Studenten
 * Patrick:  24.10.2015  DirectoryChooser für die Screenshots
 */
public class ControllerTeacher implements Initializable {

    @FXML
    public TextField tfTimeSS; // SS ... Screenshot

    @FXML
    public ListView<Student> lvStudents;

    @FXML
    public Label lbAlert;

    public ControllerTeacher() {
        Session.getInstance().setTime(3000);
    }

    public void initialize(URL location, ResourceBundle resources) {
        lvStudents.setItems(Session.getInstance().getObservableList());
    }

    public void changeTime(ActionEvent actionEvent) {
        if (Session.getInstance().getImagePath() != null) {
            Session.getInstance().setTime(Integer.parseInt(tfTimeSS.getText()));
            Thread t1 = new Thread(new Threader());
            t1.start();
        } else {
            lbAlert.setText("Bitte gib ein Verzeichnis an");
        }
    }

    public void chooseDirectory(ActionEvent actionEvent) {
        DirectoryChooser dc = new DirectoryChooser();
        dc.setInitialDirectory(new File(System.getProperty("user.home")));
        dc.setTitle("Wähle dein Ziel-Verzeichnis");
        File choosedFile = dc.showDialog(new Stage());
        Session.getInstance().setImagePath(choosedFile.getPath());
    }

}
