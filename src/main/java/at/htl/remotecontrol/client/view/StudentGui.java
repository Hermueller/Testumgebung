package at.htl.remotecontrol.client.view;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * @timeline Text
 * 18.10.2015: PHI 065  Implementieren der Gui
 * 05.11.2015: PON 010  Reparatur der Datei
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
