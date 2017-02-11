package at.htl.server.advanced;

import at.htl.common.Pupil;
import at.htl.common.fx.FxUtils;
import at.htl.common.io.FileUtils;
import at.htl.server.Settings;
import at.htl.server.entity.Student;
import javafx.beans.value.ChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.apache.logging.log4j.Level;

import java.io.*;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.Properties;
import java.util.ResourceBundle;

/**
 * controller for all the advanced settings.
 *
 * @timeline Controller
 * 17.06.2016: PHI 065  created class
 * 17.06.2016: PHI 035  connected the user-settings with the amount of points in the chart
 * 17.06.2016: PHI 075  scale can be changed by the user
 * 18.06.2016: PHI 010  data-points-file is optional for the user.
 * 18.06.2016: PHI 045  properties-file can be imported and applied.
 * 18.06.2016: PHI 020  properties-file can be created from the GUI.
 * 18.06.2016: PHI 015  fixed graphic bug in progressbar by using math.
 * 19.06.2016: PHI 025  implemented the port. Port is now in properties-file too.
 * 20.06.2016: PHI 045  added the random time, fix time, test-directory and handout to the properties file.
 * 21.06.2016: PHI 010  included the testmode in the properties-file
 * 21.06.2016: PHI 010  only *.properties-files can be imported.
 */
public class Controller implements Initializable {

    @FXML
    private ToggleButton TB_SS_rnd, tbImageFormat, tbDataPoints;
    @FXML
    private ProgressBar pbImageScale, pbImageQuality;
    @FXML
    private Slider slImageScale, slImageQuality;
    @FXML
    private Label lbImageScale, lbImageQuality;
    @FXML
    private ComboBox cbFilterSetMain;
    @FXML
    private TextField tfPoints, tfPort;

    private int points = 10;
    private ChangeListener<String> onlyNumber = (observable, oldValue, newValue) -> {
        if (!newValue.matches("\\d*")) {
            tfPoints.setText(newValue.replaceAll("[^\\d]", ""));
        }
    };

    //region Basic GUI methods

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initializeSlides(slImageScale, pbImageScale, lbImageScale, 100);
        initializeSlides(slImageQuality, pbImageQuality, lbImageQuality, 100);
        initializeNewFilters();
        slImageQuality.setValue(AdvancedSettingsPackage.getInstance().getImageQuality()*100);
        slImageScale.setValue(AdvancedSettingsPackage.getInstance().getImageScale()*100);

