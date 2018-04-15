package at.htl.server.view;

import at.htl.common.MyUtils;
import at.htl.common.TimeSpinner;
import at.htl.common.actions.IpConnection;
import at.htl.common.fx.FxUtils;
import at.htl.common.fx.StudentView;
import at.htl.common.io.FileUtils;
import at.htl.server.*;
import at.htl.server.advanced.AdvancedSettingsPackage;
import at.htl.server.entity.Interval;
import at.htl.server.entity.Student;
import com.aquafx_project.AquaFx;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.*;
import javafx.scene.chart.StackedAreaChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.*;
import javafx.util.Duration;
import org.apache.logging.log4j.Level;
import org.controlsfx.control.Notifications;

import javax.imageio.ImageIO;
import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.util.jar.Attributes;
import java.util.jar.JarEntry;
import java.util.jar.JarOutputStream;
import java.util.jar.Manifest;
import java.util.stream.Collectors;

/**
 * @timeline ServerController
 * 15.10.2015: GNA 001  created class
 * 15.10.2015: PHI 035  Allowed quickinfo-input in the GUI for the screenshot-delay.
 * 19.10.2015: PON 020  list of connected pupils
 * 24.10.2015: PON 020  teachers can now select the folder where the screenshots are saved
 * 26.10.2015: PHI 050  created method for the messages from the start and stop of the server (+random quickinfo).
 * 05.11.2015: PON 015  implemented selecting of specification file
 * 06.11.2015: PON 002  expansion to the password field
 * 12.11.2015: PON 002  save password in the repository
 * 29.11.2015: PHI 025  Handout + shows error messages in the GUI.
 * 07.12.2015: PHI 030  Live-View and LOC-Chart will always be the size of the window.
 * 07.12.2015: PHI 020  changed at.htl.client.styles of the LineChart.
 * 17.12.2015: PON 120  function "importPupilList" for importing student lists
 * 17.12.2015: PON 005  Bug found: exception Handling missing, registration of pupils
 * 30.12.2015: GNA 100  TimeSpinner Abagabe hinzugefügt
 * 31.12.2015: PHI 010  LineChart revised (if the student from the list changes then the LineChart changes too).
 * 01.01.2016: PHI 010  fixed bug in the LineChart.
 * 03.01.2016: GNA 120  Add quickinfo to Abgabe
 * 06.01.2016: PHI 025  BugFix (LineChart will not be influenced when the student changes)
 * 15.01.2016: PHI 060  Shows check-pictures for errors and success when the server is started.
 * 20.01.2016: PHI 040  Simple- and Advanced-Mode created. / input quickinfo now in seconds
 * 22.01.2016: GNA 030  added Startspinner
 * 23.01.2016: PHI 020  Tooltip and Version created.
 * 24.01.2016: PHI 035  Shows the screenshot in fullscreen on click and closes on another click. +RandomTimeBugFix
 * 10.02.2016: GNA 120  Send Start and Abgabe Zeit for automatic Abgabe
 * 10.03.2016: PHI 120  BugFix (Screenshot, Lines-of-Code Chart)
 * 11.03.2016: GNA 030  BugFix to send quickinfo data
 * 12.03.2016: PHI 125  show last screenshot of the student if the selection changed (no waitTime)
 * 15.03.2016: PHI 030  show the log on the application and check the portnumber
 * 18.03.2016: PHI 005  export the log to an text-file
 * 21.03.2016: PHI 055  fixed bug in the screenshot-View  AND   created pop-up window for the log-export
 * 22.03.2016: PHI 225  create JAR with button; create properties; fixed minor bug in the view
 * 23.03.2016: PHI 145  added a new Tab called Student-Info (shows Student infos) AND changed language+View of the application
 * 24.03.2016: PHI 105  created the LineChart-Export
 * 04.04.2016: GNA 045  Added test data
 * 14.04.2016: GNA 050  Testdata for fast mode
 * 14.04.2016: PON 120  created method for patrol-mode
 * 25.04.2016: PHI 065  fixed the "log out". Teacher now notices if a student logs out.
 * 03.05.2016: PHI 120  changes the layout of the GUI + implemented the Student-Settings.
 * 04.05.2016: PHI 110  implemented the kickStudent- and the new timeInterval-Function.
 * 04.05.2016: PHI 050  added the file extension filter
 * 05.05.2016: PHI 045  applied the file extension filter (it can be used now [bugfix]) + Layout changed
 * 05.05.2016: PHI 130  changes layout. added filter-Sets which can be applied during the test.
 * 06.05.2016: PHI 015  changed the layout and style
 * 07.05.2016: PHI 015  changed the chart from LineChart to AreaChart. Changed the code of the chart.
 * 12.05.2016: PHI 020  implemented the LogFilter-Method
 * 13.05.2016: PHI 035  changed the view and code from the file extension filters.
 * 18.05.2016: PHI 065  improved simple- and advanced-Mode
 * 21.05.2016: PHI 015  fixed bug
 * 21.05.2016: PHI 040  created the help-website
 * 22.05.2016: PHI 020  extended the help-website-tab. The help-website can be seen offline AND online.
 * 22.05.2016: PHI 035  the help-website can be reloaded (can go during runtime from offline to online).
 * 02.06.2016: PHI 030  implemented the advanced settings for the image (only in GUI).
 * 11.06.2016: PHI 030  implemented student count AND recovered lost code from the last merge.
 * 16.06.2016: PHI 040  fixed some bugs (in the last commit) and implemented pdf,jpg submission
 * 16.06.2016: PHI 035  opens extra window for the advanced settings
 * 17.06.2016: PHI 035  changed the style of the advanced settings chart
 * 17.06.2016: PHI 045  scale can be changed by the user
 * 19.06.2016: PHI 015  removed unnecessary code.
 * 20.06.2016: PHI 025  fixed bug. unique filters. The latest selected file will be shown in the FileChooser.
 * 21.06.2016: PHI 025  if the teacher closes the ServerSocket, all StudentServers are shut down.
 * 09.02.2017: PHI 050  fixed some bugs for issue #69 and #66
 * 09.02.2017: PHI 040  set the default directory and selects the handout-file automatically.
 * 11.02.2017: PHI 055  fixed some bugs, removed student-import and shows path on exports.
 * 12.02.2017: PHI 035  fixed some GUI-Elements (userfriendly).
 * 13.02.2017: MET 015  Anzeige für die Start- und Endzeit korrigiert
 * 13.02.2017: MET 010  GUI benutzerfreundlicher gestaltet
 */
