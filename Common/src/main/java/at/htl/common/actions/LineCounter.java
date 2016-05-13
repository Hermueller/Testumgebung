package at.htl.common.actions;

import at.htl.common.MyUtils;
import at.htl.common.io.FileUtils;
import org.apache.logging.log4j.Level;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

/**
 * @timeline .
 * 10.12.2015: PHI 001  created class
 * 10.12.2015: PHI 055  Anlegen der Grundfunktionen und implementieren dieser in andere Klassen(z.B. SocketWriterThread)
 * 11.12.2015: PHI 045  Errechnen von ALLEN zeilen im Projekt NUR im Startverzeichnis, das beim Login erstellt wurde.
 * 21.04.2016: PHI 010  changed this class to a Singleton and added the variable "finished"
 * 07.05.2016: PHI 015  added new method (remembers how many lines for each filter)
 */

/**
 * counts the lines in a file for each file in a directory.
 *
 * @author Philipp Hermueller
 */
public class LineCounter {

    private static LineCounter instance = null;

    private boolean finished = false;

    private LineCounter() {

    }

    public static LineCounter getInstance() {
        if (instance == null) {
            instance = new LineCounter();
        }
        return instance;
    }

    /**
     *
     * @return  boolean (finished the student the test?)
     */
    public boolean isFinished() {
        return finished;
    }

    /**
     *
     * @param finished  The boolean which shows if the student finished the test.
     */
    public void setFinished(boolean finished) {
        this.finished = finished;
    }

    /**
     * Counts the lines of code in a file.
     *
     * @param _filename Specifies the file in which the lines have to be counted
     * @return          the number of lines in the file
     */
    public long countLines(String _filename) {
        long lines = 0;

        try {
            try (BufferedReader br = new BufferedReader(new FileReader(_filename))) {
                lines = br.lines().count();
            }
        } catch (Exception exc) {
            FileUtils.log(this, Level.ERROR,"File konnte nicht gelesen werden!!"+ MyUtils.exToStr(exc));
        }

        return lines;
    }

    /**
     * RECURSIVE
     * <p>
     * Counts the line of code in every file of the root directory
     *
     * @param folder Specifies the directory in which the files to count
     *               are located.
     * @param filter file extension name.
     * @return       The number of lines from all files in the directory
     *
     * @since 1.2.9.002
     */
    public long countLinesInFilesFromFolder(final File folder, String filter) {
        long allLines = 0;

        for (final File fileEntry : folder.listFiles()) {
            if (fileEntry.isDirectory()) {
                allLines += countLinesInFilesFromFolder(fileEntry, filter);
            } else {
                if (fileEntry.getName().endsWith("." + filter.split("\\.")[1])) {
                    allLines += countLines(fileEntry.getPath());
                }
            }
        }

        return allLines;
    }

    /**
     * counts the lines in a file with a specific file extension.
     *
     * @param folder    the root folder which includes all files to count in.
     * @param filter    Specialises the file-extension-names.
     * @return          The Array which includes the number of lines for each file-extension.
     */
    public Long[] countLinesWithFilter(final File folder, String[] filter) {
        Long[] lines = new Long[filter.length];

        for (int i = 0; i < filter.length; i++) {
            lines[i] = countLinesInFilesFromFolder(folder, filter[i]);
        }

        return lines;
    }
}