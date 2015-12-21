package at.htl.remotecontrol.entity;

/**
 * @timeline Text
 * 30.10.2015: MET ???  Klasse für Zeitspanne zwischen Screenshots erstellt
 */
public class Interval {

    private long value;
    private long min;
    private long max;
    private boolean random;

    /**
     * FIX interval between screenshots.
     *
     * @param value the fix time between the screenshots.
     */
    public Interval(long value) {
        this.value = value;
        this.random = false;
    }

    /**
     * RANDOM interval between screenshots.
     *
     * @param min the minimum time interval between screenshots.
     * @param max the maximum time interval between screenshots.
     */
    public Interval(long min, long max) {
        this.min = min;
        this.max = max;
        this.random = true;
    }

    //region Getter And Setter

    /**
     * how much time to the next screenshot.
     *
     * @return the time to wait for the next screenshot.
     */
    public long getValue() {
        if (isRandom())
            return (int) (Math.random() * ((max - min) + 1)) + min;
        return value;
    }

    public boolean isRandom() {
        return random;
    }

    //endregion

}
