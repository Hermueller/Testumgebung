package at.htl.remotecontrol.gui.main.teacher;

import at.htl.remotecontrol.gui.controller.ControllerTeacher;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.Effect;
import javafx.scene.effect.Light;
import javafx.scene.effect.Lighting;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 * Philipp:  15.10.2015   ???  Implementieren der Gui
 * Philipp:  16.12.2015   132  Beim Schließen des Fenster eine Abfrage erstellt
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
     * Show a window to ask if the root-window should really be closed or not.
     *
     * @param stage     Specialises the root-window of the program.
     */
    public void askCancel(Stage stage) {
        //create Window
        Stage stage1 = new Stage();
        AnchorPane root1 = new AnchorPane();
        Scene scene1 = new Scene(root1, 431, 279);

        AnchorPane pane = new AnchorPane();
        pane.setPrefWidth(431);
        pane.setPrefHeight(279);

        scene1.setCursor(Cursor.CLOSED_HAND);

        //ask questions
        Label text = new Label("Haben Sie den Server auch gestoppt?");
        text.setLayoutX(21);
        text.setLayoutY(23);
        text.setPrefHeight(35);
        text.setPrefWidth(385);

        Label text2 = new Label("Wollen Sie das Fenster wirklich schließen?");
        text2.setLayoutX(21);
        text2.setLayoutY(67);
        text2.setPrefHeight(35);
        text2.setPrefWidth(385);

        //do not quit the window
        Button cancel = new Button("CANCEL");
        cancel.setLayoutX(21);
        cancel.setLayoutY(206);
        cancel.setPrefHeight(35);
        cancel.setPrefWidth(138);
        cancel.setUnderline(true);

        //quit the window
        Button ok = new Button("JAP");
        ok.setLayoutX(268);
        ok.setLayoutY(206);
        ok.setPrefHeight(35);
        ok.setPrefWidth(138);
        ok.setUnderline(true);
        ok.setDefaultButton(true);

        //set effects
        final Light.Distant light = new Light.Distant();
        light.setAzimuth(-135.0);
        light.setColor(Color.valueOf("#d7e2e4"));
        final Lighting lighting = new Lighting();
        lighting.setLight(light);
        lighting.setSurfaceScale(9.0);
        ok.setEffect(lighting);

        //on click close
        cancel.setOnAction(cancelEvent -> stage1.close());

        ok.setOnAction(okEvent -> {
            stage1.close();
            stage.close();
        });

        pane.getChildren().addAll(text, text2, cancel, ok);
        pane.setStyle("-fx-background-color: #808080");

        root1.getChildren().add(pane);

        stage1.setScene(scene1);
        stage1.show();
        //****************
    }

}
