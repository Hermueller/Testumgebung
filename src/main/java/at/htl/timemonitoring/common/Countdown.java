package at.htl.timemonitoring.common;

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
            } catch (InterruptedException e) {
                System.out.println("Clock beendet");
                e.printStackTrace();
            }
        }
        System.out.println("Clock beendet");
    }

    @Override
    public String toString() {
        return getToTime();
    }
}
