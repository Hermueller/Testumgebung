package at.htl.client.view;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

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
 */
public class StudentGui extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Student.fxml"));
        Parent root = loader.load();

        Scene scene = new Scene(root);

        stage.setTitle("Student");
        stage.setScene(scene);

        stage.show();

        ((Controller) loader.getController()).setStage(stage);
    }

    public static void main(String[] args) {
        launch(args);
    }

}
