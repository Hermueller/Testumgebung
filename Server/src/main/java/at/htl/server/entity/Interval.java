package at.htl.server.entity;

/**
 * @timeline .
 * 30.10.2015: MET 005  created class for time between screenshots
 * 20.11.2015: MET 005  extended by toString() and a factor for getValue()
 */

/**
 * @author Tobias Melhorn
 */
public class Interval {

    private long value;
    private long min;
    private long max;
    private boolean random;
    private long factor;

    private Interval(boolean random) {
        this.random = random;
        this.factor = 1;
    }

    /**
     * creates new interval with static value.
     *
     * @param value    The seconds between every harvest.
     */
    public Interval(long value) {
        this(false);
        this.value = value;
    }

    /**
     * creates new interval with dynamic values.
     *
     * @param min   The minimum time between harvests (in sec.)
     * @param max   The maximum time between harvests (in sec.)
     */
    public Interval(long min, long max) {
        this(true);
        this.min = min;
        this.max = max;
    }

    //region Getter and Setter

    /**
     * @return fixed value or random value between min and max
     */
    public long getValue() {
        return (random ?
                (int) (Math.random() * ((max - min) + 1)) + min
                : value)
                * factor;
    }

    public boolean isRandom() {
        return random;
    }

    public long getFactor() {
        return factor;
    }

    public void setFactor(long factor) {
        this.factor = factor;
    }
    //endregion

    @Override
    public String toString() {
        return "value = " + getValue();
    }

}