public class Controller implements Initializable {

    //region Tab: Option Variables
    @FXML
    private AnchorPane spOption;
    @FXML
    private AnchorPane apOption;
    @FXML
    private ScrollPane spStudentList;
    @FXML
    private Button tbMode, btnTestMode;
    @FXML
    private ImageView ivPort;
    @FXML
    private Label lbAngabe;
    @FXML
    private ImageView ivAngabe;
    @FXML
    private Label lbPath;
    @FXML
    private ImageView ivPath;
    @FXML
    private Slider slHarvester;
    @FXML
    private ComboBox cbFilterSetMain;
    @FXML
    private ImageView ivFileExtensions;
    @FXML
    private Label lbAlert;
    @FXML
    private Button btnStart;
    @FXML
    private Button btnStop;
    @FXML
    private Label lbVersion, lbCount;
    @FXML
    private Label lbTime;
    @FXML
    private ProgressBar pbHarvester;
    @FXML
    private TabPane tpMainTabs;
    @FXML
    private TextField tfHandoutPath, tfDirectoryPath;
    @FXML
    private Button btndirectory, btnhandout;

    //endregion

    //region HandIn Variables
    @FXML
    private Button btnAddTime;
    @FXML
    private AnchorPane apStartTime, apEndTime;
    //endregion

    //region Log Variables
    @FXML
    private ScrollPane scrollLog;
    @FXML
    private AnchorPane anchorPaneScrollLog;
    @FXML
    private ComboBox cbLogFilter;
    //endregion

    //region Student-Info Variables
    @FXML
    private Label lbNameOfPupilSI, lbNameOfPupilM;
    @FXML
    private Button btnExportLOC;
    @FXML
    private StackedAreaChart<Number, Number> loc;
    //endregion

    //region Student-Settings Variables
    @FXML
    private Label lbAddress, lbTimeInterval, lbSettingsHeader;
    @FXML
    private ProgressBar pbHarvesterStudent;
    @FXML
    private Slider slHarvesterStudent;
    @FXML
    private ComboBox cbFilterSet;
    @FXML
    private CheckBox tbToggleSettings;
    @FXML
    private TextField tfNewFilter;
    @FXML
    private ListView<CheckBox> lvFileFilters;
    @FXML
    private Accordion accordion;
    @FXML
    private ToggleButton tbstudents;

    private List<String[]> filterSets = new LinkedList<>();
    //endregion

    //region Student-Spy Variables
    @FXML
    private ImageView ivLiveView;
    //endregion

    //region Web Help Variables
    @FXML
    WebView wvHelp;
    @FXML
    Label lbStat;
    @FXML
    Button btnReload;
    @FXML
    Tab tbHelp;
    //endregion

    //region other Variables
    @FXML
    private AnchorPane apInfo;
    @FXML
    private Label lbDate;
    //    @FXML
//    private ListView<Button> lvStudents;
    @FXML
    private VBox vbStudents;
    @FXML
    private Button btnPatrolMode;
    private boolean patrolMode = false;
    private PatrolMode pm = new PatrolMode();

    private Thread server;
    private Threader threader;
    //endregion

    //region INITIALIZE and Constructor

    /**
     * empty constructor
     */
    public Controller() {

    }

    /**
     * LOAD standard values.
     */
    public void initialize(URL location, ResourceBundle resources) {
        styleStage();


        StudentList.createStudentList(this.vbStudents);
        StudentList.getStudentList().refreshList();

//        lvStudents.setItems(Settings.getInstance().getObservableList());
        StudentView.getInstance().setIv(ivLiveView);
//        StudentView.getInstance().setLv(lvStudents);
        Settings.getInstance().setLogArea(anchorPaneScrollLog);
        //TODO
        scrollLog.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);

        setDynamicScreenSize();
        setVersionAnchor();
        setOnStudentChange();
        setFitHeight();
        setImageClick();
        setHomePath();

        initializeLOC();
        initializeSLOMM();
        initializeSlides(slHarvesterStudent, pbHarvesterStudent, lbTimeInterval, 60, false);
        initializeSlides(slHarvester, pbHarvester, lbTime, 60, false);
        initializeNewFilters();
        initializePatrol();
        initializeLogFilters();
        initializeWebView();
        setImage(ivPath, true);

        initializeTimeSpinner();
        readProperties();

        Settings.getInstance().setLbCount(lbCount);
        AdvancedSettingsPackage.getInstance().setLbAddress(lbAddress);
        AdvancedSettingsPackage.getInstance().setTimeSlider(slHarvester);
        AdvancedSettingsPackage.getInstance().setTestMode(btnTestMode);
        AdvancedSettingsPackage.getInstance().setTestMode(false);

        btnStart.setDisable(false);
        btnStop.setDisable(true);
        Settings.getInstance().setStartTime(LocalTime.now());
        slHarvester.setValue(3);
        slHarvesterStudent.setValue(10);

