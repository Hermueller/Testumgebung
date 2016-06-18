package at.htl.server.advanced;

import at.htl.server.Settings;
import javafx.beans.value.ChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Callback;

import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;

/**
 * @timeline advancedSettingsController
 * 17.06.2016: PHI 065  created class
 * 17.06.2016: PHI 035  connected the user-settings with the amount of points in the chart
 * 17.06.2016: PHI 075  scale can be changed by the user
 * 18.06.2016: PHI 010  data-points-file is optional for the user.
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
    private TextField tfPoints;

    private List<String[]> filterSets = new LinkedList<>();
    private int points = 10;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initializeSlides(slImageScale, pbImageScale, lbImageScale, 100);
        initializeSlides(slImageQuality, pbImageQuality, lbImageQuality, 100);
        initializeNewFilters();
        slImageQuality.setValue(20);
        slImageScale.setValue(100);

        tfPoints.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                tfPoints.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });
    }

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
    }

    @FXML
    public void changeImageFormat() {
        if (tbImageFormat.isSelected()) {
            tbImageFormat.setText("PNG");
        }
        else {
            tbImageFormat.setText("JPG");
        }
    }

    /**
     * adjusts the progressbar to the slider.
     */
    public void initializeSlides(Slider slider, ProgressBar progressBar, Label label,
                                 int maxTime) {
        slider.valueProperty().addListener((ov, old_val, new_val) -> {
            progressBar.setProgress(new_val.doubleValue() / maxTime);
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
     *     <li>Set filter sets</li>
     *     <li>fill sets with standard values</li>
     *     <li>initializes callback</li>
     * </ul>
     *
     * @see   <a href="http://github.com/BeatingAngel/Testumgebung/issues/34">Student-Settings GitHub Issue</a>
     */
    public void initializeNewFilters() {
        Callback<ListView<String>, ListCell<String>> callback =
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
                };

        cbFilterSetMain.getItems().addAll("ALL-SETS", "JAVA", "C-SHARP", "SQL", "WEB");
        cbFilterSetMain.setValue("ALL-SETS");

        filterSets.add(new String[]{".java", ".xhtml", ".css", ".fxml",
                ".cs", ".cshtml", ".js", ".sql", ".xml", ".xsd", ".xsl", ".html"
        });
        filterSets.add(new String[]{".java", ".xhtml", ".css", ".fxml"});
        filterSets.add(new String[]{".cs", ".cshtml", ".js", ".css"});
        filterSets.add(new String[]{".sql", ".xml", ".xsd", ".xsl"});
        filterSets.add(new String[]{".js", ".html", ".css"});

        ChangeListener cl = ((observable, oldValue, newValue) -> {

            /*for (String filter : filterSets.get(cbFilterSetMain.getItems().indexOf(newValue))) {
                createFilterItem(filter);
            }*/

            cbFilterSetMain.setValue(newValue);
        });

        cbFilterSetMain.valueProperty().addListener(cl);
        cbFilterSetMain.setValue(cbFilterSetMain.getItems().get(0));
    }

    @FXML
    public void saveSettings() {
        AdvancedSettingsPackage.getInstance().setRandom(TB_SS_rnd.isSelected());
        AdvancedSettingsPackage.getInstance().setImageScale(slImageScale.getValue() / 100);
        AdvancedSettingsPackage.getInstance().setImageQuality(slImageQuality.getValue() / 100);
        AdvancedSettingsPackage.getInstance().setFilterSet(
                (String)cbFilterSetMain.getSelectionModel().getSelectedItem());
        AdvancedSettingsPackage.getInstance().setJpgFormat(!tbImageFormat.isSelected());
        AdvancedSettingsPackage.getInstance().setPoints(points);
        AdvancedSettingsPackage.getInstance().setSaveDataPoints(tbDataPoints.isSelected());

        Stage stage = (Stage)TB_SS_rnd.getScene().getWindow();
        stage.fireEvent(new WindowEvent(stage, WindowEvent.WINDOW_CLOSE_REQUEST));
    }

    @FXML
    public void addPoint() {
        points++;
        tfPoints.setText(Integer.toString(points));
    }

    @FXML
    public void subtractPoint() {
        if (points - 1 > 4) {
            points--;
        }
        tfPoints.setText(Integer.toString(points));
    }

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
}