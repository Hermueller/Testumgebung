package at.htl.remotecontrol;

import at.htl.remotecontrol.gui.Threader;
import at.htl.remotecontrol.server.TeacherServer;
import org.junit.After;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.junit.Assert.*;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

/**
 * @timeline Text
 * 01.12.2015: PHI 020  Erstellen des ersten Socket-Test
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@RunWith(MockitoJUnitRunner.class)
public class MockTests {

    @Mock
    Threader threader;

    @Mock
    TeacherServer ts;


    @Before
    public void setUp() {
        TeacherServer.setPORT(50555);
        threader = new Threader() {
            @Override
            public boolean createTeacherServer(Socket socket) {
                return true;
            }
        };
    }

    @After
    public void tearDown() {
        threader = null;
        ts = null;
    }

    @Test
    public void t101_AcceptStudents() throws IOException {
        Thread thread = new Thread(threader);
        thread.start();

        Socket s1 = new Socket("localhost", 50555);
        Socket s2 = new Socket("localhost", 50555);
        Socket s3 = new Socket("localhost", 50555);
        Socket s4 = new Socket("localhost", 50555);
        Socket s5 = new Socket("localhost", 50555);
        Socket s6 = new Socket("localhost", 50555);
        Socket s7 = new Socket("localhost", 50555);
        Socket s8 = new Socket("localhost", 50555);
        Socket s9 = new Socket("localhost", 50555);
        Socket s10 = new Socket("localhost", 50555);
        Socket s11 = new Socket("localhost", 50555);
        Socket s12 = new Socket("localhost", 50555);
        Socket s13 = new Socket("localhost", 50555);
        Socket s14 = new Socket("localhost", 50555);
        Socket s15 = new Socket("localhost", 50555);
        Socket s16 = new Socket("localhost", 50555);
        Socket s17 = new Socket("localhost", 50555);
        Socket s18 = new Socket("localhost", 50555);
        Socket s19 = new Socket("localhost", 50555);
        Socket s20 = new Socket("localhost", 50555);
        Socket s21 = new Socket("localhost", 50555);
        Socket s22 = new Socket("localhost", 50555);
        Socket s23 = new Socket("localhost", 50555);
        Socket s24 = new Socket("localhost", 50555);
        Socket s25 = new Socket("localhost", 50555);
        Socket s26 = new Socket("localhost", 50555);
        Socket s27 = new Socket("localhost", 50555);
        Socket s28 = new Socket("localhost", 50555);
        Socket s29 = new Socket("localhost", 50555);
        Socket s30 = new Socket("localhost", 50555);

        thread.interrupt();

        s1.close();
        s2.close();
        s3.close();
        s4.close();
        s5.close();
        s6.close();
        s7.close();
        s8.close();
        s9.close();
        s10.close();
        s11.close();
        s12.close();
        s13.close();
        s14.close();
        s15.close();
        s16.close();
        s17.close();
        s18.close();
        s19.close();
        s20.close();
        s21.close();
        s22.close();
        s23.close();
        s24.close();
        s25.close();
        s26.close();
        s27.close();
        s28.close();
        s29.close();
        s30.close();
    }
}
