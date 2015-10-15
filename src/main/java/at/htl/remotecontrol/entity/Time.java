package at.htl.remotecontrol.entity;

/**
 * Created by gnadlinger on 14.10.15.
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
        System.out.println(newtime);
        time=newtime;
    }

    public long getTime() {
        System.out.println("get: " + time);
        return time;
    }
}
