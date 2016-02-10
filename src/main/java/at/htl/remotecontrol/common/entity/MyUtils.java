package at.htl.remotecontrol.common.entity;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;

/**
 * @timeline .
 * 29.02.2016: GNA 001  created class
 * 29.02.2016: GNA 005  implementation of exception converter
 */
public class MyUtils {

    public static String convert(Exception e) {
        Writer writer = new StringWriter();
        e.printStackTrace(new PrintWriter(writer));
        return writer.toString();
    }
}
