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

    public boolean isFinished() {
        return finished;
    }

    public void setFinished(boolean finished) {
        this.finished = finished;
    }

    /**
     * Counts the lines of code in a file
     *
     * @param _filename Specifies the file in which the lines have to be counted
     * @return the number of lines in the file
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
     * @return the number of lines from all files in the directory
     *
     * @since 1.2.9.002
     */
    public long countLinesInFilesFromFolder(final File folder) {
        long allLines = 0;

        for (final File fileEntry : folder.listFiles()) {
            if (fileEntry.isDirectory()) {
                allLines += countLinesInFilesFromFolder(fileEntry);
            } else {
                for (String ending : "*.java; *.fxml; *.cs; *.xhtml; *.html".split(";")) {
                    if (fileEntry.getName().endsWith("." + ending.split("\\.")[1])) {
                        allLines += countLines(fileEntry.getPath());
                    }
                }
            }
        }

        return allLines;
    }
}