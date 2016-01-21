package at.htl.remotecontrol;

import at.htl.remotecontrol.entity.Session;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import org.junit.Test;
import org.loadui.testfx.GuiTest;

import java.io.File;
import java.io.IOException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * @timeline Text
 * 19.10.2015: PHI 030  einfüger der Tests:
 * 12.11.2015: PON 020  Implementierung des ParameterTests.
 */
public class TeacherTest extends GuiTest {

    @Override
    protected Parent getRootNode() {
        Parent parent = null;
        try {
            parent = FXMLLoader.load(getClass().getResource("/fxml/Teacher.fxml"));
            return parent;
        } catch (IOException ex) {
            System.out.println("Error: " + ex.getMessage());
        }
        return parent;
    }

    @Test
    public void t001_checkParameters() throws IOException {

        click("#tfPort").type("5555");
        Session.getInstance().addStudentsFromCsv(new File(getClass().getResource("/testFiles/ListeAllerSchueler4AHIF.csv").getPath()));
        Session.getInstance().setHandOutFile(new File(getClass().getResource("/testFiles/Angabe.zip").getPath()));
        Session.getInstance().setPath(getClass().getResource("/testFiles/Screenshots").getPath());
        click("#tbPassword").type("myPassword");
        click("#tfFileendings").type(".java");
        click("btnStart");

        assertThat(Session.getInstance().getStudentsList().get(0),is("Herbert"));




    }

}
