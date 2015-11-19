package at.htl.remotecontrol.gui.controller;

import at.htl.remotecontrol.packets.LoginPacket;
import at.htl.remotecontrol.student.Client;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * 18.10.2015:  Philipp     Einfügen eines Login's mit Werteübergabe
 * 30.10.2015:  Tobias      Login/Logout erweitert
 * 31.10.2015:  Tobias      Erstellen von Client-Paketen und deren Übermittlung an Client
 *
 */
public class ControllerStudent implements Initializable {

    @FXML
    TextField tfUsername, tfTeacherIP, tfPath;

    @FXML
    PasswordField pfPassword;

    @FXML
    Button btnLogin, btnHandIn, btnLogout;

    @FXML
    public Label lbAlert;

    @FXML
    ToggleButton tbSwitch;

    private Client client;
    private boolean loggedIn;

    public void initialize(URL location, ResourceBundle resources)
    {
        this.loggedIn = false;
    }

    public void logIn(ActionEvent event) {
        try {
            if (!loggedIn) {
                client = new Client(new LoginPacket(
                        tfUsername.getText(),
                        pfPassword.getText(),
                        tfTeacherIP.getText(),
                        tfPath.getText()
                ));
                client.start();
                loggedIn = true;
            }
        } catch (AWTException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void chooseDirectory(ActionEvent event) {
        DirectoryChooser dc = new DirectoryChooser();
        dc.setInitialDirectory(new File(System.getProperty("user.home")));
        dc.setTitle("Wähle dein Ziel-Verzeichnis");
        File choosedFile = dc.showDialog(new Stage());
        if (choosedFile != null)
            tfPath.setText(String.format("%s/%s", choosedFile.getPath(), tfUsername.getText()));

    }

    public void handIn(ActionEvent event) {
        if (client.handIn()) {
            lbAlert.setText("Datei gezippt abgegeben");
        } else {
            lbAlert.setText("hand in error");
        }
    }

    public void logOut(ActionEvent actionEvent) {
        if (loggedIn) {
            client.stop();
            loggedIn = false;
        }
    }
}
