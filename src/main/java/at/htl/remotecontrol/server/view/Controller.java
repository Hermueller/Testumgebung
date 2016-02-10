package at.htl.remotecontrol.server.view;

import at.htl.remotecontrol.common.io.FileUtils;
import at.htl.remotecontrol.common.entity.*;
import at.htl.remotecontrol.server.Threader;
import at.htl.remotecontrol.server.Server;
import at.htl.remotecontrol.server.entity.Interval;
import at.htl.remotecontrol.common.TimeSpinner;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Screen;
import javafx.stage.Stage;
import org.apache.logging.log4j.Level;

import java.io.*;
import java.net.InetAddress;
import java.net.URL;
import java.net.UnknownHostException;
import java.nio.charset.Charset;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

/**
 * @timeline Text
 * 15.10.2015: PHI 035  Zeiteingabe für die Screenshot-Verzögerung durch Gui ermöglicht
 * 19.10.2015: PON 020  list of connected pupils
 * 24.10.2015: PON 020  teachers can now select the folder where the screenshots are saved
 * 26.10.2015: PHI 050  Methode für Meldungen, starten und stoppen des Servers und Zeitauswahl(+random)
 * 05.11.2015: PON 015  implemented selecting of specification file
 * 06.11.2015: PON 002  expansion to the password field
 * 29.11.2015: PHI 025  Angabe-Auswahl + Fehlermeldungen in GUI
 * 07.12.2015: PHI 030  Live-View und das LOC-Diagramm passt sich dem Fenster an
 * 07.12.2015: PHI 020  LineChart optimieren und benutzungsfähig machen
 * 17.12.2015: PON 120  function "importPupilList" for importing student lists
 * 17.12.2015: PON 005  Bug found: exception Handling missing, registration of pupils
 * 31.12.2015: PHI 010  LineChart überarbeitet, sodass bei der Änderung der ListView-Selection sich auch das Diagramm ändert.
 * 01.01.2016: PHI 010  Fehler in der LineChart verbessert.
 * 06.01.2016: PHI 025  Überarbeitung der Fehler beim Wechsel von der LineChart von einem Schüler zum Anderen.
 * 15.01.2016: PHI 060  Check-Bild bei Error und Erfolg beim Starten des Servers eingefügt.
 * 20.01.2016: PHI 040  Simple- und Advanced-Mode eingefügt. / Zeit wird nun in Sekunden eingegeben.
 * 23.01.2016: PHI 020  Tooltip und Version eingeführt.
 * 24.01.2016: PHI 035  Zeigt den Screenshot im Fullscreen beim Klick und verschwindet beim erneuten Klick. +RandomTimeBugFix
 */
public class Controller implements Initializable {

    @FXML
    public TextField tfTimeSS, tfPort, tfFileendings, tfMyIP_Address;



    @FXML
    public ListView<Button> lvStudents;

    @FXML
    public Label lbAlert, lbAngabe, lbPath, lbVersion1, lbVersion2, lbVersion3;

    @FXML
    public ToggleButton TB_SS_rnd;

    @FXML
    public ImageView ivLiveView;

    @FXML
    public Button btnStart, btnStop, btn, btnChange;

    @FXML
    public AnchorPane apStudentDetail, apOption, apSimple, spOption;

    @FXML
    public LineChart<Number, Number> loc;

    @FXML
    public SplitPane splitter;

    @FXML
    public AnchorPane abgabePane;

    @FXML
    public ImageView ivPort, ivAngabe, ivPath, ivTime, ivEnding;

    @FXML
    public AnchorPane aptime;

    @FXML
    public Button btnaddtime;

    private Thread server;
    private Threader threader;

    public Controller() {

    }

