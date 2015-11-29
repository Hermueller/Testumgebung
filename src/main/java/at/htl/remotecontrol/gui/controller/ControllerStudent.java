package at.htl.remotecontrol.gui.controller;

import at.htl.remotecontrol.entity.Session;
import at.htl.remotecontrol.packets.LoginPacket;
import at.htl.remotecontrol.student.Client;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.text.TextAlignment;
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
 * 29.11.2015:  Philipp     Fehlermeldungen in GUI
 */
public class ControllerStudent implements Initializable {

    @FXML
    TextField tfUsername, tfTeacherIP, tfPath, tfPassword;

    @FXML
    Button btnLogin, btnLogOut, btnfilePath;

    @FXML
    public Label lbAlert;

    @FXML
    ToggleButton tbSwitch;

    private Client client;
    private boolean loggedIn;

    public void initialize(URL location, ResourceBundle resources) {
        this.loggedIn = false;

        // Text in Textfeld mittig setzen
        lbAlert.setTextAlignment(TextAlignment.CENTER);
        lbAlert.setAlignment(Pos.CENTER);
    }

    private void setMsg(boolean error, String msg) {
        String color = (error ? "red" : "limegreen");   //bei Fehlermeldung rot, sonst grün
        lbAlert.setText(msg);
        lbAlert.setStyle("-fx-background-color: " + color);
    }

    public void logIn(ActionEvent event) {
        boolean ready = true;

        for (TextField tf : Session.getInstance().getObservableList()) {
            System.out.println(tf.getText());
            if (tf.getText().equals(tfUsername.getText())) {
                setMsg(true, "Studentname " + tfUsername.getText() + " is already in use !!");
            }
        }

        if (tfUsername.getText().length() <= 0) {
            setMsg(true, "Name bitte angeben");
            ready = false;
        } else if (tfTeacherIP.getText().length() <= 0) {
            setMsg(true, "Lehrer-IP bitte angeben");
            ready = false;
        } else if (tfPath.getText().length() <= 0) {
            setMsg(true, "Arbeits-Ordner bitte auswählen");
            ready = false;
        } else if (tfPassword.getText().length() <= 0) {
            setMsg(true, "Passwort ist falsch");
            ready = false;
        }

        try {
            if (!loggedIn && ready) {
                btnLogin.setDisable(true);
                btnLogOut.setDisable(false);
                client = new Client(new LoginPacket(
                        tfUsername.getText(),
                        "hugo",
                        tfTeacherIP.getText(),
                        tfPath.getText()
                ));
                setMsg(false, "Angemeldet");
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
        btnLogin.setDisable(false);
        btnLogOut.setDisable(true);
        if (loggedIn) {
            client.stop();
            loggedIn = false;
        }
        setMsg(false, "Abgemeldet");
    }

}
