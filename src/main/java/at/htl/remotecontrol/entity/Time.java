package at.htl.remotecontrol.entity;

/**
 * Gnadlinger:  15.Oktober.2015  Verwaltung der Gui-Eingabewerte inplementiert
 *
 *
 */
public class Time {
    public static Time instance = null;
    private static long time = 3000;

    public static Time getInstance() {
        if (instance == null) {
            instance = new Time();
        }
        return instance;
    }

    public static void setTime(long newtime) {
        time=newtime;
    }

    public long getTime() {
        return time;
    }
}
