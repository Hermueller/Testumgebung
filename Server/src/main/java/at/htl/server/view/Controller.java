package at.htl.server.view;

import at.htl.common.MyUtils;
import at.htl.common.Student;
import at.htl.common.TimeSpinner;
import at.htl.common.fx.FxUtils;
import at.htl.common.fx.StudentView;
import at.htl.common.io.FileUtils;
import at.htl.server.Settings;
import at.htl.server.Threader;
import at.htl.server.entity.Interval;
import at.htl.server.Server;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.*;
import org.apache.logging.log4j.Level;

import javax.imageio.ImageIO;
import javax.swing.filechooser.FileSystemView;
import java.io.*;
import java.net.InetAddress;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.UnknownHostException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.jar.Attributes;
import java.util.jar.JarEntry;
import java.util.jar.JarOutputStream;
import java.util.jar.Manifest;

/**
 * @timeline Text
 * 15.10.2015: GNA 001  created class
 * 15.10.2015: PHI 035  Allowed time-input in the GUI for the screenshot-delay.
 * 19.10.2015: PON 020  list of connected pupils
 * 24.10.2015: PON 020  teachers can now select the folder where the screenshots are saved
 * 26.10.2015: PHI 050  created method for the messages from the start and stop of the server (+random time).
 * 05.11.2015: PON 015  implemented selecting of specification file
 * 06.11.2015: PON 002  expansion to the password field
 * 12.11.2015: PON 002  save password in the repository
 * 29.11.2015: PHI 025  Handout + shows error messages in the GUI.
 * 07.12.2015: PHI 030  Live-View and LOC-Chart will always be the size of the window.
 * 07.12.2015: PHI 020  changed at.htl.client.styles of the LineChart.
 * 17.12.2015: PON 120  function "importPupilList" for importing student lists
 * 17.12.2015: PON 005  Bug found: exception Handling missing, registration of pupils
 * 30.12.2015: GNA 100  TimeSpinner Abagabe hinzugefügt
 * 31.12.2015: PHI 010  LineChart revised (if the student from the list changes -> the LineChart changes too).
 * 01.01.2016: PHI 010  fixed bug in the LineChart.
 * 03.01.2016: GNA 120  Add time to Abgabe
 * 06.01.2016: PHI 025  BugFix (LineChart will not be influenced when the student changes)
 * 15.01.2016: PHI 060  Shows check-pictures for errors and success when the server is started.
 * 20.01.2016: PHI 040  Simple- and Advanced-Mode created. / input time now in seconds
 * 22.01.2016: GNA 030  added Startspinner
 * 23.01.2016: PHI 020  Tooltip and Version created.
 * 24.01.2016: PHI 035  Shows the screenshot in fullscreen on click and closes on another click. +RandomTimeBugFix
 * 10.02.2016: GNA 120  Send Start and Abgabe Zeit for automatic Abgabe
 * 10.03.2016: PHI 120  BugFix (Screenshot, Lines-of-Code Chart)
 * 11.03.2016: GNA 030  BugFix to send time data
 * 12.03.2016: PHI 125  show last screenshot of the student if the selection changed (no waitTime)
 * 15.03.2016: PHI 030  show the log on the application and check the portnumber
 * 18.03.2016: PHI 005  export the log to an text-file
 * 21.03.2016: PHI 055  fixed bug in the screenshot-View  &&   created pop-up window for the log-export
 * 22.03.2016: PHI 225  create JAR with button; create properties; fixed minor bug in the view
 * 23.03.2016: PHI 145  added a new Tab called Student-Info (shows Student infos) && changed language+View of the application
 * 24.03.2016: PHI 105  created the LineChart-Export
 * 04.04.2016: GNA 045  Added test data
 * 14.04.2016: GNA 050  Testdata for fast mode
 */
public class Controller implements Initializable {

    //region Tab: Option Variables
    @FXML
    private AnchorPane spOption;
    @FXML
    private AnchorPane apOption;
    @FXML
    private AnchorPane apSimple;
    @FXML
    private Button btnChange;
    @FXML
    private TextField tfPort;
    @FXML
    private ImageView ivPort;
    @FXML
    private Label lbAngabe;
    @FXML
    private Button btnAngabe;
    @FXML
    private ImageView ivAngabe;
    @FXML
    private Label lbPath;
    @FXML
    private ImageView ivPath;
    @FXML
    private ToggleButton TB_SS_rnd;
    @FXML
    private TextField tfTimeSS;
    @FXML
    private ImageView ivTime;
    @FXML
    private TextField tfFileExtensions;
    @FXML
    private ImageView ivFileExtensions;
    @FXML
    private Label lbAlert;
    @FXML
    private Button btnStart;
    @FXML
    private Button btnStop;
    @FXML
    private Label lbVersion;
    //endregion

