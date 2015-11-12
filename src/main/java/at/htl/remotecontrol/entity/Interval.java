package at.htl.remotecontrol.entity;

/**
 * 30.10.2015:  Tobias      Klasse für Zeitspanne zwischen Screenshots erstellt
 */
public class Interval {

    private long value;
    private long min;
    private long max;
    private boolean random;

    public Interval(long value) {
        this.value = value;
        this.random = false;
    }

    public Interval(long min, long max) {
        this.min = min;
        this.max = max;
        this.random = true;
    }

    //region Getter And Setter
    public long getValue() {
        if (random)
            return (int) (Math.random() * ((max - min) + 1)) + min;
        return value;
    }

    public boolean isRandom() {
        return random;
    }
    //endregion

}
