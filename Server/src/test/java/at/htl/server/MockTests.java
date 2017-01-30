package at.htl.server;

import org.junit.*;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.IOException;
import java.net.Socket;

/**
 * @timeline Text
 * 23.12.2015: PHI 020  Erstellen des ersten Socket-Test
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@RunWith(MockitoJUnitRunner.class)
public class MockTests {

    @Mock
    Threader threader;

    @Mock
    Server ts;


    @Before
    public void setUp() {
        Server.setPORT(50555);
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
    @Ignore
    public void t101_AcceptStudents() throws IOException {

    }
}
