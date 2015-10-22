package at.htl.remotecontrol.gui.controller;

import at.htl.remotecontrol.Student;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Philipp:  18.Oktober.2015  einfügen eines Login's mit Werteübergabe
 *
 *
 */
public class ControllerStudent implements Initializable {

    @FXML
    TextField _username, _teacherIP;

    @FXML
    Button _login;

    public ControllerStudent() {

    }

    public void initialize(URL location, ResourceBundle resources) {

    }

    public void logIn(ActionEvent event) {
        try {
            Student student = new Student(_teacherIP.getText(), _username.getText());
            student.start();
        } catch (AWTException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