    /**
     * LOAD standard values.
     * @param location
     * @param resources
     */
    public void initialize(URL location, ResourceBundle resources) {
        lvStudents.setItems(Session.getInstance().getObservableList());
        StudentView.getInstance().setIv(ivLiveView);
        StudentView.getInstance().setLv(lvStudents);

        setDynamicScreenSize();
        setVersionAnchor();
        setOnChangeSize();
        setFitHeight();
        setImageClick();
        initializeLOC();
        initializeSLOMM();
        showIP_Address();
        TimeSpinner spinner = new TimeSpinner();
        final LocalTime[] time = new LocalTime[1];



        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("hh:mm:ss a");
        spinner.valueProperty().addListener((obs, oldTime, newTime) ->
                time[0] = doSomething(newTime, false));

        System.out.println("LOCAL TIME  "+time[0]);

        btnaddtime.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                doSomething(time[0], true);
            }
        });



        aptime.getChildren().add(spinner);

        btnStart.setDisable(false);
        btnStop.setDisable(true);
        Session.getInstance().setStartTime(LocalDateTime.now());
    }
    private LocalTime doSomething(LocalTime newTime, boolean addtime ){
        System.out.println(newTime);
        if(addtime){
            newTime.plusMinutes(10);
            System.out.println("NEW TIME "+newTime);
        }

        if(LocalTime.now() == newTime){
            System.out.println("ABGABE");
            //directory.zip(Session.getInstance().getPath());
        }
        return newTime;
    }

    //region initialize
    /**
     * show the version number always in the bottom right corner.
     */
    private void setVersionAnchor() {
        AnchorPane.setBottomAnchor(lbVersion1, 10.0);
        AnchorPane.setRightAnchor(lbVersion1, 10.0);
        AnchorPane.setBottomAnchor(lbVersion2, 10.0);
        AnchorPane.setRightAnchor(lbVersion2, 10.0);
        AnchorPane.setBottomAnchor(lbVersion3, 10.0);
        AnchorPane.setRightAnchor(lbVersion3, 10.0);
    }

    /**
     * show screenshot in fullscreen on click.
     */
    private void setImageClick() {
        ivLiveView.setOnMouseClicked(event -> {
            Stage stage = new Stage();
            ImageView iv = new ImageView(ivLiveView.getImage());
            AnchorPane root = new AnchorPane(iv);
            Scene scene = new Scene(root, Screen.getPrimary().getBounds().getWidth(), Screen.getPrimary().getBounds().getHeight());
            stage.setScene(scene);
            AnchorPane.setLeftAnchor(iv, (Screen.getPrimary().getBounds().getWidth() - iv.getImage().getWidth())/2);
            AnchorPane.setTopAnchor(iv, (Screen.getPrimary().getBounds().getHeight() - iv.getImage().getHeight())/2);
            iv.setOnMouseClicked(event1 -> stage.close());
            stage.show();
        });
    }

    /**
     * show the path as a tooltip.
     */
    private void initializeSLOMM() { //SLOMM . . . Show Label On Mouse Move
        Tooltip mousePositionToolTip = new Tooltip("");
        lbPath.setOnMouseMoved(event -> {
            String msg = Session.getInstance().getPath();
            if (msg != null) {
                mousePositionToolTip.setText(msg);

                Node node = (Node) event.getSource();
                mousePositionToolTip.show(node, event.getScreenX() + 50, event.getScreenY());
            }
        });
        lbAngabe.setOnMouseMoved(event -> {
            File file = Session.getInstance().getHandOutFile();
            if (file != null) {
                mousePositionToolTip.setText(file.getPath());

                Node node = (Node) event.getSource();
                mousePositionToolTip.show(node, event.getScreenX() + 50, event.getScreenY());
            }
        });
        lbPath.setOnMouseExited(event -> mousePositionToolTip.hide());
        lbAngabe.setOnMouseExited(event -> mousePositionToolTip.hide());
    }

    /**
     * if the SelectedStudent changes, the Chart and ImageView
     * is cleared.
     */
    private void setOnChangeSize() {
        lvStudents.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            //   CHANGE LINECHART
            loc.getData().clear();

            Student st = Session.getInstance().findStudentByName(newValue.getText());
            if (st.getSeries() != null) {
                for (XYChart.Series<Number, Number> actualSeries : st.getSeries()) {
                    loc.getData().add(actualSeries);
                }
            }

            //   CHANGE SCREENSHOT
            ivLiveView.setImage(new Image("images/loading.gif"));
        });
    }

    /**
     * sets the size of the images, which are shown after starting/stopping the server
     */
    private void setFitHeight() {
        ivPort.setFitHeight(25);
        ivAngabe.setFitHeight(25);
        ivPath.setFitHeight(25);
        ivTime.setFitHeight(25);
        ivEnding.setFitHeight(25);
    }

    /**
     * if the screensize changes, the size of the image and chart changes too.
     */
    private void setDynamicScreenSize() {
        apStudentDetail.widthProperty().addListener((observable, oldValue, newValue) -> {
            ivLiveView.setFitWidth((double) newValue - 10);
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
        spOption.widthProperty().addListener((observable, oldValue, newValue) -> {
            AnchorPane.setLeftAnchor(apSimple, (double) newValue / 2 - apSimple.getPrefWidth() / 2);
        });
        spOption.heightProperty().addListener((observable, oldValue, newValue) -> {
            AnchorPane.setTopAnchor(apSimple, (double) newValue / 2 - apSimple.getPrefHeight() / 2);
        });
        ivLiveView.setPreserveRatio(true);
        ivLiveView.setSmooth(true);
        ivLiveView.setCache(true);
    }

    /**
     * edits the chart and saves it in a singleton-class.
     */
    private void initializeLOC() {
        Session.getInstance().setChart(loc);

        loc.setCursor(Cursor.CROSSHAIR);
    }

    /**
     * shows the IP-Address of the Teacher.
     */
    public void showIP_Address() {
        String ip = "";
        try {
            ip = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {

            FileUtils.log(this, Level.ERROR,"Keine IP Adresse gefunden "+MyUtils.convert(e));
        }
        tfMyIP_Address.setText(ip);
    }
    //endregion

    /**
     * checks all fields on correctness.
     * starts the server for the students to connect.
     *
     * @param actionEvent Information from the click on the button.
     */
    public void startServer(ActionEvent actionEvent) {
        String path = Session.getInstance().getPathOfImages();
        File handOut = Session.getInstance().getHandOutFile();
        String ssTime = tfTimeSS.getText();
        boolean isRnd = TB_SS_rnd.isSelected();
        boolean startable = true;

        if (!ssTime.matches("[0-9]+") && !isRnd) {
            setMsg(true, "Zeit darf nur Zahlen enthalten!!");
            setImage(ivTime, false);
            startable = false;
        } if (!isRnd && ssTime.length() < 1) {
            setMsg(true, "Bitte gib einen Zeitwert(in Sek.) an");
            setImage(ivTime, false);
            startable = false;
        } if (path == null) {
            setMsg(true, "Bitte gib ein Verzeichnis an");
            setImage(ivPath, false);
            startable = false;
        } if (handOut == null) {
            setMsg(true, "Bitte eine Angabe auswählen!");
            setImage(ivAngabe, false);
            startable = false;
        } if (!tfPort.getText().matches("[0-9]+") && tfPort.getText().length() != 0) {
            setMsg(true, "ungültiger Port!!");
            setImage(ivPort, false);
            startable = false;
        }

        if (startable) {
            if (tfPort.getText().matches("[0-9]+")) {
                Server.PORT = Integer.valueOf(tfPort.getText());
            }
            String ending = tfFileendings.getText();
            if (ending.length() == 0) {
                ending = "*.java; *.fxml; *.cs; *.xhtml; *.html";
            }

            if (isRnd) {
                Session.getInstance().setInterval(new Interval(1, 30));
            } else {
                Session.getInstance().setInterval(new Interval(Integer.parseInt(ssTime)));
            }

            String[] endings = ending.split(";");
            Session.getInstance().setEndings(endings);

            threader = new Threader();
            server = new Thread(threader);
            server.start();
            btnStart.setDisable(true);
            btnStop.setDisable(false);
            setMsg(false, "Server lauft");
            setImage(ivPort, true);
            setImage(ivAngabe, true);
            setImage(ivPath, true);
            setImage(ivTime, true);
            setImage(ivEnding, true);
            btnChange.setDisable(true);
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
                setMsg(true, "Server gestoppt");
                btnStart.setDisable(false);
                btnStop.setDisable(true);
                ivAngabe.setImage(null);
                ivTime.setImage(null);
                ivEnding.setImage(null);
                ivPort.setImage(null);
                ivPath.setImage(null);
                btnChange.setDisable(false);
            } else {
                setMsg(true, "Server wurde bereits gestoppt");
            }
        } else {
            setMsg(true, "Server wurde noch nie gestartet");
        }
    }

    /**
     * switching from Simple/Advanced-Mode to the other mode.
     *
     * @param event
     */
    public void changeMode(ActionEvent event) {
        apOption.setVisible(!apOption.isVisible());
        apSimple.setVisible(!apSimple.isVisible());
        if (apOption.isVisible()) {
            btnChange.setText("Simple Mode");
        } else {
            btnChange.setText("Advanced Mode");
        }
    }

    /**
     * Shows a green picture if the values were correct from the user
     * and shows a red picture if the userinput was wrong.
     * @param element   The Place where the Image will show
     * @param correct   Was the userinput correct?
     */
    private void setImage(ImageView element, boolean correct) {
        if (correct) {
            element.setImage(new Image("images/checked.png"));
        }
        else {
            element.setImage(new Image("images/unchecked.png"));
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
     * shows a dialog-screen to choose the directory where the directories of the
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
            //cbHome.setSelected(true);
        } /*else {
            cbHome.setSelected(false);
        }*/
    }

    /**
     * shows a dialog-screen to choose the test-file.
     * only zip-files are allowed.
     *
     * @param event Information from the click on the button.
     */
    public void chooseHandOutFile(ActionEvent event) {
        // Create and show the file filter
        FileChooser fc = new FileChooser();
        fc.setSelectedExtensionFilter(new FileChooser.ExtensionFilter("ZIP files (*.zip)", "*.zip"));
        File yourZip = fc.showOpenDialog(new Stage());

        // Check the user pressed OK, and not Cancel.
        if (yourZip != null) {
            Session.getInstance().setHandOutFile(yourZip);
            //cbAngabe.setSelected(true);
        } /*else {
            cbAngabe.setSelected(false);
        }*/
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
     * Imports a file of pupils
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

        if (choosedFile != null) {
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
}
