package at.htl.remotecontrol;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import org.junit.Test;
import org.loadui.testfx.GuiTest;

import java.io.IOException;

/**
 * Philipp:  19.Oktober.2015  einfüger der Tests:
 */
public class TeacherTest extends GuiTest {

    @Override
    protected Parent getRootNode() {
        Parent parent = null;
        try {
            parent = FXMLLoader.load(getClass().getResource("/styles/Styles.css"));
            return parent;
        } catch (IOException ex) {
        }
        return parent;
    }

    @Test
    public void setScreenshotTime() {

    }

}