    //region Student-Details Variables
    @FXML
    public AnchorPane apStudentDetail;
    @FXML
    private LineChart<Number, Number> loc;
    @FXML
    private ImageView ivLiveView;
    //endregion

    //region HandIn Variables
    @FXML
    private AnchorPane apHandIn;
    @FXML
    private TextField tfMyIP_Address;
    @FXML
    private AnchorPane apTime;
    @FXML
    private Button btnaddTime;
    @FXML
    private Button btnabgabe;
    @FXML
    private AnchorPane apstarttime,aptime;
    //endregion

    //region Log Variables
    @FXML
    private ScrollPane scrollLog;
    @FXML
    private AnchorPane anchorPaneScrollLog;
    //endregion

    //region Student-Info Variables
    @FXML
    private Label lbFirstName, lbLastName, lbEnrolmentID, lbCatalogNumber, lbLoC, lbFaceNumber;
    //endregion

    //region other Variables
    @FXML
    private BorderPane bpDataView;
    @FXML
    private AnchorPane apInfo;
    @FXML
    private Label lbDate;
    @FXML
    public SplitPane splitter;
    @FXML
    private ListView<Button> lvStudents;


    private Thread server;
    private Threader threader;
    //endregion

    public Controller() {

    }

    //region Export-Methods

    /**
     * creates an image from the LineChart of a specific student and saves it.
     *
     * @param event     of the click on the button
     */
    public void exportLOC(ActionEvent event) {
        WritableImage image = loc.snapshot(new SnapshotParameters(), null);

        String studentInfo = lbFaceNumber.getText() + "-" + lbLastName.getText() + "-LineChart.png";

        File file = new File(Settings.getInstance().getPathOfExports().concat("/" + studentInfo));

        try {
            ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", file);
        } catch (IOException e) {
            FileUtils.log(Level.ERROR, e.getMessage() + " ;; " + e.getLocalizedMessage());
        }

        FxUtils.showPopUp("exported LineChart successfully !!", true);
    }

    /**
     * writes the Log into a TEXT-file.
     * <br /><br />
     * find the issue on GitHub:<p>
     * https://github.com/BeatingAngel/Testumgebung/issues/26
     *
     * @param actionEvent   event of the click on the button
     *
     * @since   1.11.33.051
     */
    public void exportLog(ActionEvent actionEvent) {
        try {
            List<String> list = new LinkedList<>();
            ObservableList<Node> nodes = ((VBox)Settings.getInstance().getLogArea().getChildren().get(0)).getChildren();
            for (Node node : nodes) {
                TextField tf = (TextField)node;
                list.add(tf.getText());
            }

            Path file = Paths.get(Settings.getInstance().getPathOfExports().concat("/log.txt"));
            Files.write(file, list, Charset.forName("UTF-8"));

            FxUtils.showPopUp("exported log successfully!!", true);
        } catch (IOException e) {
            FileUtils.log(Level.ERROR, e.getMessage());
        }
    }

    //endregion

    //region create and read properties

    /**
     * create pop-up window to ask for the version number
     * and create a properties-file for it and creates a JAR-file
     * for the student and the teacher.
     * <br /><br />
     * find the issue on GitHub:<p>
     * https://github.com/BeatingAngel/Testumgebung/issues/23
     *
     * @param actionEvent   event from the click on the button
     *
     * @since 1.12.35.071
     */
    public void fromVersion(ActionEvent actionEvent) {
        final Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);

        Label label = new Label("Version: ");
        TextField tf = new TextField();
        Button btn = new Button("   CREATE   ");
        btn.setOnAction(event -> {
            createProp(tf.getText());
            createJar();
            stage.close();
        });

        AnchorPane ap = new AnchorPane();
        HBox hbox = new HBox(10, label, tf);
        VBox vBox = new VBox(20, hbox, btn);
        vBox.setLayoutX(30);
        vBox.setLayoutY(30);
        ap.getChildren().add(vBox);

