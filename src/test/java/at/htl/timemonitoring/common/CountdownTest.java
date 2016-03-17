package at.htl.timemonitoring.common;

import javafx.scene.text.Text;
import org.junit.Test;

import java.time.LocalTime;

/**
 * @timeline .
 * 10.03.2016: MET 001  created class
 * 10.03.2016: MET 010
 * 17.03.2015: MET 005
 */
public class CountdownTest {

    @Test
    public void t001SimpleCountdown() throws Exception {
        Text txCountdown = new Text();
        LocalTime toTime = LocalTime.now().plusMinutes(0).plusSeconds(3);
        Countdown cd = new Countdown(txCountdown, toTime);
        cd.setDaemon(true);
        cd.run();
    }

    @Test
    public void t002RolloverPrompt() throws Exception {
        Text txCountdown = new Text();
        LocalTime toTime = LocalTime.now().plusMinutes(1).plusSeconds(10);
        Countdown cd = new Countdown(txCountdown, toTime);
        cd.setDaemon(true);
        cd.run();
    }
}