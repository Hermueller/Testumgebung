package at.htl.remotecontrol.server;

import at.htl.remotecontrol.common.entity.Session;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import org.junit.Test;
import org.loadui.testfx.GuiTest;

import java.io.IOException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

/**
 * @timeline .
 * 12.11.2015: PON 001  created test class
 * 12.11.2015: PON 070  implemented parameter tests
 */
public class TeacherTestGui extends GuiTest {

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
    public void t001_checkParameters() {

        ToggleButton TB_SS_rnd = find("#TB_SS_rnd");
        TextField tfTimeSS = find("#tfTimeSS");
        PasswordField tbPassword = find("#tbPassword");
        Button btnStart = find("#btnStart");
        Session session = Session.getInstance();

        session.setPath(System.getProperty("user.home"));

        click(TB_SS_rnd);
        click(tfTimeSS);
        type("3000");
        click(tbPassword);
        type("passme");
        click(btnStart);

        assertThat(session.getInterval(),is(3000L));
        assertThat(session.getPassword(), is("passme"));
    }

}
