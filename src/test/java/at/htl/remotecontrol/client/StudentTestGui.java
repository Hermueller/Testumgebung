package at.htl.remotecontrol.client;

import at.htl.remotecontrol.common.entity.Session;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.junit.Test;
import org.loadui.testfx.GuiTest;

import java.io.IOException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

/**
 * @timeline .
 * 12.11.2015: PON 001  created test class
 * 12.11.2015: PON 050  implemented TestFX test methods
 */
public class StudentTestGui extends GuiTest {

    TextField tfUsername;
    TextField tfTeacherIP;
    PasswordField pfPassword;
    Button btnLogin;
    TextField tfPort;

    @Override
    protected Parent getRootNode() {
        Parent parent = null;
        try {
            parent = FXMLLoader.load(getClass().getResource("/fxml/Student.fxml"));
            return parent;
        } catch (IOException ex) {
            System.err.println("Error: " + ex.getMessage());
        }
        return parent;
    }

    @Test
    public void t001_testAttributes() throws IOException, InterruptedException {
        tfUsername = find("#tfUsername");
        tfTeacherIP = find("#tfTeacherIP");
        pfPassword = find("#pfPassword");
        btnLogin = find("#btnLogin");
        tfPort = find("#tfPort");

        click(tfUsername);
        type("admin");
        click(tfTeacherIP);
        type("localhost");
        click(tfPort);
        type("55555");
        click(pfPassword);
        type("passme");
        click(btnLogin);

        assertThat(Session.getInstance().getPassword(), is("passme"));
    }

}
