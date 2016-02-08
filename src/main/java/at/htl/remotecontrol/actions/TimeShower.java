package at.htl.remotecontrol.actions;

import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;

/**
 * @timeline Text
 * 02.01.2016: PHI 055  Klasse implementiert. Farbe ermöglicht.
 * 14.01.2016: PHI 010  Statt den Lines of Code wird jetzt die Uhrzeit angezeigt.
 */
public class TimeShower extends StackPane {

    /**
     * a node which displays a value on hover, but is otherwise empty
     * @param priorValue    value before the actual value
     * @param value         actual value (to add)
     */
    public TimeShower(Long priorValue, Long value, String time) {
        setPrefSize(15, 15);

        final Label label = createTimeShowerData(priorValue, value, time);

        setOnMouseEntered(mouseEvent -> {
            getChildren().setAll(label);
            setCursor(Cursor.NONE);
            toFront();
        });
        setOnMouseExited(mouseEvent -> {
            getChildren().clear();
            setCursor(Cursor.CROSSHAIR);
        });
    }

    /**
     * create label with color and value
     *
     * @param priorValue    value before the actual value
     * @param value         actual value (to add)
     * @return      the colored label (with value)
     */
    private Label createTimeShowerData(Long priorValue, Long value, String time) {
        if (time.split("T").length > 1) {
            time = time.split("T")[1];
        }
        final Label label = new Label(time + "");
        label.getStyleClass().addAll("default-color0", "chart-line-symbol", "chart-series-line");
        label.setStyle("-fx-font-size: 20; -fx-font-weight: bold;");

        if (value < priorValue) {
            label.setTextFill(Color.FIREBRICK);
        } else if (value > priorValue) {
            label.setTextFill(Color.FORESTGREEN);
        } else {
            label.setTextFill(Color.DARKGRAY);
        }

        label.setMinSize(Label.USE_PREF_SIZE, Label.USE_PREF_SIZE);
        return label;
    }
}
