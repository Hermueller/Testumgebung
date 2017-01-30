package at.htl.client;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.junit.Ignore;
import org.junit.Test;
import org.loadui.testfx.GuiTest;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

/**
 * @timeline StudentTestGui
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
        Parent parent;
        try {
            parent = FXMLLoader.load(getClass().getResource("/fxml/Student.fxml"));
            return parent;
        } catch (IOException ex) {
            System.err.println("Error: " + ex.getMessage());
        }
        return null;
    }

    @Test @Ignore
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

        assertEquals("Falscher Username", "admin", tfUsername.getText());
    }

}
