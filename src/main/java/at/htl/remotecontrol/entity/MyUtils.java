package at.htl.remotecontrol.entity;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;

/**
 * Created by Gnadlinger on 08.02.2016.
 */
public class MyUtils {

    public static String convert(Exception e){
        Writer writer = new StringWriter();
        e.printStackTrace(new PrintWriter(writer));
        return writer.toString();
    }
}
