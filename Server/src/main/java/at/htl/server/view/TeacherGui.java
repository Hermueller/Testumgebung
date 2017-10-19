package at.htl.server.view;

import at.htl.server.Settings;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;

/**
 * @timeline TeacherGui
 * 01.10.2015: PON 130  configured prototype and created first working version
 * 08.10.2015: PON 001  created class
 * 15.10.2015: PON 060  established repository for the project
 * 15.10.2015: PON 040  created GUI for the teacher
 * 15.10.2015: PHI 001  Implementieren der GUI
 * 19.10.2015: PHI 055  Liste der verbundenen Studenten
 * 24.10.2015: PHI 005  Implementieren der 'Ordner auswahl'
 * 26.10.2015: PHI 060  Erweitern der Fehlermeldungsausgabe und Tabs, Optionen, Live-View und weitere Optionen hinzugefügt
 * 05.11.2015: PON 020  repair of this file
 * 07.12.2015: PHI 020  Fixe Größe der Liste von den verbundenen Studenten
 * 10.12.2015: PHI 005  Hinzufügen von Checkboxen, die angeben, ob etwas Ausgewählt wurde oder nicht
 * 16.12.2015: PHI 135  Beim Schließen des Fenster eine Abfrage erstellt
 * 22.12.2015: PHI 010  Optische Fehler in der GUI ausgebessert.
 * 31.01.2016: PHI 001  bugfix (Schließen des Fensters)
 * 21.03.2016: PHI 001  catch all exceptions
 * 31.03.2016: PON 075  Apache-Maven-Plugin for creating jar-Files
 * 15.04.2016: MET 002  solved Style-Error of the Server-GUI
 * 28.05.2016: PHI 015  added new shortcuts (ESC, F1)
 * 02.06.2016: PON 020  No Multiple Alerts
 * 21.06.2016: PHI 001  fixed error-bug
 * 21.06.2016: PHI 020  alert can be closed by pressing ESC again.
 * 13.02.2017: MET 020  Bug bei der Auswahl einer Angabe behoben
 */
public class TeacherGui extends Application {

    @Override
    public void start(Stage stage) throws Exception {


        Parent root = FXMLLoader.load(getClass().getResource("/fxml/Teacher.fxml"));

        final Scene scene = new Scene(root);
        scene.getStylesheets().add("/styles/TeacherStyle.css");
        stage.getIcons().add(new Image(
                "images/logo.png"
        ));

        stage.setTitle("Server");
        stage.setScene(scene);

        setShortCuts(scene, stage);

        stage.show();

        Thread.setDefaultUncaughtExceptionHandler((t, e) ->
                Settings.getInstance().printMessage(t, e));
        Platform.setImplicitExit(false);

        stage.setOnCloseRequest(event -> {
            askCancel(stage);

            event.consume();
        });
    }

    public static void main(String[] args) {
        launch(args);
    }

    /**
     * initializes shortcuts for the program
     *
     * @param scene The scene from where the keyEvent comes
     * @param stage The stage which will be closed/hidden/...
     */
    public void setShortCuts(Scene scene, Stage stage) {
        scene.setOnKeyReleased(event -> {

            if (event.getCode() == KeyCode.ESCAPE) {    // ESC -> CLOSE
                askCancel(stage);
            } else if (event.getCode() == KeyCode.F1) {   // F1  -> HELP
                getHostServices().showDocument("http://BeatingAngel.github.io/Testumgebung/#program");
            }

        });
    }

    /**
     * Show a window to ask if the root-window should really be closed or not.
     *
     * @param stage Specialises the root-window of the program.
     */
    public void askCancel(Stage stage) {
        Alert alert = new Alert(Alert.AlertType.WARNING,"Your Server is running!\nWould you like to close this Application?",ButtonType.OK,ButtonType.CANCEL);

        alert.showAndWait()
                .filter(response -> response == ButtonType.OK)
                .ifPresent((r) -> {
                    Platform.exit();
                    System.exit(0);
                });
    }

}
