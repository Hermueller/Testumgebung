package at.htl.common;

import at.htl.common.fx.TextAnimation;
import javafx.animation.SequentialTransition;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

import java.time.Duration;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @timeline Countdown
 * 10.03.2016: MET 001  created class
 * 10.03.2016: MET 040  created basic structure for the countdown
 * 28.04.2016: MET 010  implemented function for resetting timer (incl. Design)
 * 12.05.2016: MET 060  Fixed bug: Timer no longer continues after stopping
 * 11.06.2016: MET 010  dynamic time display
 * 12.06.2016: MET 030  countdown with several Texts
 */
public class Countdown extends Thread {

    private final static int PAUSE = 1; // seconds

    private LocalTime toTime;
    private List<Text> texts;
    SequentialTransition blinkThenFade;

    public Countdown(LocalTime toTime) {
        texts = new LinkedList<>();
        this.toTime = toTime;
    }

    public Countdown(LocalTime toTime, Text text) {
        this(toTime);
        addText(text);
    }

    public void addText(Text text) {
        texts.add(text);
    }

    public void setToTime(LocalTime toTime) {
        this.toTime = toTime;
    }

    public String getTime() {
        Duration d = Duration.between(LocalTime.now(), toTime);
        if (d.toMillis() < 1000) {
            interrupt();
        }
        String pattern;
        if (d.getSeconds() / 60  >= 60) {
            pattern = "HH:mm:ss";
        } else if (d.getSeconds() >= 60) {
            pattern = "mm:ss";
        } else {
            pattern = "s";
        }
        return LocalTime.MIDNIGHT.plus(d).format(DateTimeFormatter.ofPattern(pattern));
    }

    @Override
    public void run() {
        while (!isInterrupted()) {
            try {
                sleep(TimeUnit.SECONDS.toMillis(PAUSE));
                for (Text text : texts) {
                    text.setText(getTime());
                }
            } catch (InterruptedException e) {
                System.out.println("Interrupted Thread.sleep");
                interrupt();
                return;
            }
        }
        setAfter();
    }

    private void setAfter() {
        for (Text text : texts) {
            text.setText("00:00:00");
            System.out.println("Clock beendet");
            text.setFill(Color.RED);
            blinkThenFade = new SequentialTransition(
                    text,
                    TextAnimation.createBlinker(text),
                    TextAnimation.createFade(text)
            );
            blinkThenFade.play();
        }
    }

    public void reset() {
        for (Text text : texts) {
            if (blinkThenFade != null) {
                blinkThenFade.stop();
            }
            text.setText("");
            text.setVisible(true);
            text.setFill(Color.BLACK);
        }
    }

    @Override
    public String toString() {
        return getTime();
    }
}
