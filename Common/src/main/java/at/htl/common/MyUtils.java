package at.htl.common;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;

/**
 * @timeline MyUtils
 * 29.01.2016: GNA 001  created class
 * 29.01.2016: GNA 005  implementation of exception converter
 * 12.02.2016: MET 003  function: converting string to int
 * 12.02.2016: MET 003  provided functions with comments: strToInt(), exToStr()
 * 31.05.2016: STU 003  changed Integer.valueOf() to Integer.parseInt()
 */
public class MyUtils {

    /**
     * Converts string to int
     *
     * @param s to be converted string
     * @return number
     */
    public static int strToInt(String s) {
        try {
            return Integer.parseInt(s);
        } catch (Exception e) {
            return 0;
        }
    }

    /**
     * Converts an exception to string
     *
     * @param e exception
     * @return exception as  string format
     */
    public static String exToStr(Exception e) {
        // TODO Warum wird hier nicht einfach e.getMessage() verwendet, um einen String zu erhalten?
        Writer writer = new StringWriter();
        e.printStackTrace(new PrintWriter(writer));
        return writer.toString();
    }
}
