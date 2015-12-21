package at.htl.remotecontrol.gui.controller;

import at.htl.remotecontrol.entity.Interval;
import at.htl.remotecontrol.entity.Session;
import at.htl.remotecontrol.entity.Student;
import at.htl.remotecontrol.entity.StudentView;
import at.htl.remotecontrol.gui.Threader;
import at.htl.remotecontrol.server.TeacherServer;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.chart.LineChart;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.TextAlignment;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;
import java.net.URL;
import java.nio.charset.Charset;
import java.time.LocalDateTime;
import java.util.ResourceBundle;

/**
 * @timeline Text
 * 15.10.2015: PHI ???  Zeiteingabe für die Screenshot-Verzögerung durch Gui ermöglicht
 * 19.10.2015: PON ???  Liste der verbundenen Studenten
 * 24.10.2015: PON ???  DirectoryChooser für die Screenshots
 * 26.10.2015: PHI ???  Methode für Meldungen, starten und stoppen des Servers und Zeitauswahl(+random)
 * 29.11.2015: PHI ???  Angabe-Auswahl + Fehlermeldungen in GUI
 * 07.12.2015: PHI 030  Live-View und das LOC-Diagramm passt sich dem Fenster an
 * 07.12.2015: PHI 020  LineChart optimieren und benutzungsfähig machen
 * 17.12.2015: PON 040  importPupilList
 */
public class ControllerTeacher implements Initializable {

    @FXML
    public TextField tfTimeSS, tfPort, tfFileendings; //tfTimeSS -> Time-interval between screenshots

    @FXML
    public PasswordField tbPassword;

    @FXML
    public ListView<TextField> lvStudents;

    @FXML
    public Label lbAlert;

    @FXML
    public ToggleButton TB_SS_rnd;  //ToggleButton to see if 'random' is active

    @FXML
    public ImageView ivLiveView;    //shows the screenshot

    @FXML
    public Button btnStart, btnStop, btn;

    @FXML
    public AnchorPane apStudentDetail, apOption, spOption;

    @FXML
    public LineChart<Number, Number> loc;

    @FXML
    public SplitPane splitter;

    @FXML
    public CheckBox cbAngabe, cbHome;

    private Thread server;
    private Threader threader;

    public ControllerTeacher() {

    }

    public void initialize(URL location, ResourceBundle resources) {
        lvStudents.setItems(Session.getInstance().getObservableList());
        StudentView.getInstance().setIv(ivLiveView);
        StudentView.getInstance().setLv(lvStudents);

        // text in the middle of the textfield
        lbAlert.setTextAlignment(TextAlignment.CENTER);
        lbAlert.setAlignment(Pos.CENTER);

        setDynamicScreenSize();
        initializeLOC();

        Session.getInstance().setStartTime(LocalDateTime.now());
    }

    /**
     * if the screensize changes, the size of the image and chart changes too.
     */
    private void setDynamicScreenSize() {
        apStudentDetail.widthProperty().addListener((observable, oldValue, newValue) -> {
            ivLiveView.setFitWidth((double) newValue);
            loc.setPrefWidth((double) newValue);
        });
        apStudentDetail.heightProperty().addListener((observable, oldValue, newValue) -> {
            ivLiveView.setFitHeight((double) newValue - loc.getHeight() - 10);
        });
        spOption.widthProperty().addListener((observable, oldValue, newValue) -> {
            AnchorPane.setLeftAnchor(apOption, (double) newValue / 2 - apOption.getPrefWidth() / 2);
        });
        spOption.heightProperty().addListener((observable, oldValue, newValue) -> {
            AnchorPane.setTopAnchor(apOption, (double) newValue / 2 - apOption.getPrefHeight() / 2);
        });
        ivLiveView.setPreserveRatio(true);
        ivLiveView.setSmooth(true);
        ivLiveView.setCache(true);
    }

    /**
     * edits the chart and saves it in a singleton-class.
     */
    private void initializeLOC() {
        loc.getXAxis().setLabel("Seconds after START");
        loc.getYAxis().setLabel("Lines in File");

        Session.getInstance().setChart(loc);
    }