        Scene dialogScene = new Scene(ap, 300, 200);
        stage.setScene(dialogScene);
        stage.show();
    }

    /**
     * creates the properties-file
     * <br /><br />
     * The properties include:
     * <ul>
     *     <li>Date from the creation of the JAR-file (current date)</li>
     *     <li>Version of the application</li>
     * </ul>
     *
     * @param version   Specifies the version of the application
     */
    public void createProp(String version) {
        Properties prop = new Properties();
        OutputStream output = null;

        try {

            output = new FileOutputStream("src/main/resources/config.properties");
            prop.setProperty("version", version);
            prop.setProperty("date", LocalDate.now().toString());
            prop.store(output, null);


            output = new FileOutputStream("target/classes/config.properties");
            prop.setProperty("version", version);
            prop.setProperty("date", LocalDate.now().toString());
            prop.store(output, null);

        } catch (IOException io) {
            FileUtils.log(Level.ERROR, io.getMessage());
        } finally {
            if (output != null) {
                try {
                    output.close();
                } catch (IOException e) {
                    FileUtils.log(Level.ERROR, e.getMessage());
                }
            }
        }
    }

    /**
     * writes the data from the properties-file into the Labels in the application
     * <br /><br />
     * The properties include:
     * <ul>
     *     <li>Date from the creation of the JAR-file</li>
     *     <li>Version of the application</li>
     * </ul>
     */
    public void readProperties() {
        Properties prop = new Properties();
        String propFileName = "config.properties";
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(propFileName);
        try {
            prop.load(inputStream);
        } catch (IOException e) {
            FileUtils.log(Level.ERROR, e.getMessage());
        }

        lbVersion.setText(prop.getProperty("version"));
        lbDate.setText("created on: " + prop.getProperty("date"));
    }

    //endregion

    //region create JAR-files

    /**
     * creates a jar-file for the student and teacher
     * <br /><br />
     * find the issue on GitHub:<p>
     * https://github.com/BeatingAngel/Testumgebung/issues/23
     *
     * @since 1.12.35.071
     */
    public void createJar()
    {
        try {
            // creating student JAR
            Manifest manifest = new Manifest();
            manifest.getMainAttributes().put(
                    Attributes.Name.MANIFEST_VERSION, "1.0");
            manifest.getMainAttributes().put(
                    Attributes.Name.MAIN_CLASS, "at.htl.client.view.StudentGui");
            JarOutputStream target = new JarOutputStream(
                    new FileOutputStream(
                            Settings.getInstance().getPathOfExports().concat("/student.jar")
                    ), manifest);
            File source = new File("target/classes/");
            add(source, target);
            target.close();

            //creating teacher JAR
            manifest = new Manifest();
            manifest.getMainAttributes().put(
                    Attributes.Name.MANIFEST_VERSION, "1.0");
            manifest.getMainAttributes().put(
                    Attributes.Name.MAIN_CLASS, "TeacherGui");
            target = new JarOutputStream(
                    new FileOutputStream(
                            Settings.getInstance().getPathOfExports().concat("/teacher.jar")
                    ), manifest);
            source = new File("target/classes/");
            add(source, target);
            target.close();
        } catch (IOException exp) {
            FileUtils.log(Level.ERROR, exp.getMessage());
        }

        FxUtils.showPopUp("created JAR-file successfully!!", true);
    }

    /**
     * add files from the source to the jar file
     *
     * @param source        the source of the files to add
     * @param target        the stream from the jar-file
     * @throws IOException
     */
    private void add(File source, JarOutputStream target) throws IOException
    {
        BufferedInputStream in = null;
        try
        {
            if (source.isDirectory())
            {
                String name = source.getPath().replace("\\", "/").replace("target/classes/", "");
                if (!name.isEmpty())
                {
                    if (!name.endsWith("/"))
                        name += "/";
                    JarEntry entry = new JarEntry(name);
                    entry.setTime(source.lastModified());
                    target.putNextEntry(entry);
                    target.closeEntry();
                }
                for (File nestedFile: source.listFiles())
                    add(nestedFile, target);
                return;
            }

            JarEntry entry = new JarEntry(source.getPath().replace("\\", "/").replace("target/classes/", ""));
            entry.setTime(source.lastModified());
            target.putNextEntry(entry);
            in = new BufferedInputStream(new FileInputStream(source));

            byte[] buffer = new byte[1024];
            while (true)
            {
                int count = in.read(buffer);
                if (count == -1)
                    break;
                target.write(buffer, 0, count);
            }
            target.closeEntry();
        }
        finally
        {
            if (in != null)
                in.close();
        }
    }

    //endregion

    /**
     * LOAD standard values.
     *
     * @param location
     * @param resources
     */
    public void initialize(URL location, ResourceBundle resources) {
        lvStudents.setItems(Settings.getInstance().getObservableList());
        StudentView.getInstance().setIv(ivLiveView);
        StudentView.getInstance().setLv(lvStudents);
        Settings.getInstance().setLogArea(anchorPaneScrollLog);
        Settings.getInstance().setLbLoc(lbLoC);
        scrollLog.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);

        setDynamicScreenSize();
        setVersionAnchor();
        setOnChangeSize();
        setFitHeight();
        setImageClick();
        initializeLOC();
        initializeSLOMM();
        showIP_Address();
        initializeTimeSpinner();
        readProperties();

        btnStart.setDisable(false);
        btnStop.setDisable(true);
        Settings.getInstance().setStartTime(LocalTime.now());
    }

    //region initialize

    private void initializeTimeSpinner() {
        TimeSpinner spinner = new TimeSpinner();
        TimeSpinner startspinner = new TimeSpinner();
        final boolean[] alreadyaddedtime = {false};
        final LocalTime[] time = new LocalTime[1];

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("hh:mm:ss a");
        spinner.valueProperty().addListener((observable, oldValue, newValue) -> {
            btnaddTime.setOnAction(event -> {
                if(alreadyaddedtime[0]){
                    spinner.setMode(TimeSpinner.Mode.MINUTES);
                    spinner.increment(10);
                    Settings.getInstance().setEndTime(Settings.getInstance().getEndTime().plusMinutes(10));
                }
                else {
                    spinner.setMode(TimeSpinner.Mode.MINUTES);
                    spinner.increment(10);
                    Settings.getInstance().setEndTime(newValue.plusMinutes(10));
                }


                System.out.println("ADDED 10 MINIUTES "+time[0]);
                alreadyaddedtime[0] =true;
            });
            System.out.println("NEW TIME  "+newValue);
        });


        apTime.getChildren().add(spinner);
        apstarttime.getChildren().add(startspinner);


        startspinner.valueProperty().addListener((observable, oldValue, newValue) -> {
            Settings.getInstance().setStartTime(newValue);
            System.out.println("NEW STARTTIME  "+newValue);
        });
    }

    private LocalTime doSomething(LocalTime newTime, boolean addtime) {
        System.out.println(newTime);
        if (addtime) {
            newTime.plusMinutes(10);
            System.out.println("NEW TIME " + newTime);
        }

        if (LocalTime.now() == newTime) {
            System.out.println("ABGABE");
            //directory.zip(Session.getInstance().getPath());
        }
        return newTime;
    }

    /**
     * show the version number always in the bottom right corner.
     */
    private void setVersionAnchor() {
        AnchorPane.setBottomAnchor(lbVersion, 10.0);
        AnchorPane.setRightAnchor(lbVersion, 10.0);
    }

    /**
     * show screenshot in fullscreen on click.
     * <br /><br />
     * The Github-issue to this method:
     * <br />
     * https://github.com/BeatingAngel/Testumgebung/issues/16
     *
     * @since 1.11.21.067
     */
    private void setImageClick() {
        ivLiveView.setOnMouseClicked(event -> {
            Stage stage = new Stage();
            ImageView iv = new ImageView(ivLiveView.getImage());
            AnchorPane root = new AnchorPane(iv);
            Scene scene = new Scene(root, Screen.getPrimary().getBounds().getWidth(), Screen.getPrimary().getBounds().getHeight());
            stage.setScene(scene);
            AnchorPane.setLeftAnchor(iv, (Screen.getPrimary().getBounds().getWidth() - iv.getImage().getWidth()) / 2);
            AnchorPane.setTopAnchor(iv, (Screen.getPrimary().getBounds().getHeight() - iv.getImage().getHeight()) / 2);
            iv.setOnMouseClicked(event1 -> stage.close());
            stage.show();
        });
    }

    /**
     * show the path as a tooltip.
     * <br /><br />
     * The Github-issue to this method:
     * <br />
     * https://github.com/BeatingAngel/Testumgebung/issues/7
     *
     * @since
     */
    private void initializeSLOMM() { //SLOMM . . . Show Label On Mouse Move
        Tooltip mousePositionToolTip = new Tooltip("");
        lbPath.setOnMouseMoved(event -> {
            String msg = Settings.getInstance().getPath();
            if (msg != null) {
                mousePositionToolTip.setText(msg);

                Node node = (Node) event.getSource();
                mousePositionToolTip.show(node, event.getScreenX() + 50, event.getScreenY());
            }
        });
        lbAngabe.setOnMouseMoved(event -> {
            File file = Settings.getInstance().getHandOutFile();
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
            Student st = Settings.getInstance().findStudentByName(newValue.getText());
            if (st != null && st.getSeries() != null) {
                for (XYChart.Series<Number, Number> actualSeries : st.getSeries()) {
                    loc.getData().add(actualSeries);
                }
            }

            //   CHANGE SCREENSHOT
            String pathOfLastScreenshot = getLastScreenshot(newValue.getText());
            if (pathOfLastScreenshot == null) {
                pathOfLastScreenshot = "images/loading.gif";
            }
            ivLiveView.setImage(new Image(pathOfLastScreenshot));

            //   CHANGE STUDENT-INFO-DATA
            if (st != null) {
                lbFirstName.setText(st.getFirstName());
                lbLastName.setText(st.getName());
                lbCatalogNumber.setText(Integer.toString(st.getCatalogNumber()));
                String nr = lbCatalogNumber.getText();
                lbFaceNumber.setText(nr.length() < 2 ? "0".concat(nr) : nr);
                lbEnrolmentID.setText(st.getEnrolmentID());
                ObservableList<XYChart.Data<Number, Number>> ol =
                        st.getSeries().get(st.getSeries().size() - 1).getData();
                Long locVal = (Long)ol.get(ol.size() - 1).getYValue();
                lbLoC.setText(Long.toString(locVal));
            }
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
        ivFileExtensions.setFitHeight(25);
    }

    /**
     * if the screensize changes, the size of the image and chart changes too.
     */
    private void setDynamicScreenSize() {
        apStudentDetail.widthProperty().addListener((observable, oldValue, newValue) -> {
            ivLiveView.setFitWidth((double) newValue - 10);
            loc.setPrefWidth((double) newValue);
        });
        bpDataView.heightProperty().addListener((observable, oldValue, newValue) -> {
            ivLiveView.setFitHeight((double) newValue - loc.getHeight() - apInfo.getPrefHeight() - 20);
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
     * edits the chart and saves it in the settings
     */
    private void initializeLOC() {
        Settings.getInstance().setChart(loc);

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
            FileUtils.log(this, Level.ERROR, "No IP-Address found " + MyUtils.exToStr(e));
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
        String path = Settings.getInstance().getPathOfImages();
        File handOut = Settings.getInstance().getHandOutFile();
        String ssTime = tfTimeSS.getText();
        String portStr = tfPort.getText();
        boolean isRnd = TB_SS_rnd.isSelected();
        boolean startable = true;

        // checks the input of the time (number format)
        if (!ssTime.matches("[0-9]+") && !isRnd) {
            setMsg(true, "Zeit darf nur Zahlen enthalten!!");
            setImage(ivTime, false);
            startable = false;
        }
        // checks the input of the time (time greater than zero)
        if (!isRnd && ssTime.length() < 1) {
            setMsg(true, "Bitte gib einen Zeitwert(in Sek.) an");
            setImage(ivTime, false);
            startable = false;
        }
        // checks if the user selected a path
        if (path == null) {
            setMsg(true, "Bitte gib ein Verzeichnis an");
            setImage(ivPath, false);
            startable = false;
        }
        // checks if the user selected a path
        if (handOut == null) {
            setMsg(true, "Bitte eine Angabe auswählen!");
            setImage(ivAngabe, false);
            startable = false;
        }
        // if no port is set, the default port will be set
        if (!portStr.matches("[0-9]+") || (portStr.length() != 4 && portStr.length() != 5)) {
            int port = Integer.parseInt(portStr);
            if (portStr.length() == 0) {
                portStr = "50555";
            }
            else {
                if (port < 1024) {
                    setMsg(true, "System Ports are not allowed!");
                } else if (port > 65535) {
                    setMsg(true, "Ports greater than 65535 do not exist!");
                } else {
                    setMsg(true, "invalid port!");
                }
                setImage(ivPort, false);
                startable = false;
            }
        }

        if (startable) {
            if (portStr.matches("[0-9]+")) {
                Server.PORT = Integer.valueOf(portStr);
            }
            String ending = tfFileExtensions.getText();
            if (ending.length() == 0) {
                ending = "*.java; *.fxml; *.cs; *.xhtml; *.html";
            }

            if (isRnd) {
                Settings.getInstance().setInterval(new Interval(1, 30));
            } else {
                Settings.getInstance().setInterval(new Interval(Integer.parseInt(ssTime)));
            }

            String[] endings = ending.split(";");
            Settings.getInstance().setEndings(endings);

            threader = new Threader();
            server = new Thread(threader);
            server.start();
            btnStart.setDisable(true);
            btnStop.setDisable(false);
            setMsg(false, "Server is running");
            setImage(ivPort, true);
            setImage(ivAngabe, true);
            setImage(ivPath, true);
            setImage(ivTime, true);
            setImage(ivFileExtensions, true);
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
                setMsg(true, "Server stopped");
                btnStart.setDisable(false);
                btnStop.setDisable(true);
                ivAngabe.setImage(null);
                ivTime.setImage(null);
                ivFileExtensions.setImage(null);
                ivPort.setImage(null);
                ivPath.setImage(null);
                btnChange.setDisable(false);
            } else {
                setMsg(true, "Server is already stopped");
            }
        } else {
            setMsg(true, "Server was never started");
        }
    }

    /**
     * switching from Simple/Advanced-Mode to the other mode.
     *
     * @param event Specifies the event from the click on the button
     *
     * @since 1.9.22.067
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
     *
     * @param element The Place where the Image will show
     * @param correct Was the userinput correct?
     */
    private void setImage(ImageView element, boolean correct) {
        if (correct) {
            element.setImage(new Image("images/checked.png"));
        } else {
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
            Settings.getInstance().setPath(choosedFile.getPath());
        }
    }
    public void setTestOptions(ActionEvent event) throws IOException, URISyntaxException {
        File home = FileSystemView.getFileSystemView().getHomeDirectory();
        String path = home.getAbsolutePath() + "/newFolder";

        System.out.println(home.getAbsolutePath());
        File file = new File(path);
        file.mkdir();
        Settings.getInstance().setPath(path);
        String myQuery = "^IXIC";

        String test=String.valueOf(this.getClass().getResource("/testFiles/ListeSchueler4AHIF.csv"));

        File list = new File(String.valueOf(this.getClass().getResource("/testFiles/ListeSchueler4AHIF.csv")));
        File abgabe = new File(String.valueOf(this.getClass().getResource("/testFiles/Angabe.zip")));

        //String uri = String.format(URLEncoder.encode( myQuery , "UTF8" ), this.getClass().getResource("/testFiles/Angabe.zip"));
        System.out.println("ANGABE:  "+test);
        Settings.getInstance().addStudentsFromCsv(list);
        //Settings.getInstance().setHandOutFile(abgabe);
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
            Settings.getInstance().setHandOutFile(yourZip);
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
            TB_SS_rnd.setText("ON");
        } else {
            tfTimeSS.setDisable(false);
            tfTimeSS.setEditable(true);
            TB_SS_rnd.setText("OFF");
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
                Settings.getInstance().addStudent(new Student(line.split(";")[nameColumn]));
            }
        }
    }

    public void onBeforeButtonClick(ActionEvent actionEvent) {

        List<String> screens = Settings.getInstance().getListOfScreenshots();

        String fileName = screens.get((getScreenshotPos(Settings.getInstance().getActualScreenshot()))-1);

        (StudentView.getInstance().getIv())
                .setImage(new javafx.scene.image.Image("file:" + fileName));
    }

    public void onNextButtonClick(ActionEvent actionEvent) {
    }

    public void OnActual(ActionEvent actionEvent) {
    }

    private void setPrevScreenshot() {

    }

    private void setNextScreenshot() {

    }

    /**
     * find the last screenshot of a specific student by his/her name
     *
     * @param name  specialises the name of the student
     * @return      the path of the last screenshot
     */
    private String getLastScreenshot(String name) {
        String pathOfFolder = Settings.getInstance()
                .getPathOfImages().concat(
                        "/" + name
                );
        File folder = new File(pathOfFolder);
        File lastScreenshot = null;

        for (final File fileEntry : folder.listFiles()) {
            if (lastScreenshot == null) {
                lastScreenshot = fileEntry;
            } else if (lastScreenshot.lastModified() < fileEntry.lastModified()) {
                lastScreenshot = fileEntry;
            }
        }

        return lastScreenshot != null ? "file:" + lastScreenshot.getPath() : null;
    }

    private int getScreenshotPos(String file) {
        int i = 0;
        for (String screen : Settings.getInstance().getListOfScreenshots()) {
            if (screen.equals(Settings.getInstance().getActualScreenshot())) {
                return i;
            }
            i++;
        }
        return -1;
    }
}
