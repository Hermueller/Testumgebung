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
import javafx.scene.control.*;
import javafx.scene.control.TextField;
import javafx.scene.text.TextAlignment;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * @timeline Text
 * 18.10.2015: PHI ???  Einfügen eines Login's mit Werteübergabe
 * 30.10.2015: MET ???  Login/Logout erweitert
 * 31.10.2015: MET ???  Erstellen von Client-Paketen und deren Übermittlung an Client
 * 19.11.2015: PON 010  Port hinzugefügt
 * 29.11.2015: PHI ???  Fehlermeldungen in GUI
 */
public class ControllerStudent implements Initializable {

    @FXML
    TextField tfUsername, tfTeacherIP, tfPath, tfPort;

    @FXML
    PasswordField pfPassword;

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
        btnLogOut.setDisable(true);

        // Text in Textfeld mittig setzen
        lbAlert.setTextAlignment(TextAlignment.CENTER);
        lbAlert.setAlignment(Pos.CENTER);
    }

    /**
     * sets an message on the screen of the student.
     *
     * @param error TRUE if it is an error-message and
     *              FALSE if it is a success-message.
     * @param msg   Specifies the message to show.
     */
    private void setMsg(boolean error, String msg) {
        String color = (error ? "red" : "limegreen");
        lbAlert.setText(msg);
        lbAlert.setStyle("-fx-background-color: " + color);
    }

    /**
     * connects the student with the teacher
     *
     * @param event Information from the click on the button.
     */
    public void logIn(ActionEvent event) {
        boolean ready = true;
        int port = 0;

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
        } else if (tfPath.getText().length() <= 0 || tfPath == null) {
            setMsg(true, "Arbeits-Ordner bitte auswählen");
            ready = false;
        } else if (tfPort.getText().length() <= 0) {
            setMsg(true, "Port bitte angeben");
            ready = false;
        } else {
            try {
                port = Integer.valueOf(tfPort.getText());
            } catch (Exception exc) {
                setMsg(true, "ungültiger Port!!");
                ready = false;
            }
        }

        try {
            if (!loggedIn && ready) {
                btnLogin.setDisable(true);
                btnLogOut.setDisable(false);
                client = new Client(new LoginPacket(
                        tfUsername.getText(),
                        pfPassword.getText(),
                        tfTeacherIP.getText(),
                        tfPath.getText(),
                        port
                ));
                //TeacherServer.PORT = port;
                setMsg(false, "Angemeldet");
                client.start();
                loggedIn = true;
            }
        } catch (AWTException | IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * shows a dialog-screen to choose the working-directory where
     * the project will be and saves the path of it.
     *
     * @param event Information from the click on the button.
     */
    public void chooseDirectory(ActionEvent event) {
        DirectoryChooser dc = new DirectoryChooser();
        dc.setInitialDirectory(new File(System.getProperty("user.home")));
        dc.setTitle("Wähle dein Ziel-Verzeichnis");
        File choosedFile = dc.showDialog(new Stage());
        if (choosedFile != null)
            tfPath.setText(String.format("%s/%s", choosedFile.getPath(), tfUsername.getText()));

    }

    /**
     * @param event
     */
    public void handIn(ActionEvent event) {
        if (client.handIn()) {
            lbAlert.setText("Datei gezippt abgegeben");
        } else {
            lbAlert.setText("hand in error");
        }
    }

    /**
     * disconnects from the teacher.
     *
     * @param actionEvent Information from the click on the button.
     */
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
