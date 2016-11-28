package at.htl.common;

import at.htl.common.io.FileUtils;
import at.htl.common.io.ScreenShot;
import org.junit.FixMethodOrder;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

/**
 * @timeline ScreenShotTest
 * 14.11.2015: MET 001  created test class
 * 14.11.2015: MET 010  test: image format, validate suffix
 * 14.11.2015: MET 005  test: create and delete screenshots
 * 28.11.2016: PHI 030  test: the PNG und JPG format of a screenshot (test3-4)
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ScreenShotTest {

    @Test
    public void t001ImageFormat() {
        ScreenShot.Format format = ScreenShot.Format.JPG;
        assertThat(format.toString(), is("jpg"));
        format = ScreenShot.Format.PNG;
        assertThat(format.toString(), is("png"));
    }

    @Test
    public void t002ValidSuffix() {
        ScreenShot screenShot = new ScreenShot();
        String fileName = "/opt/test.1.png";
        assertThat(screenShot.validSuffix(fileName), is(true));
        fileName = "/opt/readme.txt";
        assertFalse(screenShot.validSuffix(fileName));
    }

    @Test
    public void t003ScreenshotWithFormatJPG() throws Exception {
        ScreenShot scrSh = new ScreenShot();
        scrSh.setFormat(ScreenShot.Format.JPG);
        byte[] screenShot = scrSh.get();
        String fileName = "scrTestJ.jpg";
        scrSh.save(screenShot, fileName);
        assertTrue("Couldn't find the screenshot", FileUtils.exists(fileName));
        boolean success = FileUtils.delete(fileName);
        assertFalse("Can't delete the screenshot", FileUtils.exists(fileName));
        if (success) {
            System.out.println("A screenshot of the format JPG can be created!");
        }
    }

    @Test
    public void t004ScreenshotWithFormatPNG() throws Exception {
        ScreenShot scrSh = new ScreenShot();
        scrSh.setFormat(ScreenShot.Format.PNG);
        byte[] screenShot = scrSh.get();
        String fileName = "scrTestP.jpg";
        scrSh.save(screenShot, fileName);
        assertTrue("Couldn't find the screenshot", FileUtils.exists(fileName));
        boolean success = FileUtils.delete(fileName);
        assertFalse("Can't delete the screenshot", FileUtils.exists(fileName));
        if (success) {
            System.out.println("A screenshot of the format PNG can be created!");
        }
    }
}