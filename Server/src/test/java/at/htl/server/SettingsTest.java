package at.htl.server;

import org.apache.commons.io.FileUtils;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

/**
 * @timeline .
 * 10.02.2016: PON 001  created test class
 * 10.02.2016: PON 030  added testAddStudentsFromCsv
 * 10.02.2016: PON 020  added init method
 * 10.02.2016: PON 010  test if directorys were created
 * 10.02.2016: PON 005  added deleteDirecotrys method
 * 10.02.2016: PON 010  test findStudentByName
 */

public class SettingsTest {

    public static Settings settings = Settings.getInstance();

    public static File dic;
    public static File ang;


    @BeforeClass
    public static void init() throws IOException {

            dic = new File(System.getProperty("java.io.tmpdir"));
            ang = FileUtils.getFile("src", "test", "resources","testFiles", "Angabe.zip");
            settings.setPath(dic.getPath());
            settings.setHandOutFile(ang);

    }

    @Test @Ignore
    public void testAddStudentsFromCsv() throws Exception {

        assertThat(settings.getStudentsList().get(0).getPupil().getLastName(), is("Forster"));
        assertThat(settings.getStudentsList().get(1).getPupil().getLastName(), is("Froschauer"));
        assertThat(settings.getStudentsList().get(10).getPupil().getLastName(), is("Krannich"));
        assertThat(settings.getStudentsList().get(20).getPupil().getLastName(), is("Tanzer"));

        File screenShotsFile = new File(dic.getPath() + "/Screenshots");

        String[] directories = screenShotsFile.list((current, name) -> new File(current, name).isDirectory());

        assertThat(directories[0], is("Forster"));
        assertThat(directories[1], is("Froschauer"));
        assertThat(directories[10], is("Krannich"));
        assertThat(directories[20], is("Tanzer"));
    }

    @AfterClass
    public static void deleteDirecotrys() throws IOException {
        FileUtils.deleteDirectory(dic);
        dic.mkdir();
    }

}