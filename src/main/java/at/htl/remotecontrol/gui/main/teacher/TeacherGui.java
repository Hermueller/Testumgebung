package at.htl.remotecontrol.gui.main.teacher;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.Light;
import javafx.scene.effect.Lighting;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

/**
 * @timeline Text
 * 15.10.2015: PHI 001  Implementieren der GUI
 * 19.10.2015: PHI 055  Liste der verbundenen Studenten
 * 24.10.2015: PHI 005  Implementieren der 'Ordner auswahl'
 * 26.10.2015: PHI 060  Erweitern der Fehlermeldungsausgabe und Tabs, Optionen, Live-View und weitere Optionen hinzugefügt
 * 05.11.2015: PON 005  Reparatur der Datei
 * 07.12.2015: PHI 020  Fixe Größe der Liste von den verbundenen Studenten
 * 10.12.2015: PHI 005  Hinzufügen von Checkboxen, die angeben, ob etwas Ausgewählt wurde oder nicht
 * 16.12.2015: PHI 135  Beim Schließen des Fenster eine Abfrage erstellt
 * 22.12.2015: PHI 010  Optische Fehler in der GUI ausgebessert.
 */
public class TeacherGui extends Application {

    @Override
    public void start(final Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/Teacher.fxml"));

        final Scene scene = new Scene(root);
        scene.getStylesheets().add("/styles/TeacherStyle.css");

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
     * @param stage Specialises the root-window of the program.
     */
    public void askCancel(Stage stage) {
        //create Window
        Stage stage1 = new Stage();
        stage1.setResizable(false);
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

        final Light.Distant light = new Light.Distant();
        light.setAzimuth(-135.0);
        light.setColor(Color.valueOf("#861c24"));
        final Lighting lighting = new Lighting();
        lighting.setLight(light);
        lighting.setSurfaceScale(9.0);
        cancel.setEffect(lighting);

        //quit the window
        Button ok = new Button("OKEY");
        ok.setLayoutX(268);
        ok.setLayoutY(206);
        ok.setPrefHeight(35);
        ok.setPrefWidth(138);
        ok.setUnderline(true);
        ok.setDefaultButton(true);

        //set effects
        final Light.Distant light1 = new Light.Distant();
        light1.setAzimuth(-135.0);
        light1.setColor(Color.valueOf("#d7e2e4"));
        final Lighting lighting1 = new Lighting();
        lighting1.setLight(light1);
        lighting1.setSurfaceScale(9.0);
        ok.setEffect(lighting1);

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
