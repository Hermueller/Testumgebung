package at.htl.common;

import at.htl.common.fx.TextAnimation;
import javafx.animation.FadeTransition;
import javafx.animation.SequentialTransition;
import javafx.animation.Timeline;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

import java.time.Duration;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.TimeUnit;

/**
 * @timeline .
 * 10.03.2016: MET 001  created class
 * 10.03.2016: MET 030  Grundgerüst
 */
public class Countdown extends Thread {

    private final static int PAUSE = 1; // seconds

    private Text txCountdown;
    private LocalTime toTime;

    public Countdown(Text txCountdown, LocalTime toTime) {
        this.txCountdown = txCountdown;
        this.toTime = toTime;
    }

    public String getToTime() {
        Duration d = Duration.between(LocalTime.now(), toTime);
        System.out.println(d.toMillis());
        if (d.toMillis() < 1000) {
            this.interrupt();
        }
        return LocalTime.MIDNIGHT.plus(d).format(DateTimeFormatter.ofPattern("HH:mm:ss"));
    }

    @Override
    public void run() {
        while (!isInterrupted()) {
            try {
                /*Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        txCountdown.setText(getToTime());
                    }
                });*/
                sleep(TimeUnit.SECONDS.toMillis(PAUSE));
                System.out.println("Time: " + getToTime());
                txCountdown.setText(getToTime());
            } catch (InterruptedException e) {
                System.out.println("Clock beendet");
                e.printStackTrace();
            }
        }
        System.out.println("Clock beendet");
        txCountdown.setFill(Color.RED);

        Timeline blinker = TextAnimation.createBlinker(txCountdown);
        blinker.setOnFinished(event -> txCountdown.setText("Test closed!"));
        FadeTransition fade = TextAnimation.createFade(txCountdown);

        SequentialTransition blinkThenFade = new SequentialTransition(
                txCountdown,
                blinker,
                fade
        );
        blinkThenFade.play();
    }

    @Override
    public String toString() {
        return getToTime();
    }
}
