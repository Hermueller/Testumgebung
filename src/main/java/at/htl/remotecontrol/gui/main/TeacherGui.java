package at.htl.remotecontrol.gui.main;

import at.htl.remotecontrol.gui.controller.ControllerTeacher;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 * Philipp:  15.10.2015   Implementieren der Gui
 */
public class TeacherGui extends Application {

    @Override
    public void start(final Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/Teacher.fxml"));

        final Scene scene = new Scene(root);
        scene.getStylesheets().add("/styles/Styles.css");

        stage.setTitle("Teacher Client");
        stage.setScene(scene);

        stage.show();
/*
        Platform.setImplicitExit(false);

        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            //@Override
            public void handle(WindowEvent event) {
                if (!stage.isFullScreen())
                    event.consume();
            }
        });*/
    }

    public static void main(String[] args) {
        launch(args);
    }

}
