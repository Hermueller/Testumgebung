package at.htl.remotecontrol.common;

import at.htl.remotecontrol.common.io.FileUtils;
import at.htl.remotecontrol.common.io.ScreenShot;
import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

/**
 * @timeline .
 * 14.11.2015: MET 001  created test class
 * 14.11.2015: MET 010  test: image format, validate suffix
 * 14.11.2015: MET 005  test: create and delete screenshots
 */
public class ScreenShotTest {

    @Test
    public void t001ImageFormat() {
        ScreenShot.Format format = ScreenShot.Format.JPG;
        assertThat(format.toString(), is("jpg"));
        format = ScreenShot.Format.PNG;
        assertThat(format.toString(), is("png"));
    }

    @Test
    public void t005ValidSuffix() {
        String fileName = "/opt/test.1.png";
        assertThat(ScreenShot.validSuffix(fileName), is(true));
        fileName = "/opt/readme.txt";
        assertFalse(ScreenShot.validSuffix(fileName));
    }

    @Test
    public void t006Screenshot() throws Exception {
        byte[] screenShot = ScreenShot.get();
        String fileName = "src/test/resources/test.jpg";
        ScreenShot.save(screenShot, fileName);
        assertTrue(FileUtils.exists(fileName));
        FileUtils.delete(fileName);
        assertFalse(FileUtils.exists(fileName));
    }

}