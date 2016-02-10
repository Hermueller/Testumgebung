package at.htl.remotecontrol.common.entity;

import org.apache.commons.io.FileUtils;
import org.junit.AfterClass;
import org.junit.BeforeClass;
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
 *
 */
public class SessionTest {

    public static Session session = Session.getInstance();

    public static File dic;
    public static File ang;


    @BeforeClass
    public static void init() throws IOException {
        dic = FileUtils.getFile("src","test", "resources", "Dirs");
        ang = FileUtils.getFile("src","test", "resources", "Angabe.zip");
        session.setPath(dic.getPath());
        session.addStudentsFromCsv(FileUtils.getFile("src", "test", "resources", "ListeSchueler4AHIF.csv"));
        session.setHandOutFile(ang);


    }

    @Test
    public void testFindStudentByName() throws Exception {
        String expected = "Forster";
        String actual = session.findStudentByName("Forster").getName();
        assertThat(actual,is(expected));
    }

    @Test
    public void testAddStudentsFromCsv() throws Exception {

        assertThat(session.getStudentsList().get(0).getName(), is("Forster"));
        assertThat(session.getStudentsList().get(1).getName(), is("Froschauer"));
        assertThat(session.getStudentsList().get(10).getName(), is("Krannich"));
        assertThat(session.getStudentsList().get(20).getName(), is("Tanzer"));

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