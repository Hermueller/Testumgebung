package at.htl.common;

import at.htl.common.fx.TextAnimation;
import javafx.animation.SequentialTransition;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

import java.time.Duration;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.TimeUnit;

/**
 * @timeline .
 * 10.03.2016: MET 001  created class
 * 10.03.2016: MET 040  created basic structure for the countdown
 * 28.04.2016: MET 010  implemented function for resetting timer (incl. Design)
 * 12.05.2016: MET 060  Fixed bug: Timer no longer continues after stopping
 */
public class Countdown extends Thread {

    private final static int PAUSE = 1; // seconds

    private Text txCountdown;
    private LocalTime toTime;
    SequentialTransition blinkThenFade;

    public Countdown(Text txCountdown, LocalTime toTime) {
        this.txCountdown = txCountdown;
        this.toTime = toTime;
    }

    public void setToTime(LocalTime toTime) {
        this.toTime = toTime;
    }

    public String getTime() {
        Duration d = Duration.between(LocalTime.now(), toTime);
        if (d.toMillis() < 1000) {
            interrupt();
        }
        return LocalTime.MIDNIGHT.plus(d).format(DateTimeFormatter.ofPattern("HH:mm:ss"));
    }

    @Override
    public void run() {
        while (!isInterrupted()) {
            try {
                sleep(TimeUnit.SECONDS.toMillis(PAUSE));
                txCountdown.setText(getTime());
            } catch (InterruptedException e) {
                System.out.println("Interrupted Thread.sleep");
                interrupt();
                return;
            }
        }
        System.out.println("Clock beendet");
        txCountdown.setFill(Color.RED);
        blinkThenFade = new SequentialTransition(
                txCountdown,
                TextAnimation.createBlinker(txCountdown),
                TextAnimation.createFade(txCountdown)
        );
        blinkThenFade.play();
    }

    public void reset() {
        if (blinkThenFade != null) {
            blinkThenFade.stop();
        }
        txCountdown.setText("");
        txCountdown.setVisible(true);
        txCountdown.setFill(Color.BLACK);
    }

    @Override
    public String toString() {
        return getTime();
    }
}
