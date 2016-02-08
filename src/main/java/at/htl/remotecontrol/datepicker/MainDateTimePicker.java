package at.htl.remotecontrol.datepicker;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * Created by gnadi on 26.11.15.
 */
public class MainDateTimePicker extends Application {

    @Override
    public void start(Stage primaryStage) {
        VBox vBox = new VBox();
        Scene s = new Scene(new ScrollPane(vBox), 600, 400);
        DateTimePicker d = new DateTimePicker();

        // Date only
        d.valueProperty().addListener(t -> System.out.println(t));

        // Time only
        d.timeValueProperty().addListener(t -> System.out.println(t));

        // DateAndTime
        d.dateTimeValueProperty().addListener(t -> System.out.println(t));

        vBox.getChildren().add(d);

        primaryStage.setScene(s);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}