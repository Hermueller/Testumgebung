package at.htl.client.view;

import at.htl.common.Countdown;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Background;
import javafx.scene.layout.Pane;
import javafx.scene.shape.StrokeType;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.time.LocalTime;

/**
 * @timeline StudentGui
 * 01.10.2015: GNA 130  configured prototype and created first working version
 * 15.10.2015: PON 040  created GUI for students
 * 18.10.2015: PHI 065  implementation of the GUI
 * 24.10.2015: PON 010  create the field "username"
 * 05.11.2015: PON 010  repair of this class
 * 29.11.2015: PHI 035  Strukturierung bei der Ansicht geändert
 * 31.03.2016: MET 075  Apache-Maven-Plugin for creating jar-Files
 * 14.04.2016: MET 080  bugs fixed that were produced in the separation of client and server
 * 15.04.2016: MET 002  solved Style-Error of the Client-GUI
 * 02.06.2016: MET 020  New design of the GUI: unified language, tooltips, checkbox
 * 02.06.2016: MET 015  Inform the teacher whether the student finished the test
 * 09.06.2016: MET 100  Show quick info (time, status, transparent background, positioning, ...)
 */
public class StudentGui extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/Student.fxml"));

        Scene scene = new Scene(root);
        scene.getStylesheets().add("/styles/StudentStyle.css");

        stage.setTitle("Student");
        stage.setScene(scene);

        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

    public static void showQuickInfo(LocalTime toTime) {

        final Stage stage = new Stage();

        stage.initStyle(StageStyle.TRANSPARENT);
        stage.setAlwaysOnTop(true);

        Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
        stage.setX(primaryScreenBounds.getWidth() - 150);
        stage.setY(primaryScreenBounds.getMinY());


        Pane pane = new Pane();
        pane.setBackground(Background.EMPTY);
        Text text = new Text();
        text.setY(32);
        text.setStrokeType(StrokeType.OUTSIDE);
        text.setText("00:00:00");
        text.setFont(Font.font("System", FontWeight.BOLD, 32));

        Countdown countdown = new Countdown(text, toTime);
        countdown.setDaemon(false);
        countdown.start();

        pane.getChildren().add(text);

        Scene scene = new Scene(pane);
        scene.setFill(null);
        stage.initStyle(StageStyle.TRANSPARENT);
        stage.setScene(scene);

        stage.show();
    }

}
