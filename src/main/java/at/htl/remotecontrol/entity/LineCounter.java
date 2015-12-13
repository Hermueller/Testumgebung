package at.htl.remotecontrol.entity;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

/**
 * Philipp:  10.12.2015   053 Anlegen der Grundfunktionen und implementieren dieser in andere Klassen(z.B. SocketWriterThread)
 * Philipp:  11.12.2015   045 Errechnen von ALLEN zeilen im Projekt NUR im Startverzeichnis, das beim Login erstellt wurde.
 */
public class LineCounter {

    public LineCounter() {

    }

    /**
     * Counts the lines of code in a file
     *
     * @param _filename Specifies the file in which the lines have to be counted
     * @return  the number of lines in the file
     */
    public long countLines(String _filename) {
        long lines = 0;

        try {
            try (BufferedReader br = new BufferedReader(new FileReader(_filename))) {
                lines = br.lines().count();
            }
        } catch (Exception exc) {
            System.out.println("File konnte nicht gelesen werden!!");
        }

        return lines;
    }

    /**
     * Counts the line of code in every file of the root directory
     *
     * @param folder    Specifies the directory in which the files to count
     *                  are located.
     * @return  the number of lines from all files in the directory
     */
    public long listFilesForFolder(final File folder) {
        long allLines = 0;

        for (final File fileEntry : folder.listFiles()) {
            if (fileEntry.isDirectory()) {
                allLines += listFilesForFolder(fileEntry);
            } else {
                if (fileEntry.getName().endsWith(".java")) {
                    allLines += countLines(fileEntry.getPath());
                }
            }
        }

        return allLines;
    }
}