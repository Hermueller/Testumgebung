package at.htl.remotecontrol;

import at.htl.remotecontrol.entity.Student;
import at.htl.remotecontrol.entity.StudentView;
import at.htl.remotecontrol.gui.Threader;
import at.htl.remotecontrol.server.TeacherServer;
import javafx.application.Platform;
import javafx.scene.control.TextField;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

import static org.mockito.Mockito.*;

/**
 * Philipp   :    01.12.2015  Erstellen des ersten Socket-Test
 */


public class MockTests {

    @Test
    public void t001_ImageShowTest() {
        // create mock

        // define return-value for .accept()


        /*Message text = new Message(emptyPayload) {
            @Override
            protected Socket createSocket() {
                return socket;
            }
        };*/
    }

}
