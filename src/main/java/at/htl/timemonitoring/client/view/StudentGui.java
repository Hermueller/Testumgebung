package at.htl.timemonitoring.client.view;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * @timeline .
 * 01.10.2015: GNA 130  configured prototype and created first working version
 * 15.10.2015: PON 040  created GUI for students
 * 18.10.2015: PHI 065  Implementieren der Gui
 * 24.10.2015: PON 010  create the field "username"
 * 05.11.2015: PON 010  repair of this class
 * 29.11.2015: PHI 035  Strukturierung bei der Ansicht geändert
 */
public class StudentGui extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/Student.fxml"));

        Scene scene = new Scene(root);
        scene.getStylesheets().add("/styles/TeacherStyle.css");

        stage.setTitle("Student");
        stage.setScene(scene);

        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

}
