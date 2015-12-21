package at.htl.remotecontrol.gui.main.student;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * @timeline Text
 * 18.10.2015: PHI ???  Implementieren der Gui
 * 26.10.2015: MET ???  Implementieren der Fehlermeldungsausgabe
 * 05.11.2015: PON 010  Reparatur der Datei
 * 29.11.2015: PHI ???  Strukturierung bei der Ansicht geändert
 */
public class StudentGui extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/Student.fxml"));

        Scene scene = new Scene(root);
        scene.getStylesheets().add("/styles/Styles.css");

        stage.setTitle("Student");
        stage.setScene(scene);

        stage.show();

    }

    public static void main(String[] args) {
        launch(args);
    }

}