        slHarvesterStudent.valueChangingProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(
                    ObservableValue<? extends Boolean> observableValue,
                    Boolean wasChanging,
                    Boolean changing) {

                if (!changing) {
                    saveScreenshottime();
                    try {
                        Platform.runLater(() -> Notifications.create()
                                .title("Student Settings updated")
                                .hideAfter(Duration.seconds(1))
                                .showInformation());
                    }
                    catch (Exception e){
                        System.out.println(e.getMessage());
                    }
                }
            }
        });
        String seperator=getSeperatorForOS();
        String DefPath=System.getProperty("user.dir")+seperator+"Server"+seperator+"src"+seperator+"main"+seperator+"resources"+seperator+"images";
        Settings.getInstance().setHandOutFile(new File(DefPath+seperator+"NoExamInstruction1s.jpg"));

        tfHandoutPath.setText(Settings.getInstance().getHandOutFile().getPath());
    }


        /*try {
            // Get the location where the JAR-File is currently located at.
            String currFileLoc = Controller.class.getProtectionDomain().getCodeSource().getLocation()
                    .toURI().getPath();
            //remove the filename in order to get the directory name.
            String[] dirs = currFileLoc.split("/");
            Settings.getInstance().setPath(currFileLoc.replace(dirs[dirs.length - 1], ""));
        } catch (URISyntaxException e) {
            FileUtils.log(this, Level.WARN, "Could not set Standardpath" + MyUtils.exceptionToString(e));
            Settings.getInstance().printError(Level.WARN, e.getStackTrace(), "WARNINGS", e.getMessage());
       }*/


    /**
     * styles the application.
     */
    public void styleStage() {
        //AquaFx.style();
    }


    public void saveScreenshottime() {
        long new_time = (long) slHarvesterStudent.getValue();

        if (!tbToggleSettings.isSelected()) {
            Button selected = (Button) StudentView.getInstance()
                    .getLv().getSelectionModel().getSelectedItem();
            Student toChange = StudentList.getStudentList()
                    .findStudentByIpAddress(selected.getId());
            System.out.println(selected.getId());
            toChange.setInterval(new Interval(new_time));
            String[] filters = getSelectedFilters();
            toChange.setFilter(filters);
        } else {
            for (Object obj : StudentView.getInstance().getLv().getItems()) {
                String address = ((Button) obj).getId();
                Student toChange = StudentList.getStudentList()
                        .findStudentByIpAddress(address);

                toChange.setInterval(new Interval(new_time));
                String[] filters = getSelectedFilters();
                toChange.setFilter(filters);
            }
        }
    }

    //endregion

    //region {GitHub-Issue: #--} Start and Stop Server

    /**
     * checks all fields on correctness.
     * starts the server for the students to connect.
     */
    @FXML
    public void startServer() {

        String path = Settings.getInstance().getPathOfImages();
        File handOut = Settings.getInstance().getHandOutFile();
        int time = (int) slHarvester.getValue();
        int port = AdvancedSettingsPackage.getInstance().getPort();
        boolean isRnd = AdvancedSettingsPackage.getInstance().isRandom();
        boolean startable = true;

        // checks if the user selected a path
        if (path == null || !handOut.exists()) {
            setMsg(true, "Please select a valid directory!");
            setImage(ivPath, false);
            startable = false;
        }
        // checks if the user selected a path
        if (handOut == null || !handOut.exists()) {
            setMsg(true, "Please select valid handout (pdf, jpg)!");
            setImage(ivAngabe, false);
            startable = false;
        }
        // if no port is set, the default port will be set
        if (port < 1024 || port > 65535) {
            if (port == 0) {
                port = 50555;
            }
            if (port < 1024) {
                setMsg(true, "System Ports are not allowed!");
            } else if (port > 65535) {
                setMsg(true, "Ports greater than 65535 do not exist!");
            }
        }

        if (startable) {
            Server.PORT = port;

            if (isRnd) {
                Settings.getInstance().setInterval(new Interval(1, 30));
            } else {
                Settings.getInstance().setInterval(new Interval(time));
            }

            Settings.getInstance().setEndings(getSelectedFilters());

            threader = new Threader();
            server = new Thread(threader);
            server.start();
            btnStart.setDisable(true);
            btnStop.setDisable(false);
            setMsg(false, "Server is running");
            setImage(ivPort, true);
            setImage(ivAngabe, true);
            setImage(ivPath, true);
            setImage(ivFileExtensions, true);
            tbMode.setDisable(true);
            slHarvester.setDisable(true);

            btnhandout.setDisable(true);
            btndirectory.setDisable(true);
        }
    }

    /**
     * stops the server.
     */
    @FXML
    public void stopServer() {
        if (server != null) {
            if (!server.isInterrupted()) {
                threader.stop();
                server.interrupt();
                setMsg(true, "Server stopped");
                btnStart.setDisable(false);
                btnStop.setDisable(true);
                ivAngabe.setImage(null);
                ivFileExtensions.setImage(null);
                ivPort.setImage(null);
                ivPath.setImage(null);
                tbMode.setDisable(false);
                if (pm.isRunning()) {
                    pm.stopIt();
                }

//                for (Button b : lvStudents.getItems()) {
//                    b.setStyle("-fx-background-color: crimson");
//                    Student student = Settings.getInstance().findStudentByAddress(b.getId());
//                    student.getServer().shutdown();
//                }
                btndirectory.setDisable(false);
                btnhandout.setDisable(false);
                slHarvester.setDisable(false);
            } else {
                setMsg(true, "Server is already stopped");
            }
        } else {
            setMsg(true, "Server was never started");
        }
    }

    //endregion

    //region {GitHub-Issue: #04} Screenshot-Showcase Methods

    @FXML
    public void onBeforeButtonClick() {

        List<String> screens = Settings.getInstance().getListOfScreenshots();

        String fileName = screens.get((getScreenshotPos(Settings.getInstance().getActualScreenshot())) - 1);

        (StudentView.getInstance().getIv())
                .setImage(new javafx.scene.image.Image("file:" + fileName));
    }

    @FXML
    public void onNextButtonClick() {
    }

    @FXML
    public void OnActual() {
    }

    @SuppressWarnings("unused")
    public void setPrevScreenshot() {

    }

    @SuppressWarnings("unused")
    public void setNextScreenshot() {

    }

    /**
     * find the last screenshot of a specific student by his/her name
     *
     * @param name specialises the name of the student
     * @return the path of the last screenshot
     */
    public String getLastScreenshot(String name) {
        String pathOfFolder = Settings.getInstance()
                .getPathOfImages().concat(
                        "/" + name
                );
        File folder = new File(pathOfFolder);
        File lastScreenshot = null;
        File[] files = folder.listFiles();

        if (files != null) {
            for (final File fileEntry : files) {
                if (lastScreenshot == null) {
                    lastScreenshot = fileEntry;
                } else if (lastScreenshot.lastModified() < fileEntry.lastModified()) {
                    lastScreenshot = fileEntry;
                }
            }
        }

        return lastScreenshot != null ? "file:" + lastScreenshot.getPath() : null;
    }

    @SuppressWarnings("unused")
    public int getScreenshotPos(String file) {
        int i = 0;
        for (String screen : Settings.getInstance().getListOfScreenshots()) {
            if (screen.equals(Settings.getInstance().getActualScreenshot())) {
                return i;
            }
            i++;
        }
        return -1;
    }

    //endregion

    //region {GitHub-Issue: #05 #06} Simple- and Advanced-Mode Methods

    /**
     * switching from Simple/Advanced-Mode to the other mode.
     *
     * @see <a href="http://github.com/BeatingAngel/Testumgebung/issues/5">Advanced-Mode GitHub Issue</a>
     * @see <a href="http://github.com/BeatingAngel/Testumgebung/issues/6">Simple-Mode GitHub Issue</a>
     * @since 1.9.22.067
     */
    @SuppressWarnings("unchecked")
    @FXML
    public void changeMode() {
        Parent root;
        try {
            final Stage stage = new Stage();

            root = FXMLLoader.load(getClass().getResource("/fxml/AdvancedSettings.fxml"));

            final Scene scene = new Scene(root);
            scene.getStylesheets().add("/styles/TeacherStyle.css");
            stage.getIcons().add(new Image(
                    "images/spying_eye.png"
            ));

            stage.setTitle("Advanced Settings");
            stage.initModality(Modality.WINDOW_MODAL);
            stage.setScene(scene);
            AquaFx.styleStage(stage, StageStyle.UNDECORATED);

            stage.show();

            stage.setOnCloseRequest(event -> {
                slHarvester.setDisable(AdvancedSettingsPackage.getInstance().isRandom());
                Settings.getInstance().getScreenShot().setQuality(
                        (float) convertToOneDecimalPoint(AdvancedSettingsPackage.getInstance().getImageQuality()));
                Settings.getInstance().getScreenShot().setFormat(
                        AdvancedSettingsPackage.getInstance().isJpgFormat() ? "JPG" : "PNG");
                Settings.getInstance().getScreenShot().setScale(
                        convertToOneDecimalPoint(AdvancedSettingsPackage.getInstance().getImageScale()));
                cbFilterSetMain.getSelectionModel().select(AdvancedSettingsPackage.getInstance().getFilterSet());

                stage.close();
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public double convertToOneDecimalPoint(double value) {
        int oneD = (int) (value * 10);

        return oneD / 10.0;
    }

    //endregion

    //region {GitHub-Issue: #07} Tooltip Methods

    /**
     * show the path as a tooltip.
     *
     * @see <a href="http://github.com/BeatingAngel/Testumgebung/issues/7">Tooltip GitHub Issue</a>
     */
    public void initializeSLOMM() { //SLOMM . . . Show Label On Mouse Move
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

    //endregion

    //region {GitHub-Issue: #12} Submission Methods

    public void initializeTimeSpinner() {
        TimeSpinner startTimeSpinner = new TimeSpinner();
        TimeSpinner endTimeSpinner = new TimeSpinner();
        //final LocalTime[] time = new LocalTime[1];

        //DateTimeFormatter formatter = DateTimeFormatter.ofPattern("hh:mm:ss a");
        endTimeSpinner.valueProperty().addListener((observable, oldValue, newValue) -> {
            btnAddTime.setOnAction(event -> {
                endTimeSpinner.setMode(TimeSpinner.Mode.MINUTES);
                endTimeSpinner.increment(10);
                Settings.getInstance().incrementEndTime(10);
            });
        });

        apStartTime.getChildren().add(startTimeSpinner);
        apEndTime.getChildren().add(endTimeSpinner);
    }

    @FXML
    public void saveTime() {
        Settings.getInstance().setStartTime(((TimeSpinner) apStartTime.getChildren().get(0)).getValue());
        Settings.getInstance().setEndTime(((TimeSpinner) apEndTime.getChildren().get(0)).getValue());
    }

    //endregion

    //region {GitHub-Issue: #16} Fullscreen-Screenshot Methods

    /**
     * shows screenshot in fullscreen on click.
     *
     * @see <a href="http://github.com/BeatingAngel/Testumgebung/issues/16">Fullscreen GitHub Issue</a>
     * @since 1.11.21.067
     */
    public void setImageClick() {
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

    //endregion

    //region {GitHub-Issue: #22} Patrol-Mode Methods

    /**
     * runs through the list of the connected students.
     */
    @FXML
    public void startPatrol() {
        if (!patrolMode) {
            patrolMode = true;
            btnPatrolMode.setText("Stop Patrol");
            pm = new PatrolMode();
//            pm.setLv(lvStudents);
            pm.start();
        } else {
            patrolMode = false;
            btnPatrolMode.setText("Start Patrol");
            pm.stopIt();
            pm.interrupt();
        }
    }

    /**
     * changes the quickinfo for showing the student.
     */
    public void initializePatrol() {
        slHarvesterStudent.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                Settings.getInstance().setSleepTime(newValue.longValue() * 1000);
            }
        });
    }

    //endregion

    //region {GitHub-Issue: #23} JAR-File AND Version-Number Methods

    /**
     * create pop-up window to ask for the version number
     * and create a properties-file for it and creates a JAR-file
     * for the student and the teacher.
     * <br>
     *
     * @see <a href="http://github.com/BeatingAngel/Testumgebung/issues/23">JAR GitHub Issue</a>
     * @since 1.12.35.071
     */
    @FXML
    public void fromVersion() {
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
     * <br>
     * The properties include:
     * <ul>
     * <li>Date from the creation of the JAR-file (current date)</li>
     * <li>Version of the application</li>
     * </ul>
     *
     * @param version Specifies the version of the application
     */
    public void createProp(String version) {
        Properties prop = new Properties();
        OutputStream output = null;

        try {

            output = new FileOutputStream("src/main/resources/config.properties");
            prop.setProperty("version", version);
            prop.setProperty("build.date", LocalDate.now().toString());
            prop.store(output, null);


            output = new FileOutputStream("target/classes/config.properties");
            prop.setProperty("version", version);
            prop.setProperty("build.date", LocalDate.now().toString());
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
     * <br>
     * The properties include:
     * <ul>
     * <li>Date from the creation of the JAR-file</li>
     * <li>Version of the application</li>
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
        lbDate.setText("created on: " + prop.getProperty("build.date"));
    }

    /**
     * creates a jar-file for the student.
     *
     * @see <a href="http://github.com/BeatingAngel/Testumgebung/issues/23">JAR GitHub Issue</a>
     * @since 1.12.35.071
     */
    public void createJar() {
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
        } catch (IOException exp) {
            FileUtils.log(Level.ERROR, exp.getMessage());
        }

        FxUtils.showPopUp("created JAR-file successfully!!\nPath: "
                + Settings.getInstance().getPathOfExports() + "\n/student.jar", true);
    }

    /**
     * add files from the source to the jar file
     *
     * @param source the source of the files to add
     * @param target the stream from the jar-file
     * @throws IOException can't create JAR-File
     * @see <a href="http://stackoverflow.com/a/1281295">Method-code source</a>
     */
    public void add(File source, JarOutputStream target) throws IOException {
        BufferedInputStream in = null;
        try {
            File[] files = source.listFiles();
            if (source.isDirectory() && files != null) {
                String name = source.getPath().replace("\\", "/").replace("target/classes/", "");
                if (!name.isEmpty()) {
                    if (!name.endsWith("/"))
                        name += "/";
                    JarEntry entry = new JarEntry(name);
                    entry.setTime(source.lastModified());
                    target.putNextEntry(entry);
                    target.closeEntry();
                }
                for (File nestedFile : files)
                    add(nestedFile, target);
                return;
            }

            JarEntry entry = new JarEntry(source.getPath().replace("\\", "/").replace("target/classes/", ""));
            entry.setTime(source.lastModified());
            target.putNextEntry(entry);
            in = new BufferedInputStream(new FileInputStream(source));

            byte[] buffer = new byte[1024];
            while (true) {
                int count = in.read(buffer);
                if (count == -1)
                    break;
                target.write(buffer, 0, count);
            }
            target.closeEntry();
        } finally {
            if (in != null)
                in.close();
        }
    }

    //endregion

    //region {GitHub-Issue: #25} Directory Delete Methods

    @FXML
    public void deleteFiles() {
        FileChooser fc = new FileChooser();
        fc.setSelectedExtensionFilter(new FileChooser.ExtensionFilter("ZIP files (*.zip)", "*.zip"));
        File yourZip = fc.showOpenDialog(new Stage());

        // Check the user pressed OK, and not Cancel.
        if (yourZip != null) {
            Settings.getInstance().setHandOutFile(yourZip);
        }
    }

    //endregion

    //region {GitHub-Issue: #26} Log Methods

    /**
     * writes the Log into a TEXT-file.
     * <br>
     *
     * @see <a href="http://github.com/BeatingAngel/Testumgebung/issues/26">Log GitHub Issue</a>
     * @since Testumgebung: v1.11.33.051
     */
    @FXML
    public void exportLog() {
        try {
            List<String> list = new LinkedList<>();
            ObservableList<Node> nodes = ((VBox) Settings.getInstance().getLogArea().getChildren().get(0)).getChildren();
            for (Node node : nodes) {
                TextField tf = (TextField) node;
                list.add(tf.getText());
            }

            Path file = Paths.get(Settings.getInstance().getPathOfExports().concat("/log.txt"));
            Files.write(file, list, Charset.forName("UTF-8"));

            FxUtils.showPopUp("exported log successfully!!\nPath: "
                    + Settings.getInstance().getPathOfExports() + "\n/log.txt", true);
        } catch (IOException e) {
            FileUtils.log(Level.ERROR, e.getMessage());
        }
    }

    /**
     * implements the values and listener for the comboBox (LogFilter)
     */
    @SuppressWarnings("unchecked")
    public void initializeLogFilters() {
        cbLogFilter.getItems().addAll(Settings.getInstance().getLogFields().keySet());
        cbLogFilter.setValue("ALL");
        cbLogFilter.valueProperty().addListener((observable, oldValue, newValue) -> {
            Settings.getInstance().setCurrentLogFilter((String) newValue);
            VBox vbox = (VBox) anchorPaneScrollLog.getChildren().get(0);
            vbox.getChildren().clear();
            anchorPaneScrollLog.setPrefHeight(570);
            for (TextField tf : Settings.getInstance().getLogFields().get(newValue.toString())) {
                vbox.getChildren().add(tf);
            }
            anchorPaneScrollLog.setMinHeight(vbox.getChildren().size() * 30);
        });
    }

    //endregion

    //region {GitHub-Issue: #34} Student-Settings Methods

    /**
     * creates a listItem for the List of file-extension-filters.
     * <br>
     * The List-Item is a checkbox with the name of the file-extension.
     *
     * @param filter file extension name
     * @see <a href="http://github.com/BeatingAngel/Testumgebung/issues/34">Student-Settings GitHub Issue</a>
     */
    public void createFilterItem(String filter) {
        CheckBox item = new CheckBox(filter);
        item.setSelected(true);
        lvFileFilters.getItems().add(item);
    }

    /**
     * initializes the filter-Sets.
     * <br>
     * This includes:
     * <ul>
     * <li>Set filter sets</li>
     * <li>fill sets with standard values</li>
     * <li>initializes callback</li>
     * </ul>
     *
     * @see <a href="http://github.com/BeatingAngel/Testumgebung/issues/34">Student-Settings GitHub Issue</a>
     */
    @SuppressWarnings("unchecked")
    public void initializeNewFilters() {
        /*Callback<ListView<String>, ListCell<String>> callback =
                new Callback<ListView<String>, ListCell<String>>() {
                    @Override public ListCell<String> call(ListView<String> param) {
                        final ListCell<String> cell = new ListCell<String>() {
                            {
                                super.setPrefWidth(100);
                            }
                            @Override public void updateItem(String item,
                                                             boolean empty) {
                                super.updateItem(item, empty);
                                if (item != null) {
                                    setText(item);
                                    Rectangle rec = new Rectangle(20, 20);
                                    rec.setFill(Settings.getInstance().getFilterColors().get(item));
                                    setGraphic(rec);
                                }
                                else {
                                    setText(null);
                                }
                            }
                        };
                        return cell;
                    }
                };*/

        cbFilterSet.getItems().addAll("ALL-SETS", "JAVA", "C-SHARP", "SQL", "WEB");
        cbFilterSet.setValue("ALL-SETS");

        filterSets.add(new String[]{".java", ".xhtml", ".css", ".fxml",
                ".cs", ".cshtml", ".js", ".sql", ".xml", ".xsd", ".xsl", ".html"
        });
        filterSets.add(new String[]{".java", ".xhtml", ".css", ".fxml"});
        filterSets.add(new String[]{".cs", ".cshtml", ".js", ".css"});
        filterSets.add(new String[]{".sql", ".xml", ".xsd", ".xsl"});
        filterSets.add(new String[]{".js", ".html", ".css"});

        ChangeListener cl = ((observable, oldValue, newValue) -> {

            lvFileFilters.getItems().clear();

            for (String filter : filterSets.get(cbFilterSet.getItems().indexOf(newValue))) {
                createFilterItem(filter);
            }

            cbFilterSet.setValue(newValue);
            cbFilterSetMain.setValue(newValue);
        });

        cbFilterSet.valueProperty().addListener(cl);
        cbFilterSetMain.valueProperty().addListener(cl);

        cbFilterSetMain.getItems().addAll(cbFilterSet.getItems());
        cbFilterSetMain.setValue(cbFilterSetMain.getItems().get(0));

        accordion.setExpandedPane(accordion.getPanes().get(1));
    }

    /**
     * adjusts the progressbar to the slider.
     */
    public void initializeSlides(Slider slider, ProgressBar progressBar, Label label,
                                 int maxTime, boolean show_decimals) {
        slider.valueProperty().addListener((ov, old_val, new_val) -> {
            progressBar.setProgress(new_val.doubleValue() / maxTime);
            String time = (new_val.intValue() < 10) ?
                    "0" + new_val.toString().substring(0, 1) :
                    new_val.toString().substring(0, 2);
            time += " s";
            if (show_decimals) {
                time = String.valueOf(new_val.doubleValue()).substring(0, 3);
                Settings.getInstance().getScreenShot().setQuality(new_val.floatValue());
            }
            label.setText(time);
            if (slider == slHarvester) {
                AdvancedSettingsPackage.getInstance().setTime(new_val.intValue());
            }
        });
    }

    /**
     * changes the header.
     * <br>
     * + All changes of the settings will be applied to:
     * <ul><li>All students</li></ul>
     * OR
     * <ul><li>Selected student</li></ul>
     */
    @FXML
    public void toggleSettings() {
        if (tbToggleSettings.isSelected()) {    // "apply for all"
            //tbToggleSettings.setText("Apply for \"Selected Student\"");
            lbSettingsHeader.setText("All Student Settings:");
        } else {
            //tbToggleSettings.setText("Apply for \"All Students\"");
            lbSettingsHeader.setText("Selected Student Settings:");
        }
    }

    /**
     * changes the quickinfo interval of the selected student.
     *
     * @see <a href="https://github.com/BeatingAngel/Testumgebung/issues/34">Student-Settings GitHub Issue</a>
     */
    @FXML
    public void saveStudentChanges() {
        if (!tbstudents.isSelected()) {
            Button selected = (Button) StudentView.getInstance().getLv().getSelectionModel().getSelectedItem();
            Student toChange = StudentList.getStudentList()
                    .findStudentByIpAddress(selected.getId());
            String[] filters = getSelectedFilters();
            toChange.setFilter(filters);
        } else {
            for (Object obj : StudentView.getInstance().getLv().getItems()) {
                String address = ((Button) obj).getId();
                Student toChange = StudentList.getStudentList()
                        .findStudentByIpAddress(address);

                String[] filters = getSelectedFilters();
                toChange.setFilter(filters);
            }
        }
        FxUtils.showPopUp("Änderungen übernommen", true);
    }


    /**
     * converts the selected checkboxes to an String[].
     *
     * @return the selected filters as String[]
     */
    public String[] getSelectedFilters() {
        List<String> filterList = lvFileFilters.getItems().stream()
                .filter(CheckBox::isSelected)
                .map(CheckBox::getText)
                .collect(Collectors.toCollection(LinkedList::new));
        String[] filters = new String[filterList.size()];
        for (int i = 0; i < filters.length; i++) {
            filters[i] = filterList.get(i);
        }
        return filters;
    }

    /**
     * adds a new filter to the current selected set.
     */
    @FXML
    public void addFilterToSet() {
        int index = cbFilterSet.getSelectionModel().getSelectedIndex();

        if (!Arrays.asList(filterSets.get(index)).contains(tfNewFilter.getText())) {
            String[] set = filterSets.get(index);
            String[] newSet = new String[set.length + 1];
            System.arraycopy(set, 0, newSet, 0, set.length);
            newSet[newSet.length - 1] = tfNewFilter.getText();
            filterSets.set(index, newSet);
            lvFileFilters.getItems().add(new CheckBox(tfNewFilter.getText()));
        }
    }

    //endregion

    //region {GitHub-Issue: #35} Chart Methods

    /**
     * saved the chart in a singleton class.
     */
    public void initializeLOC() {
        Settings.getInstance().setChart(loc);

        loc.setCursor(Cursor.CROSSHAIR);
    }

    /**
     * creates an image from the StackedAreaChart of a specific student and saves it.<br>
     * The location of the picture is the "exports"-folder.<br>
     * The name of the picture is "catalogNumber-LastName-LineChart".<br><br>
     * example:   07-Mustermann-LineChart.png
     *
     * @see <a href="http://github.com/BeatingAngel/Testumgebung/issues/35">Chart GitHub Issue</a>
     */
    @FXML
    public void exportLOC() {
        loc.setLegendVisible(true);

        WritableImage image = loc.snapshot(new SnapshotParameters(), null);
        //String studentInfo = lbCatalogNumber.getText() + "-" + lbLastName.getText() + "-LineChart.png";
        String studentInfo = "xx";
        File file = new File(Settings.getInstance().getPathOfExports().concat("/" + studentInfo));

        try {
            ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", file);
        } catch (IOException e) {
            FileUtils.log(Level.ERROR, e.getMessage() + " ;; " + e.getLocalizedMessage());
            Settings.getInstance().printError(Level.ERROR, e.getStackTrace(), "ERRORS", e.getMessage());
        }
        loc.setLegendVisible(false);

        FxUtils.showPopUp("exported LineChart successfully !!\nPath: "
                + Settings.getInstance().getPathOfExports() + "\n/" + studentInfo, true);
    }

    /**
     * if the SelectedStudent changes, the Chart and ImageView
     * is cleared and the new Chart and Image will be shown.ein
     */
    public void setOnStudentChange() {
        StudentList.getStudentList().selectedStudentProperty().addListener((observable, oldValue, newValue) -> {
            Student st = newValue;

            System.out.println("Current Student: "+st.getPupil().getEnrolmentID());

            //   CHANGE CHART
            loc.getData().clear();
            if (st.getSeries().size() > 0) {
                for (List<XYChart.Series<Number, Number>> seriesList : st.getSeries()) {
                    for (XYChart.Series<Number, Number> series : seriesList) {
                        loc.getData().add(series);
                    }
                }
            }

            //   CHANGE SCREENSHOT
            String pathOfLastScreenshot = getLastScreenshot(newValue.getPupil().getLastName());
            if (pathOfLastScreenshot == null) {
                pathOfLastScreenshot = "images/loading.gif";
            }
            ivLiveView.setImage(new Image(pathOfLastScreenshot));

            //   CHANGE STUDENT-INFO-DATA
            String nr = Integer.toString(st.getPupil().getCatalogNumber());

            String name = String.format("%s     %s  %s %s",
                    st.getPupil().getEnrolmentID(),
                    nr.length() < 2 ? "0".concat(nr) : nr,
                    st.getPupil().getFirstName(),
                    st.getPupil().getLastName());

            lbNameOfPupilM.setText(name);
            lbNameOfPupilSI.setText(name);

            if (!patrolMode) {
                tpMainTabs.getSelectionModel().select(1);
            }
        });
    }

    /**
     * if the screensize changes, the size of the image and chart changes too.
     */
    public void setDynamicScreenSize() {
        spOption.widthProperty().addListener((observable, oldValue, newValue) -> {
            AnchorPane.setLeftAnchor(apOption, (double) newValue / 2 - apOption.getPrefWidth() / 2);
            ivLiveView.setFitWidth((double) newValue);
            loc.setPrefWidth((double) newValue);
            btnReload.setLayoutX((double) newValue - btnReload.getWidth() - 20);
            wvHelp.setPrefWidth((double) newValue);
        });
        spOption.heightProperty().addListener((observable, oldValue, newValue) -> {
            AnchorPane.setTopAnchor(apOption, (double) newValue / 2 - apOption.getPrefHeight() / 2);
            ivLiveView.setFitHeight((double) newValue - apInfo.getPrefHeight());
            loc.setPrefHeight((double) newValue - loc.getLayoutY() - 35);
            btnExportLOC.setLayoutY(loc.getLayoutY() + loc.getPrefHeight());
            wvHelp.setPrefHeight((double) newValue - btnReload.getHeight());
        });
        ivLiveView.setPreserveRatio(true);
        ivLiveView.setSmooth(true);
        ivLiveView.setCache(true);
        spStudentList.widthProperty().addListener((observable, oldValue, newValue) -> {
            this.vbStudents.setPrefWidth(((double)newValue)-25);
        });
    }

    //endregion

    //region {GitHub-Issue: #36} Help Website

    /**
     * shows the help-website in the help-tab.<br>
     * If the application has internet-connection then the online website is shown.<br>
     * If the application has no internet-connection then the outdated, offline website is shown.
     */
    public void initializeWebView() {
        WebEngine webEngine = wvHelp.getEngine();
        boolean applicationHasInternetConnection = IpConnection.checkInternetConnection();
        String url;
        try {
            if (applicationHasInternetConnection) {
                webEngine.setJavaScriptEnabled(true);
                url = "http://BeatingAngel.github.io/Testumgebung/#program";
                lbStat.setText("Status: online");
            } else {
                url = Server.class.getResource("/").toExternalForm().split("Server")[0].concat("index.html");
                lbStat.setText("Status: offline");
            }
            webEngine.load(url);
        } catch (Exception exc) {
            FileUtils.log(Level.WARN, exc.getMessage());
            Settings.getInstance().printError(Level.WARN, exc.getStackTrace(), "WARNINGS", "");
        }
    }

    /**
     * reloads the help-website.
     */
    @FXML
    public void reloadWebsite() {
        initializeWebView();
    }

    //endregion

    //region {GitHub-Issue: #--} Other Methods

    /**
     * Shows a green picture if the values were correct from the user
     * and shows a red picture if the userinput was wrong.
     *
     * @param element The Place where the Image will show
     * @param correct Was the userinput correct?
     */
    public void setImage(ImageView element, boolean correct) {
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
    public void setMsg(boolean error, String msg) {
        String color = (error ? "red" : "limegreen");   //bei Fehlermeldung rot, sonst grün
        lbAlert.setText(msg);
        lbAlert.setStyle("-fx-background-color: " + color);
    }

    /**
     * shows a dialog-screen to choose the directory where the directories of the
     * screenshots and the finished tests will be.
     */
    @FXML
    public void chooseDirectory() {
        DirectoryChooser dc = new DirectoryChooser();
        if (Settings.getInstance().getPath() != null) {
            dc.setInitialDirectory(new File(Settings.getInstance().getPath()));
        } else {
            dc.setInitialDirectory(new File(System.getProperty("user.home")));
        }
        dc.setTitle("Wähle dein Ziel-Verzeichnis");
        File choosedFile = dc.showDialog(new Stage());
        if (choosedFile != null) {
            Settings.getInstance().setPath(choosedFile.getPath());
            tfDirectoryPath.setText(Settings.getInstance().getPath());
        }
    }

    @FXML
    public void setTestOptions() throws IOException, URISyntaxException {

    }

    /**
     * shows a dialog-screen to choose the test-file.
     * only zip-files are allowed.
     */
    @FXML
    public void chooseHandOutFile() {
        // Create and show the file filter
        FileChooser fc = new FileChooser();
        //fc.setSelectedExtensionFilter(new FileChooser.ExtensionFilter("ZIP files (*.zip)", "*.zip"));
        File yourZip = fc.showOpenDialog(new Stage());
        if (Settings.getInstance().getHandOutFile() != null) {
            fc.setInitialDirectory(Settings.getInstance().getHandOutFile());
        }

        // Check the user pressed OK, and not Cancel.
        if (yourZip != null) {
            Settings.getInstance().setHandOutFile(yourZip);
            tfHandoutPath.setText(Settings.getInstance().getHandOutFile().getPath());
            setImage(ivAngabe, true);
            setMsg(false, "Handout added");
        }

    }

    static String getSeperatorForOS() {
        String seperator = "/";
        if (System.getProperty("os.name").contains("Windows")) {
            seperator = "\\";
        }
        return seperator;
    }

    /**
     * selects the Desktop as Home-Path
     */
    public void setHomePath() {
        try {
            File f = new File(Controller.class.getProtectionDomain()
                    .getCodeSource().getLocation().toURI().getPath());
            String filePath = f.getAbsolutePath().
                    substring(0, f.getAbsolutePath().lastIndexOf(File.separator));
            Settings.getInstance().printErrorLine(Level.INFO,
                    "current Path: " + filePath, true, "OTHER");
            Settings.getInstance().setPath(filePath);
            tfDirectoryPath.setText(Settings.getInstance().getPath());
            f = new File(filePath + "/angabe.png");
            if (f.exists() && !f.isDirectory()) {
                Settings.getInstance().setHandOutFile(f);
            }
            f = new File(filePath + "/angabe.jpg");
            if (f.exists() && !f.isDirectory()) {
                Settings.getInstance().setHandOutFile(f);
            }
            f = new File(filePath + "/angabe.pdf");
            if (f.exists() && !f.isDirectory()) {
                Settings.getInstance().setHandOutFile(f);
            }
            f = new File(filePath + "/angabe.zip");
            if (f.exists() && !f.isDirectory()) {
                Settings.getInstance().setHandOutFile(f);
            }
            if (Settings.getInstance().getHandOutFile() != null) {
                tfHandoutPath.setText(Settings.getInstance().getHandOutFile().getPath());
            }
        } catch (URISyntaxException e) {
            FileUtils.log(this, Level.WARN, "Coudn't find current path" + MyUtils.exceptionToString(e));
            Settings.getInstance().printError(Level.WARN, e.getStackTrace(), "WARNINGS", e.getMessage());
        }
    }

    /**
     * show the version number always in the bottom right corner.
     */
    public void setVersionAnchor() {
        AnchorPane.setBottomAnchor(lbVersion, 10.0);
        AnchorPane.setRightAnchor(lbVersion, 10.0);
    }

    /**
     * sets the size of the images, which are shown after starting/stopping the server
     */
    public void setFitHeight() {
        ivPort.setFitHeight(25);
        ivAngabe.setFitHeight(25);
        ivPath.setFitHeight(25);
        ivFileExtensions.setFitHeight(25);
    }

    //endregion
}