package at.htl.remotecontrol.common.entity;

import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Test;

import java.io.File;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

/**
 * @timeline .
 * 10.02.2016: PON 001  created test class
 * 10.02.2016: PON 030  added testAddStudentsFromCsv
 */
public class SessionTest {

    Session session = Session.getInstance();

    @Before
    public void init() {
        final File dic = FileUtils.getFile("src","test", "resources", "Dirs");
        session.setPath(dic.getPath());
    }

    @Test
    public void testGetObservableList() throws Exception {

    }

    @Test
    public void testGetHandOutFile() throws Exception {

    }

    @Test
    public void testFindStudentByName() throws Exception {

    }

    @Test
    public void testAddStudentsFromCsv() throws Exception {
        session.addStudentsFromCsv(FileUtils.getFile("src", "test", "resources", "ListeSchueler4AHIF.csv"));

        assertThat(session.getStudentsList().get(0).getName(), is("Forster"));
        assertThat(session.getStudentsList().get(1).getName(), is("Froschauer"));
        assertThat(session.getStudentsList().get(10).getName(), is("Krannich"));
        assertThat(session.getStudentsList().get(20).getName(), is("Tanzer"));


    }
}