    /**
     * checks all fields on correctness.
     * starts the server for the students to connect.
     *
     * @param actionEvent Information from the click on the button.
     */
    public void startServer(ActionEvent actionEvent) {
        String path = Session.getInstance().getPathOfImages();
        File handOut = Session.getInstance().getHandOutFile();
        if (tfPort.getText().length() > 0) {
            try {
                TeacherServer.PORT = Integer.valueOf(tfPort.getText());
            } catch (Exception exc) {
                setMsg(true, "ungültiger Port!!");
            }
            if (TeacherServer.PORT > 0) {
                Session.getInstance().setPassword(tbPassword.getText());
                //Session.getInstance().setHandOutFile(new File(String.format("%s/angabe.zip",
                //        Session.getInstance().getPath())));
                String ssTime = tfTimeSS.getText();
                boolean isRnd = TB_SS_rnd.isSelected();

                if (path != null && (isRnd || (!isRnd && ssTime.length() > 0)) && handOut != null) {
                    if (isRnd) {
                        Session.getInstance().setInterval(new Interval(1000, 30000)); //  wert kleiner, gleich 0 bedeutet Random
                    } else {
                        Session.getInstance().setInterval(new Interval(Integer.parseInt(tfTimeSS.getText())));
                    }

                    String[] endings = tfFileendings.getText().split(";");
                    Session.getInstance().setEndings(endings);

                    threader = new Threader();
                    server = new Thread(threader);
                    server.start();
                    setMsg(false, "Server lauft");
                } else if (path == null) {
                    setMsg(true, "Bitte gib ein Verzeichnis an");
                } else if (!isRnd && ssTime.length() < 1) {
                    setMsg(true, "Bitte gib einen Zeitwert(in ms) an");
                } else if (handOut == null) {
                    setMsg(true, "Bitte eine Angabe auswählen!");
                }
            }
        } else {
            setMsg(true, "Bitte einen Port angeben");
        }
    }

    /**
     * stops the server.
     *
     * @param actionEvent Information from the click on the button.
     */
    public void stopServer(ActionEvent actionEvent) {
        if (server != null) {
            if (!server.isInterrupted()) {
                threader.stop();
                server.interrupt();
                setMsg(false, "Server gestoppt");
                cbAngabe.setSelected(false);
                cbHome.setSelected(false);
            } else {
                setMsg(true, "Server wurde bereits gestoppt");
            }
        } else {
            setMsg(true, "Server wurde noch nie gestartet");
        }
    }

    /**
     * sets an message on the screen of the teacher.
     *
     * @param error TRUE if it is an error-message and
     *              FALSE if it is a success-message.
     * @param msg   Specifies the message to show.
     */
    private void setMsg(boolean error, String msg) {
        String color = (error ? "red" : "limegreen");   //bei Fehlermeldung rot, sonst grün
        lbAlert.setText(msg);
        lbAlert.setStyle("-fx-background-color: " + color);
    }

    /**
     * shows a dialog-screen to choose the directory where the directorys of the
     * screenshots and the finished tests will be.
     *
     * @param event Information from the click on the button.
     */
    public void chooseDirectory(ActionEvent event) {
        DirectoryChooser dc = new DirectoryChooser();
        dc.setInitialDirectory(new File(System.getProperty("user.home")));
        dc.setTitle("Wähle dein Ziel-Verzeichnis");
        File choosedFile = dc.showDialog(new Stage());
        if (choosedFile != null) {
            Session.getInstance().setPath(choosedFile.getPath());
            cbHome.setSelected(true);
        } else {
            cbHome.setSelected(false);
        }
    }

    /**
     * shows a dialog-screen to choose the test-file.
     *
     * @param event Information from the click on the button.
     */
    public void chooseHandOutFile(ActionEvent event) {
        FileChooser dc = new FileChooser();
        dc.setInitialDirectory(new File(System.getProperty("user.home")));
        dc.setTitle("Wähle deine Angabe aus");
        File choosedFile = dc.showOpenDialog(new Stage());
        if (choosedFile != null) {
            Session.getInstance().setHandOutFile(new File(choosedFile.getPath()));
            cbAngabe.setSelected(true);
        } else {
            cbAngabe.setSelected(false);
        }
    }

    /**
     * disables the textfield of the time-interval if the button 'random' is clicked and
     * enables it if the 'random'-Button is OFF
     *
     * @param actionEvent Information from the click on the button
     */
    public void changeSomeOptions(ActionEvent actionEvent) {
        if (TB_SS_rnd.isSelected()) {
            tfTimeSS.setDisable(true);
            tfTimeSS.setEditable(false);
            TB_SS_rnd.setText("EIN");
        } else {
            tfTimeSS.setDisable(false);
            tfTimeSS.setEditable(true);
            TB_SS_rnd.setText("AUS");
        }
    }

    /**
     * Inports a file of pupils
     * sets the observable list
     *
     * @param actionEvent
     * @throws IOException
     */
    public void importPupilList(ActionEvent actionEvent) throws IOException {
        FileChooser dc = new FileChooser();
        dc.setInitialDirectory(new File(System.getProperty("user.home")));
        dc.setTitle("Wähle Die Schülerliste aus");
        /*FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter(".csv");
        dc.getExtensionFilters().add(filter);*/
        File choosedFile = dc.showOpenDialog(new Stage());

        BufferedReader bis = new BufferedReader(new InputStreamReader(
                new FileInputStream(choosedFile), Charset.forName("UTF-16")));

        int nameColumn = 0;
        String line;
        String[] words = bis.readLine().split(";");


        for (int i = 0; i < words.length; i++) {
            if (words[i].equals("Familienname")) {
                nameColumn = i;
            }
        }
        while ((line = bis.readLine()) != null) {
            Session.getInstance().addStudent(new Student(line.split(";")[nameColumn], null));
        }

    }
}
