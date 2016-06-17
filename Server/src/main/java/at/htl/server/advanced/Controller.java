package at.htl.server.advanced;

import at.htl.common.io.ScreenShot;
import at.htl.server.Settings;
import javafx.beans.value.ChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;

/**
 * @timeline advancedSettingsController
 * 17.06.2016: PHI 065  created class
 */
public class Controller implements Initializable {

    @FXML
    private ToggleButton TB_SS_rnd, tbImageFormat;
    @FXML
    private ProgressBar pbImageScale;
    @FXML
    private Slider slImageScale;
    @FXML
    private Label lbTimeScale;
    @FXML
    private ComboBox cbFilterSetMain;

    private List<String[]> filterSets = new LinkedList<>();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initializeSlides(slImageScale, pbImageScale, lbTimeScale, 1, true);
        initializeNewFilters();
        slImageScale.setValue(0.2);
    }

    @FXML
    public void changeSomeOptions() {
        if (TB_SS_rnd.isSelected()) {
            TB_SS_rnd.setText("ON");
        } else {
            TB_SS_rnd.setText("OFF");
        }
    }

    @FXML
    public void importPupilList() {
    }

    @FXML
    public void changeImageFormat() {
        if (tbImageFormat.isSelected()) {
            tbImageFormat.setText("PNG");
            Settings.getInstance().getScreenShot().setDEFAULT_FORMAT(ScreenShot.Format.PNG);
        }
        else {
            tbImageFormat.setText("JPG");
            Settings.getInstance().getScreenShot().setDEFAULT_FORMAT(ScreenShot.Format.JPG);
        }
    }

    /**
     * adjusts the progressbar to the slider.
     */
    public void initializeSlides(Slider slider, ProgressBar progressBar, Label label,
                                 int maxTime, boolean show_decimals) {
        slider.valueProperty().addListener((ov, old_val, new_val) -> {
            progressBar.setProgress(new_val.doubleValue() / maxTime);
            String time = (new_val.intValue() < 10) ?
                    "0" + new_val.toString().substring(0,1) :
                    new_val.toString().substring(0,2);
            time += " s";
            if (show_decimals) {
                time = String.valueOf(new_val.doubleValue()).substring(0,3);
                float quality = new_val.floatValue();
                Settings.getInstance().getScreenShot().setDEFAULT_QUALITY(quality);
            }
            label.setText(time);
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

    /**
     * creates a listItem for the List of file-extension-filters.
     * <br>
     * The List-Item is a checkbox with the name of the file-extension.
     *
     * @see   <a href="http://github.com/BeatingAngel/Testumgebung/issues/34">Student-Settings GitHub Issue</a>
     *
     * param filter    file extension name
     */
    /*public void createFilterItem(String filter) {
        CheckBox item = new CheckBox(filter);
        item.setSelected(true);
        lvFileFilters.getItems().add(item);
    }*/

    @FXML
    public void saveSettings() {
        AdvancedSettingsPackage.getInstance().setRandom(TB_SS_rnd.isSelected());
        AdvancedSettingsPackage.getInstance().setImageScale(slImageScale.getValue());
        AdvancedSettingsPackage.getInstance().setFilterSet(
                (String)cbFilterSetMain.getSelectionModel().getSelectedItem());
        AdvancedSettingsPackage.getInstance().setJpgFormat(tbImageFormat.isSelected());
        ((Stage)TB_SS_rnd.getScene().getWindow()).close();
    }
}
