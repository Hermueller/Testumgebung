package at.htl.remotecontrol;

import at.htl.remotecontrol.actions.RobotAction;
import at.htl.remotecontrol.actions.RobotActionQueue;
import at.htl.remotecontrol.actions.ScreenShot;
import at.htl.remotecontrol.server.TeacherServer;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.junit.Test;
import org.loadui.testfx.GuiTest;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.TimeUnit;

/**
 * Patrick:  12.November.2015 Erstellen der Klasse
 */
public class StudentTest extends GuiTest{

    TextField tfUsername;
    TextField tfTeacherIP;
    PasswordField pfPassword;
    Button btnLogin;

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

        Socket socket = new Socket("localhost", TeacherServer.PORT);
        ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
        RobotActionQueue jobs = new RobotActionQueue();



        click(tfUsername);
        type("admin");
        click(tfTeacherIP);
        type("localhost");
        click(pfPassword);
        type("passme");
        click(btnLogin);

        jobs.add(new ScreenShot(1.0));

        RobotAction action = jobs.poll(0L, TimeUnit.MILLISECONDS);

        out.writeObject(action);
        out.reset();
        out.flush();



    }


}
