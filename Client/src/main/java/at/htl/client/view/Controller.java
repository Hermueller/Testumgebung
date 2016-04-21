package at.htl.client.view;

import at.htl.client.Client;
import at.htl.client.Exam;
import at.htl.common.Countdown;
import at.htl.common.MyUtils;
import at.htl.common.Pupil;
import at.htl.common.fx.FxUtils;
import at.htl.common.io.FileUtils;
import at.htl.common.trasfer.LoginPackage;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import org.apache.logging.log4j.Level;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.time.LocalTime;
import java.util.ResourceBundle;

/**
 * @timeline .
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
 * 12.02.2016: MET 005  activation and deactivation of controls depending on login and logout
 * 12.02.2016: MET 030  display of messages in the GUI with setMsg() enormously improved
 * 25.02.2016: MET 005  default settings for testing
 * 20.03.2016: PHI 001  fixed bug (check ip-address on correctness)
 */
public class Controller implements Initializable {

    //region Controls
    @FXML
    private TextField tfServerIP;
    @FXML
    private TextField tfPort;
    @FXML
    private TextField tfEnrolmentID;
    @FXML
    private TextField tfCatalogNumber;
    @FXML
    private TextField tfFirstName;
    @FXML
    private TextField tfLastName;
    @FXML
    private TextField tfPathOfProject;
    @FXML
    private Button btnTestConnection;
    @FXML
    private CheckBox cbNoLogin;
    @FXML
    private Button btnChooseDirectory;
    @FXML
    private Button btnLogin;
    @FXML
    private Button btnLogout;
    @FXML
    private Label lbAlert;
    @FXML
    private Label lbTimeLeft;
    @FXML
    private Text txTimeLeft;
    //endregion

    private Client client;
    private Countdown countdown;

    public void initialize(URL location, ResourceBundle resources) {
        setControls(true);
    }

    @FXML
    public void defaultSettings() {
        tfServerIP.setText("localhost");
        tfPort.setText("5555");
        tfEnrolmentID.setText("max");
        tfCatalogNumber.setText("99");
        tfFirstName.setText("Max");
        tfLastName.setText("Mustermann");
        tfPathOfProject.setText("/local/testC");
    }

    @FXML
    public void testConnection() {
        String ip = tfServerIP.getText();
        runSystemCommand("ping -c 2 ", ip);
    }

