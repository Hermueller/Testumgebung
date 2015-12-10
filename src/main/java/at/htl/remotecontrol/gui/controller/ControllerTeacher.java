package at.htl.remotecontrol.gui.controller;

import at.htl.remotecontrol.entity.Interval;
import at.htl.remotecontrol.entity.Session;
import at.htl.remotecontrol.entity.Student;
import at.htl.remotecontrol.entity.StudentView;
import at.htl.remotecontrol.gui.Threader;
import at.htl.remotecontrol.server.TeacherServer;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.TextAlignment;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.ResourceBundle;

/**
 * 15.10.2015:  Philipp     ??? Zeiteingabe für die Screenshot-Verzögerung durch Gui ermöglicht
 * 19.10.2015:  Patrick     ??? Liste der verbundenen Studenten
 * 24.10.2015:  Patrick     ??? DirectoryChooser für die Screenshots
 * 26.10.2015:  Philipp     ??? Methode für Meldungen, starten und stoppen des Servers und Zeitauswahl(+random)
 * 29.11.2015:  Philipp     ??? Angabe-Auswahl + Fehlermeldungen in GUI
 * 07.12.2015:  Philipp     031 Live-View und das LOC-Diagramm passt sich dem Fenster an
 * 07.12.2015:  Philipp     017 LineChart optimieren und benutzungsfähig machen
 */
public class ControllerTeacher implements Initializable {

    @FXML
    public TextField tfTimeSS, tfPort; // SS ... Screenshot

    @FXML
    public PasswordField tbPassword;

    @FXML
    public ListView<TextField> lvStudents;

    @FXML
    public Label lbAlert;

    @FXML
    public ToggleButton TB_SS_rnd;  //TB...ToggleButton   SS...Screenshot  rnd...Random

    @FXML
    public ImageView ivLiveView;

    @FXML
    public Button btnStart, btnStop, btn;

    @FXML
    public AnchorPane apStudentDetail;

    @FXML
    public LineChart<Number,Number> loc;

    @FXML
    public SplitPane splitter;

    private Thread server;
    private Threader threader;

    public ControllerTeacher() {

    }

    public void initialize(URL location, ResourceBundle resources) {
        lvStudents.setItems(Session.getInstance().getObservableList());
        StudentView.getInstance().setIv(ivLiveView);
        StudentView.getInstance().setLv(lvStudents);

        // Text in Textfeld mittig setzen
        lbAlert.setTextAlignment(TextAlignment.CENTER);
        lbAlert.setAlignment(Pos.CENTER);

        apStudentDetail.widthProperty().addListener((observable, oldValue, newValue) -> {
            ivLiveView.setFitWidth((double)newValue);
            loc.setPrefWidth((double)newValue);
        });
        apStudentDetail.heightProperty().addListener((observable, oldValue, newValue) -> {
            ivLiveView.setFitHeight((double)newValue - loc.getHeight() - 10);
        });
        ivLiveView.setPreserveRatio(true);
        ivLiveView.setSmooth(true);
        ivLiveView.setCache(true);

        initializeLOC();

        Session.getInstance().setStartTime(LocalDateTime.now());
    }

    private void initializeLOC() {
        //defining the axes
        loc.getXAxis().setLabel("Seconds after START");
        loc.getYAxis().setLabel("Lines in File");

        Session.getInstance().setChart(loc);
    }

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

    public void stopServer(ActionEvent actionEvent) {
        if (server != null) {
            if (!server.isInterrupted()) {
                threader.stop();
                server.interrupt();
                setMsg(false, "Server gestoppt");
            } else {
                setMsg(true, "Server wurde bereits gestoppt");
            }
        } else {
            setMsg(true, "Server wurde noch nie gestartet");
        }
    }

    private void setMsg(boolean error, String msg) {
        String color = (error ? "red" : "limegreen");   //bei Fehlermeldung rot, sonst grün
        lbAlert.setText(msg);
        lbAlert.setStyle("-fx-background-color: " + color);
    }

    public void chooseDirectory(ActionEvent actionEvent) {
        DirectoryChooser dc = new DirectoryChooser();
        dc.setInitialDirectory(new File(System.getProperty("user.home")));
        dc.setTitle("Wähle dein Ziel-Verzeichnis");
        File choosedFile = dc.showDialog(new Stage());
        if (choosedFile != null)
            Session.getInstance().setPath(choosedFile.getPath());
    }

    public void chooseHandOutFile(ActionEvent actionEvent) {
        FileChooser dc = new FileChooser();
        dc.setInitialDirectory(new File(System.getProperty("user.home")));
        dc.setTitle("Wähle deine Angabe aus");
        File choosedFile = dc.showOpenDialog(new Stage());
        if (choosedFile != null) {
            Session.getInstance().setHandOutFile(new File(choosedFile.getPath()));
        }
    }

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

    public void changeSuspicionColour(ActionEvent actionEvent) {
        System.out.println("INHALT: " + actionEvent.getSource());
    }
}
