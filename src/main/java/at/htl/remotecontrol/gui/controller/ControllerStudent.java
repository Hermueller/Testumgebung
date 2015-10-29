package at.htl.remotecontrol.gui.controller;

import at.htl.remotecontrol.Client;
import at.htl.remotecontrol.entity.TestDirectoryCooser;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Philipp:  18.10.2015  Einfügen eines Login's mit Werteübergabe
 */
public class ControllerStudent implements Initializable {

    @FXML
    TextField tfUsername, tfTeacherIP;

    @FXML
    Button btnLogin;

    private Client client = null;
    private boolean canLogin = true;

    public ControllerStudent() {
    }

    public void initialize(URL location, ResourceBundle resources) {
    }

    public void logIn(ActionEvent event) {
        try {
            if (canLogin) {
                client = new Client(tfTeacherIP.getText(), tfUsername.getText());
                client.start();
                canLogin = false;
            }
        } catch (AWTException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void fileDirectory(ActionEvent event){
        DirectoryChooser dc = new DirectoryChooser();
        dc.setInitialDirectory(new File(System.getProperty("user.home")));
        dc.setTitle("Wähle dein Ziel-Verzeichnis");
        File choosedFile = dc.showDialog(new Stage());
        if (choosedFile != null)
            TestDirectoryCooser.getInstance().setFilePath(choosedFile.getPath());

        TestDirectoryCooser.getInstance().CreateDirectory();
    }

    public void logOut(ActionEvent actionEvent) {
        client.stop();
        canLogin = true;
    }

}