    private void runSystemCommand(String command, String ip) {
        command += ip;
        try {
            Process p = Runtime.getRuntime().exec(command);
            BufferedReader inputStream = new BufferedReader(
                    new InputStreamReader(p.getInputStream()));

            String s = "";
            double lossPercentage = -1;
            boolean connected = false;
            String msg = "";

            while ((s = inputStream.readLine()) != null) {
                if (s.startsWith("---")) {
                    break;
                }
            }
            s = inputStream.readLine();
            if (s != null) {
                String loss = s.split(",")[2];
                lossPercentage = Double.parseDouble(loss.split("%")[0].trim());
            }
            if (lossPercentage > 0) {
                msg = lossPercentage + "% received";
            } else if (lossPercentage < 0) {
                msg = "can't ping the following IP: " + ip;
            } else {
                msg = "IP pinged successfully!!";
                connected = true;
            }

            FxUtils.showPopUp(msg, connected);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Shows a dialog-screen to choose the working-directory where
     * the project will be and saves the path of it.
     */
    @FXML
    public void chooseProjectDirectory() {
        tfPathOfProject.setText(
                FxUtils.chooseDirectory("Select Project Directory", null).getPath());
    }

    /**
     * Connects the client with the server.
     */
    @FXML
    public void login() {
        setMsg("", false);
        if (setExam()) {
            FileUtils.log(this, Level.INFO, "Try to login");
            setMsg("Try to login ...", false);
            try {
                if (isLoggedOut()) {
                    if (!cbNoLogin.isSelected()) {
                        client = new Client(new LoginPackage(
                                Exam.getInstance().getPupil().getFirstName(),
                                Exam.getInstance().getPupil().getLastName(),
                                Exam.getInstance().getPupil().getCatalogNumber(),
                                Exam.getInstance().getPupil().getEnrolmentID(),
                                Exam.getInstance().getServerIP(),
                                Exam.getInstance().getPupil().getPathOfProject(),
                                Exam.getInstance().getPort()
                        ));
                        client.start();
                    }
                    setTimeLeft();
                    setControls(false);
                    setMsg("Signed in!", false);
                }
            } catch (Exception e) {
                FileUtils.log(Level.ERROR, e.getMessage());
                setMsg("Login failed!", true);
            }
        }
    }

    private void setTimeLeft() {
        LocalTime toTime = LocalTime.now().plusMinutes(0).plusSeconds(10);
        countdown = new Countdown(txTimeLeft, toTime);
        countdown.setDaemon(true);
        countdown.start();
    }

    public boolean isLoggedIn() {
        return btnLogin.isDisable();
    }

    public boolean isLoggedOut() {
        return btnLogout.isDisable();
    }

    /**
     * Disconnects from the server.
     */
    @FXML
    public void logout() {
        setControls(true);
        setMsg("Test successfully submitted", false);
        client.stop();
    }

    /**
     * Enables and disables controls depending on login and logout
     *
     * @param value enable controls?
     */
    public void setControls(boolean value) {
        tfServerIP.setEditable(value);
        tfPort.setEditable(value);
        tfEnrolmentID.setEditable(value);
        tfCatalogNumber.setEditable(value);
        tfFirstName.setEditable(value);
        tfLastName.setEditable(value);
        tfPathOfProject.setEditable(value);
        btnChooseDirectory.setDisable(!value);
        btnLogin.setDisable(!value);
        btnLogout.setDisable(value);
        lbTimeLeft.setVisible(!value);
        txTimeLeft.setVisible(!value);
    }

    /**
     * Saves the valid values in the repository "Exam"
     *
     * @return successful
     */
    public boolean setExam() {
        if (validForm()) {
            Exam.getInstance().setServerIP(tfServerIP.getText());
            Exam.getInstance().setPort(Integer.valueOf(tfPort.getText()));
            Exam.getInstance().setPupil(new Pupil(
                    Integer.valueOf(tfCatalogNumber.getText()),
                    tfEnrolmentID.getText(),
                    tfFirstName.getText(),
                    tfLastName.getText(),
                    tfPathOfProject.getText())
            );
            return true;
        }
        return false;
    }

    /**
     * Checks whether the user has entered valid values
     *
     * @return is form valid?
     */
    public boolean validForm() {
        String serverIP = tfServerIP.getText();
        int port = MyUtils.strToInt(tfPort.getText());
        String enrolmentID = tfEnrolmentID.getText();
        int catalogNumber = MyUtils.strToInt(tfCatalogNumber.getText());
        String firstName = tfFirstName.getText();
        String lastName = tfLastName.getText();
        String pathOfProject = tfPathOfProject.getText();
        boolean validity = false;
        if (serverIP.isEmpty()) {
            setMsg("Specify the IP-Address of the server!", true);
        } else if ((serverIP.split("\\.").length != 4 && !serverIP.equals("localhost"))
                || serverIP.length() > 15) {
            setMsg("Invalid IP-Address!", true);
        } else if (port < 1) {
            setMsg("Invalid Port!", true);
        } else if (enrolmentID.isEmpty()) {
            setMsg("Enter your enrolment id", true);
        } else if (enrolmentID.length() >= 10) {
            setMsg("The enrolment id is too long!", true);
        } else if (catalogNumber < 1) {
            setMsg("Invalid catalog number!", true);
        } else if (firstName.isEmpty() || firstName.length() > 20) {
            setMsg("Enter your correct first name", true);
        } else if (lastName.isEmpty() || lastName.length() > 20) {
            setMsg("Enter your correct last name", true);
        } else if (pathOfProject.isEmpty()) {
            setMsg("Specify the path of project!", true);
        } else {
            validity = true;
        }
        return validity;
    }

    /**
     * colors the logout-button to see easier if the finished test will
     * be sent on logout.
     *
     * @param event Information from the click on the ToggleButton.
     */
    /*public void handleSelect(ActionEvent event) {
        if (((ToggleButton) event.getSource()).isSelected()) {
            btnLogOut.setStyle("-fx-background-color: lawngreen");
        } else {
            btnLogOut.setStyle("-fx-background-color: crimson");
        }
    }
    */


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
