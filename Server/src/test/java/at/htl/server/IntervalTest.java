package at.htl.server;

import at.htl.server.entity.Interval;
import org.junit.Ignore;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * @timeline .
 * 14.11.2015: MET 001  created test class
 * 14.11.2015: MET 010  test: fix value, random value, factor
 */
public class IntervalTest {

    @Test @Ignore
    public void t001FixValue() throws Exception {
        Interval a = new Interval(20);
        assertThat(a.getValue(), is(20L));
        assertFalse(a.isRandom());
        int fixValue = 30;
        Interval b = new Interval(fixValue);
        assertThat(b.getValue(), is((long) fixValue));
        assertFalse(b.isRandom());
    }

    @Test @Ignore
    public void t002RandomValue() throws Exception {
        int min = 100;
        int max = 500;
        Interval a = new Interval(min, max);
        assertTrue(a.isRandom());
        for (int i = 1; i <= 10; i++) {
            System.out.println(a.toString());
        }
    }

    @Test @Ignore
    public void t003Factor() throws Exception {
        int fixValue = 50;
        Interval a = new Interval(fixValue);
        assertThat(a.getFactor(), is(1L));
        long kilo = 1000;
        a.setFactor(kilo);
        assertThat(a.getFactor(), is(kilo));
        assertThat(a.getValue(), is(fixValue * kilo));
        System.out.println(a.toString());
        System.out.println("factor = " + a.getFactor());
    }

}
