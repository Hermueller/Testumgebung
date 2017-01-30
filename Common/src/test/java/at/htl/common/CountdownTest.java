package at.htl.common;

import javafx.scene.text.Text;
import org.junit.Ignore;
import org.junit.Test;

import java.time.LocalTime;

/**
 * @timeline CountdownTest
 * 10.03.2016: MET 001  created class
 * 10.03.2016: MET 010  test
 * 17.03.2016: MET 005  test
 */
public class CountdownTest {

    @Test
    @Ignore
    public void t001SimpleCountdown() throws Exception {
        Text txCountdown = new Text();
        LocalTime toTime = LocalTime.now().plusMinutes(0).plusSeconds(5);
        Countdown cd = new Countdown(toTime, txCountdown);
        cd.setDaemon(true);
        cd.run();
    }

    @Test
    @Ignore
    public void t002RolloverPrompt() throws Exception {
        Text txCountdown = new Text();
        LocalTime toTime = LocalTime.now().plusMinutes(1).plusSeconds(10);
        Countdown cd = new Countdown(toTime, txCountdown);
        cd.setDaemon(true);
        cd.run();
    }
}