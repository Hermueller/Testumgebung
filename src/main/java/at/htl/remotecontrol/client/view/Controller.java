package at.htl.remotecontrol.client.view;

import at.htl.remotecontrol.client.Client;
import at.htl.remotecontrol.common.MyUtils;
import at.htl.remotecontrol.common.fx.FxUtils;
import at.htl.remotecontrol.common.io.FileUtils;
import at.htl.remotecontrol.common.trasfer.LoginPackage;
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
import org.apache.logging.log4j.Level;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * @timeline Text
 * 15.10.2015: PON 001  created class
 * 18.10.2015: PHI 030  created login with text boxes
 * 14.10.2015: MET 030  new GUI design and created function setControls(value)
 * 24.10.2015: PON 005  added logout method
 * 30.10.2015: MET 010  expanded to login and logout
 * 31.10.2015: MET 020  creating Client packages and submitting them to client
 * 06.11.2015: PON 010  expansion to the password field
 * 19.11.2015: PON 015  GUI extended by the TextField "Port" and implements corresponding functions
 * 29.11.2015: PHI 040  display of messages in the GUI with setMsg()
 * 03.01.2016: MET 005  setMsg() improved by using FxUtils
 */
public class Controller implements Initializable {

    //region Controls
    @FXML
    TextField tfServerIP;
    @FXML
    TextField tfPort;
    @FXML
    TextField tfEnrolmentID;
    @FXML
    TextField tfCatalogNumber;
    @FXML
    TextField tfFirstName;
    @FXML
    TextField tfLastName;
    @FXML
    TextField tfPathOfProject;
    @FXML
    Button btnTestConnection;
    @FXML
    Button btnChooseDirectory;
    @FXML
    Button btnLogin;
    @FXML
    Button btnLogout;
    @FXML
    Label lbAlert;
    //endregion

    private Client client;
    private boolean loggedIn;

    public void initialize(URL location, ResourceBundle resources) {
        FileUtils.log(this, Level.ERROR, "initialize GUI 2");
        this.loggedIn = false;
        btnLogout.setDisable(true);

        // Text in Textfeld mittig setzen
        lbAlert.setTextAlignment(TextAlignment.CENTER);
        lbAlert.setAlignment(Pos.CENTER);
    }

    /**
     * connects the client with the teacher
     *
     * @param event Information from the click on the button.
     */
    public void logIn(ActionEvent event) {
        boolean ready = true;
        int port = 0;

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
                client = new Client(new LoginPackage(
                        tfUsername.getText(),
                        pfPassword.getText(),
                        tfTeacherIP.getText(),
                        tfPath.getText(),
                        port
                ));
                setMsg(false, "Angemeldet");
                client.start();
                loggedIn = true;
            }
        } catch (AWTException | IOException e) {
            FileUtils.log(this, Level.ERROR,
                    "Wurde nicht angemeldet überprüfen sie Username, Password, TeacherIP und den Pfad der Screenshots "
                            + MyUtils.exToStr(e));
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
        else
            FileUtils.log(this, Level.ERROR, "gewählter Ordner existiert nicht ");

    }

    /**
     * sends the finished test to the teacher.
     */
    public void handIn() {
        if (client.handIn()) {
            lbAlert.setText("Datei gezippt abgegeben");
        } else {
            lbAlert.setText("hand in error");
        }
    }

    /**
     * colors the logout-button to see easier if the finished test will
     * be sent on logout.
     *
     * @param event Information from the click on the ToggleButton.
     */
    public void handleSelect(ActionEvent event) {
        if (((ToggleButton) event.getSource()).isSelected()) {
            btnLogOut.setStyle("-fx-background-color: lawngreen");
        } else {
            btnLogOut.setStyle("-fx-background-color: crimson");
        }
    }

    /**
     * disconnects from the teacher.
     *
     * @param actionEvent Information from the click on the button.
     */
    public void logOut(ActionEvent actionEvent) {
        if (loggedIn) {
            client.stop();
            loggedIn = false;
            btnLogin.setDisable(false);
            btnLogOut.setDisable(true);
        }
        if (btnFinished.isSelected()) {
            handIn();
        }
        client.closeOut();
        setMsg(false, "Abgemeldet");
    }

    /**
     * Sets an message on the screen of the student.
     *
     * @param text  specifies the message to show
     * @param error TRUE   if it is an error-message
     *              FALSE  if it is a success-message
     */
    private void setMsg(String text, boolean error) {
        FxUtils.setMsg(lbAlert, text, error);
    }

}
