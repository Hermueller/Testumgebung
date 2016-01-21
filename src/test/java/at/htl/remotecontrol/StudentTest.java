package at.htl.remotecontrol;

import at.htl.remotecontrol.entity.Session;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import org.junit.Test;
import org.loadui.testfx.GuiTest;

import java.io.IOException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

/**
 * 12.11.2015: PON 045  Erstellen der Klasse und implementieren einer Testmethode
 */

public class StudentTest extends GuiTest{

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

        find("#tfUsername");
        find("#tfTeacherIP");
        find("#pfPassword");
        find("#btnLogin");
        find("#tfPort");



        click("#tfUsername").type("admin");
        click("#tfTeacherIP").type("localhost");
        click("#tfPort").type("5555");
        click("#pfPassword").type("pwd");
        click("#btnLogin");

        assertThat(Session.getInstance().getPassword(),is("passme"));



    }


}
