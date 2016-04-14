package at.htl.timemonitoring.common;

import javafx.scene.text.Text;

/**
 * Created by MET on 10.03.16.
 */
public class Timer extends Thread {

    private Text txTime;
    private int hour;
    private int minute;

    public Timer() {
    }

    public Timer(Text txTime, int hour, int minute) {
        this.txTime = txTime;
        this.hour = hour;
        this.minute = minute;
    }

    public String getTime() {
        return String.format("%d:%d", hour, minute);
    }

    @Override
    public String toString() {
        return getTime();
    }
}