        tfPort.textProperty().addListener(onlyNumber);
        tfPoints.textProperty().addListener(onlyNumber);
    }

    /**
     * changes the text of the source between ON and OFF.
     *
     * @param event the event of the click.
     */
    public void changeSomeOptions(ActionEvent event) {
        ToggleButton source = (ToggleButton) event.getSource();
        if (source.isSelected()) {
            source.setText("ON");
        } else {
            source.setText("OFF");
        }
    }

    @FXML
    public void importPupilList() {
        // Create and show the file filter
        FileChooser fc = new FileChooser();
        fc.setSelectedExtensionFilter(new FileChooser.ExtensionFilter("CSV Files (*.csv)", "*.csv"));
        File listFile = fc.showOpenDialog(new Stage());
        if (Settings.getInstance().getHandOutFile() != null) {
            fc.setInitialDirectory(new File(Settings.getInstance().getPath()));
        } else {
            fc.setInitialDirectory(new File(System.getProperty("user.home")));
        }

        // Check the user pressed OK, and not Cancel.
        if (listFile != null) {
            Settings.getInstance().setHandOutFile(listFile);
            importPupilListToData(listFile.getAbsolutePath());
        }
    }

    public void importPupilListToData(String filename) {
        new BufferedReader(new InputStreamReader(this.getClass().getResourceAsStream(filename),
                Charset.forName("UTF-8")))
                .lines()
                .skip(1)
                .map(s -> s.split(";"))
                .map(a -> new Student(new Pupil(0,
                    "xx120016",
                     a[3],
                     a[2],
                     "")))
                .forEach((student) -> Settings.getInstance().addStudent(student));
    }

    /**
     * changes the image format between PNG and JPG
     */
    @FXML
    public void changeImageFormat() {
        if (tbImageFormat.isSelected()) {
            tbImageFormat.setText("PNG");
        } else {
            tbImageFormat.setText("JPG");
        }
    }

    /**
     * adjusts the progressbar to the slider.
     */
    public void initializeSlides(Slider slider, ProgressBar progressBar, Label label,
                                 int maxTime) {
        slider.valueProperty().addListener((ov, old_val, new_val) -> {
            progressBar.setProgress(((new_val.doubleValue() - 10) / maxTime) * (10.0 / 9.0));
            String value = (new_val.intValue() < 10) ?
                    "0" + new_val.toString().split("\\.")[0] :
                    new_val.toString().split("\\.")[0];
            label.setText(value);
        });
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

        cbFilterSetMain.getItems().addAll("ALL-SETS", "JAVA", "C-SHARP", "SQL", "WEB");
        cbFilterSetMain.setValue("ALL-SETS");

        ChangeListener cl = ((observable, oldValue, newValue) -> cbFilterSetMain.setValue(newValue));

        cbFilterSetMain.valueProperty().addListener(cl);
        cbFilterSetMain.setValue(cbFilterSetMain.getItems().get(0));
    }

    /**
     * saves the advanced settings in a package for the later usage.
     */
    @FXML
    public void saveSettings() {
        AdvancedSettingsPackage.getInstance().setRandom(TB_SS_rnd.isSelected());
        AdvancedSettingsPackage.getInstance().setImageScale(slImageScale.getValue() / 100);
        AdvancedSettingsPackage.getInstance().setImageQuality(slImageQuality.getValue() / 100);
        AdvancedSettingsPackage.getInstance().setFilterSet(
                (String) cbFilterSetMain.getSelectionModel().getSelectedItem());
        AdvancedSettingsPackage.getInstance().setJpgFormat(!tbImageFormat.isSelected());
        AdvancedSettingsPackage.getInstance().setPoints(points);
        AdvancedSettingsPackage.getInstance().setSaveDataPoints(tbDataPoints.isSelected());
        AdvancedSettingsPackage.getInstance().setPort(Integer.parseInt(tfPort.getText()));

        Stage stage = (Stage) TB_SS_rnd.getScene().getWindow();
        stage.fireEvent(new WindowEvent(stage, WindowEvent.WINDOW_CLOSE_REQUEST));
    }

    /**
     * increases the amount of points in the chart by one.
     */
    @FXML
    public void addPoint() {
        points++;
        tfPoints.setText(Integer.toString(points));
    }

    /**
     * decreases the amount of points in the chart by one.
     * The amount can't be beneath 5.
     */
    @FXML
    public void subtractPoint() {
        if (points - 1 > 4) {
            points--;
        }
        tfPoints.setText(Integer.toString(points));
    }

    /**
     * saves the amount of points for the chart from the input-textfield.
     */
    @FXML
    public void savePoints() {
        int points = Integer.parseInt(tfPoints.getText());
        if (points > 4) {
            this.points = points;
        } else {
            tfPoints.setText("5");
            this.points = 5;
        }
    }

    //endregion

    //region Properties-file methods

    /**
     * imports a properties file as the new settings.
     */
    @FXML
    public void importSettings() {
        FileChooser dc = new FileChooser();
        FileChooser.ExtensionFilter extFilter =
                new FileChooser.ExtensionFilter("PROP files (*.properties)", "*.properties");
        dc.getExtensionFilters().add(extFilter);
        dc.setInitialDirectory(new File(System.getProperty("user.home")));
        dc.setTitle("Choose your properties-file");
        File choosedFile = dc.showOpenDialog(new Stage());
        if (choosedFile != null) {
            extractInformation(choosedFile);
        }
    }

    /**
     * applies the settings from the file to the program.
     *
     * @param file the file to extract the info from.
     */
    @SuppressWarnings("unchecked")
    private void extractInformation(File file) {
        Properties prop = new Properties();
        InputStream stream = null;

        try {
            stream = new FileInputStream(file);
            prop.load(stream);

            if (prop.getProperty("format").toUpperCase().equals("JPG")) {
                tbImageFormat.setSelected(false);
            }
            if (prop.getProperty("createFile").toUpperCase().equals("TRUE")) {
                tbDataPoints.setSelected(true);
            }
            tfPoints.setText(prop.getProperty("dataPoints"));
            points = Integer.parseInt(prop.getProperty("dataPoints"));
            slImageScale.setValue(Double.parseDouble(prop.getProperty("scale")) * 100);
            slImageQuality.setValue(Double.parseDouble(prop.getProperty("quality")) * 100);
            cbFilterSetMain.setValue(prop.getProperty("filter"));
            tfPort.setText(prop.getProperty("port"));
            TB_SS_rnd.setSelected(prop.getProperty("random").toUpperCase().equals(Boolean.toString(true).toUpperCase()));
            AdvancedSettingsPackage.getInstance().setTime(Integer.parseInt(prop.getProperty("timeSec")));
            Settings.getInstance().setPath(prop.getProperty("testDirectory"));
            Settings.getInstance().setHandOutFile(new File(prop.getProperty("testHandout")));
            AdvancedSettingsPackage.getInstance().setTestMode(Boolean.parseBoolean(prop.getProperty("testmode")));

        } catch (IOException e) {
            FileUtils.log(Level.ERROR, e.getMessage());
            e.printStackTrace();
            Settings.getInstance().printError(Level.ERROR, e.getStackTrace(), "ERRORS", e.getLocalizedMessage());
        } finally {
            if (stream != null) {
                try {
                    stream.close();
                } catch (IOException e) {
                    Settings.getInstance().printError(Level.ERROR, e.getStackTrace(), "ERRORS", e.getLocalizedMessage());
                }
            }
        }
    }

    /**
     * extracts all the settings from the GUI into a properties file.
     * That file will be saved in the exports-directory.
     */
    @FXML
    public void exportSettings() {
        Properties prop = new Properties();
        OutputStream output = null;

        try {

            if (Settings.getInstance().getPathOfExports() != null) {
                output = new FileOutputStream(Settings.getInstance().getPathOfExports() + "/settings.properties");

                // set the properties value
                prop.setProperty("format", tbImageFormat.getText());
                prop.setProperty("quality", Double.toString(convertToOneDecimalPoint(slImageQuality.getValue() / 100)));
                prop.setProperty("scale", Double.toString(convertToOneDecimalPoint(slImageScale.getValue() / 100)));
                prop.setProperty("dataPoints", tfPoints.getText());
                prop.setProperty("createFile", Boolean.toString(tbDataPoints.isSelected()));
                prop.setProperty("filter", (String) cbFilterSetMain.getValue());
                prop.setProperty("port", tfPort.getText());
                prop.setProperty("random", Boolean.toString(TB_SS_rnd.isSelected()));
                prop.setProperty("timeSec", Integer.toString(AdvancedSettingsPackage.getInstance().getTime()));
                prop.setProperty("testDirectory", Settings.getInstance().getPath());
                prop.setProperty("testHandout", Settings.getInstance().getHandOutFile().getAbsolutePath());
                prop.setProperty("testmode", Boolean.toString(false));

                // save properties to exports directory
                prop.store(output, null);

                FxUtils.showPopUp("Properties-file created!\nPath: "
                        + Settings.getInstance().getPathOfExports() + "\n/settings.properties", true);
            } else {
                FxUtils.showPopUp("Failed to create! (no export-directory or false file)", false);
            }

        } catch (IOException e) {
            FileUtils.log(Level.ERROR, e.getMessage());
            Settings.getInstance().printError(Level.ERROR, e.getStackTrace(), "ERRORS", e.getLocalizedMessage());
        } finally {
            if (output != null) {
                try {
                    output.close();
                } catch (IOException e) {
                    Settings.getInstance().printError(Level.ERROR, e.getStackTrace(), "ERRORS", e.getLocalizedMessage());
                }
            }

        }
    }

    /**
     * returns double value with only one decimal point.
     *
     * @param value the double value with many decimal points.
     * @return the value with one decimal point.
     */
    public double convertToOneDecimalPoint(double value) {
        int oneD = (int) (value * 10);

        return oneD / 10.0;
    }

    //endregion
}
