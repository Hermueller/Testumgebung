package at.htl.common;

import at.htl.common.io.FileUtils;
import at.htl.common.io.ScreenShot;
import org.junit.Ignore;
import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

/**
 * @timeline ScreenShotTest
 * 14.11.2015: MET 001  created test class
 * 14.11.2015: MET 010  test: image format, validate suffix
 * 14.11.2015: MET 005  test: create and delete screenshots
 */
public class ScreenShotTest {

    @Test @Ignore
    public void t001ImageFormat() {
        ScreenShot.Format format = ScreenShot.Format.JPG;
        assertThat(format.toString(), is("jpg"));
        format = ScreenShot.Format.PNG;
        assertThat(format.toString(), is("png"));
    }

    @Test @Ignore
    public void t005ValidSuffix() {
        ScreenShot screenShot = new ScreenShot();
        String fileName = "/opt/test.1.png";
        assertThat(screenShot.validSuffix(fileName), is(true));
        fileName = "/opt/readme.txt";
        assertFalse(screenShot.validSuffix(fileName));
    }

    @Test @Ignore
    public void t006Screenshot() throws Exception {
        ScreenShot scrSh = new ScreenShot();
        byte[] screenShot = scrSh.get();
        String fileName = "src/test/resources/test.jpg";
        scrSh.save(screenShot, fileName);
        assertTrue(FileUtils.exists(fileName));
        FileUtils.delete(fileName);
        assertFalse(FileUtils.exists(fileName));
    }

}