package at.htl.remotecontrol.common.fx;

import javafx.scene.control.Label;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

/**
 * @timeline .
 * 03.01.2016: MET 001  created class
 * 03.01.2016: MET 020  improved selection of folders and files: chooseDirectory() and chooseFile()
 * 03.01.2016: MET 003
 */
public class FxUtils {

    private static final String DEFAULT_CHOOSE_DIRECTORY_TITLE = "Select directory";
    private static final String DEFAULT_CHOOSE_FILE_TITLE = "Select file";
    private static final String DEFAULT_INITIAL_DIRECTORY = "user.home";

    /**
     * opens a window in which a folder can be chosen
     *
     * @return selected directory
     */
    public static File chooseDirectory() {
        return chooseDirectory(null, null);
    }

    /**
     * opens a window in which a folder can be chosen
     *
     * @param title            heading of DirectoryChooser
     * @param initialDirectory start directory after opening
     * @return selected directory
     */
    public static File chooseDirectory(String title, String initialDirectory) {
        DirectoryChooser dc = new DirectoryChooser();
        dc.setTitle(title == null ? DEFAULT_CHOOSE_DIRECTORY_TITLE : title);
        dc.setInitialDirectory(new File(System.getProperty(
                initialDirectory == null ? DEFAULT_INITIAL_DIRECTORY : initialDirectory)));
        return dc.showDialog(new Stage());
    }

    /**
     * opens a window in which a file can be chosen
     *
     * @return selected file
     */
    public static File chooseFile() {
        return chooseFile(null, null, null);
    }

    /**
     * opens a window in which a file can be chosen
     *
     * @param title            heading of FileChooser
     * @param initialDirectory start directory after opening
     * @param extensionFilter  limitation of file extensions
     * @return selected file
     */
    public static File chooseFile(String title, String initialDirectory, String extensionFilter) {
        FileChooser fc = new FileChooser();
        fc.setTitle(title == null ? DEFAULT_CHOOSE_FILE_TITLE : title);
        fc.setInitialDirectory(new File(System.getProperty(
                initialDirectory == null ? DEFAULT_INITIAL_DIRECTORY : initialDirectory)));
        if (extensionFilter != null) {
            fc.setSelectedExtensionFilter(new FileChooser.ExtensionFilter(extensionFilter));
        }
        return fc.showOpenDialog(new Stage());
    }

    /**
     * Displays text on a Label
     *
     * @param alert Label in which the text should be displayed
     * @param text  specifies the message to show
     * @param error TRUE   if it is an error-message
     *              FALSE  if it is a success-message
     */
    public static void setMsg(Label alert, String text, boolean error) {
        alert.setText(text);
        alert.setStyle("-fx-background-color: " + (error ? "red" : "limegreen"));
    }

}
