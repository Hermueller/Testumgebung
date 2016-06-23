package at.htl.common.io;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.LinkedList;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

/**
 * @timeline FileUtils
 * 29.10.2015: GNA 020  creating files and directories
 * 30.10.2015: MET 001  created class
 * 30.10.2015: MET 045  implementation of zipping files and directories
 * 31.10.2015: MET 005  Function "creating files" provided with messages
 * 31.10.2015: MET 030  corrected zipping folders and files and provided with messages
 * 31.10.2015: MET 025  delete one or more folders
 * 01.11.2015: MET 015  improved deleting directories and provided with messages
 * 01.11.2015: MET 005  Bug found: deleting files nonfunctional
 * 14.11.2015: MET 040  improving the method zip() and implementation of recursive zipping
 * 14.11.2015: MET 010  corrected deleting files or directories
 * 15.11.2015: MET 020  extended by createFile(fileName) and exists(fileName)
 * 15.11.2015: MET 070  Implementation of method unzip(...)
 * 02.01.2016: MET 010  added a function which saves byte arrays
 * 08.02.2016: MET 010  logger
 * 08.02.2016: MET 010  added logs
 * 15.03.2016: PHI 005  showing the log on the application
 * 18.03.2016: PHI 080  the log int the application is now in colors
 * 21.06.2016: MET 005  function for getting the file extension
 */
public class FileUtils {

    /**
     * creates a empty directory
     *
     * @param fileName path of directory
     * @return true, if a successful deletion, else false
     */
    public static boolean createDirectory(String fileName) {
        boolean created = false;
        File file = new File(fileName);
        if (file.exists()) {
            log(Level.WARN, String.format("Directory %s already exists!", fileName));
        } else if (!file.mkdir()) {
            log(Level.ERROR, String.format("Directory %s failed to create!", fileName));
        } else {
            log(Level.INFO, "created directory " + fileName);
            created = true;
        }
        return created;
    }

    /**
     * creates a empty
     *
     * @param fileName path of file
     * @return true, if a successful deletion, else false
     */
    public static boolean createFile(String fileName) {
        boolean created = false;
        File file = new File(fileName);
        if (file.exists()) {
            log(Level.WARN, String.format("File %s already exists!", fileName));
        } else try {
            if (file.createNewFile()) {
                log(Level.INFO, "created file " + fileName);
                created = true;
            }
        } catch (IOException e) {
            log(Level.ERROR, String.format("File %s failed to create!", fileName));
            e.printStackTrace();
        }
        return created;
    }

    /**
     * stores a byte array as a file on the hard disk
     *
     * @param file     to be saved file
     * @param fileName path with filename (e.g. .../directory/file.pdf)
     * @return true, if a successful deletion, else false
     */
    public static boolean saveAsFile(byte[] file, String fileName) {
        boolean saved = false;
        try {
            new FileOutputStream(fileName).write(file);
            saved = true;
        } catch (IOException e) {
            log(Level.ERROR, "Failed to save as file!");
        }
        return saved;
    }

    /**
     * checks whether a file or a directory exists
     *
     * @param fileName path of a file or directory
     * @return true, if file exists
     */
    public static boolean exists(String fileName) {
        return new File(fileName).exists();
    }

    /**
     * returns the extension of a file
     *
     * @param file file
     * @return file extension
     */
    public static String getFileExtension(File file) {
        String[] strings = file.getName().split("\\.");
        return strings[strings.length - 1];
    }

    /**
     * zips directories and files
     *
     * @param fileName    path of source file or directory
     * @param zipFileName path of zip archive
     * @return true, if a successful deletion, else false
     */
    public static boolean zip(String fileName, String zipFileName) {
        if (fileName.contains(".zip")) {
            log(Level.WARN, String.format("%s is already zipped!", fileName));
            return true;
        }
        boolean created = false;
        File file = new File(fileName);
        if (!file.exists()) {
            log(Level.ERROR, fileName + " does not exist!");
        } else if (!zipFileName.contains(".zip")) {
            log(Level.ERROR, zipFileName + " is a invalid filename of an zip archive!");
        } else {
            log(Level.INFO, String.format("creating zip file %s ...", zipFileName));
            try {
                ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(new File(zipFileName)));
                wrapRecursive(zos, file, fileName);
                zos.close();
                log(Level.INFO, "finished creating zip archive " + zipFileName);
                created = true;
            } catch (IOException e) {
                log(Level.ERROR, "error by zipping " + fileName);
                e.printStackTrace();
            }
        }
        return created;
    }

    /**
     * creates a zip archive recursively (files and directories)
     * Warning: empty folders can not be zipped
     *
     * @param zos      ZipOutputStream
     * @param file     to be zipped file
     * @param fileName path of zip file
     */
    private static void wrapRecursive(
            ZipOutputStream zos, File file, String fileName) throws IOException {
        int length;
        byte[] buffer = new byte[8192];
        File[] files = file.listFiles();
        for (File f : files == null ? new File[0] : files) {
            if (f.isDirectory()) {
                wrapRecursive(zos, f, fileName);
            } else {
                FileInputStream fis = new FileInputStream(f.getPath());
                zos.putNextEntry(new ZipEntry(f.getPath().replace(fileName, "")));
                while ((length = fis.read(buffer)) > 0)
                    zos.write(buffer, 0, length);
                zos.closeEntry();
                fis.close();
                log(Level.INFO, String.format("  %s added", f.getPath()));
            }
        }
    }

    /**
     * unzips files and directories
     *
     * @param zipFileName path of zip archive
     * @param fileName    path of target directory
     * @return successful
     */
    public static boolean unzip(String zipFileName, String fileName) {
        boolean finished = false;
        if (!zipFileName.contains(".zip")) {
            log(Level.ERROR, fileName + " can not be unzipped!");
        } else if (new File(fileName).exists()) {
            log(Level.ERROR, String.format("Directory %s already exists!", fileName));
        } else {
            createDirectory(fileName);
            int length;
            byte[] buffer = new byte[8192];
            try {
                ZipInputStream zis = new ZipInputStream(new FileInputStream(zipFileName));
                ZipEntry ze = zis.getNextEntry();
                while (ze != null) {
                    File newFile = new File(fileName + File.separator + ze.getName());
                    log(Level.INFO, "Unzipping to " + newFile.getAbsolutePath());
                    createDirectory(newFile.getParent());
                    FileOutputStream fos = new FileOutputStream(newFile);
                    while ((length = zis.read(buffer)) > 0)
                        fos.write(buffer, 0, length);
                    fos.close();
                    zis.closeEntry();
                    ze = zis.getNextEntry();
                }
                zis.closeEntry();
                zis.close();
                log(Level.INFO, "finished unzipping " + zipFileName);
                finished = true;
            } catch (IOException e) {
                log(Level.ERROR, "error by unzipping " + zipFileName);
                e.printStackTrace();
            }
        }
        return finished;
    }


    /**
     * deletes multiple paths
     *
     * @param paths list of paths to delete
     * @return true, if a successful deletion, else false
     */
    @SuppressWarnings("unused")
    public static boolean deleteAll(LinkedList<String> paths) {
        boolean error = false;
        for (String path : paths)
            if (!delete(path))
                error = true;
        if (error) {
            log(Level.ERROR, " not all paths are deleted");
            return false;
        } else {
            log(Level.INFO, " given paths are deleted");
            return true;
        }
    }

    /**
     * deletes files and directories
     *
     * @param fileName path of deleting
     * @return true, if a successful deletion, else false
     */
    public static boolean delete(String fileName) {
        boolean deleted = false;
        File file = new File(fileName);
        if (!file.exists()) {
            log(Level.ERROR, fileName + " does not exist!");
        } else {
            File[] files = file.listFiles();
            for (File f : files == null ? new File[0] : files)
                delete(f.getPath());
            if (file.delete()) {
                log(Level.INFO, file.getPath() + " deleted");
                deleted = true;
            } else {
                log(Level.ERROR, file.getPath() + " not deleted");
            }
        }
        return deleted;
    }

    /**
     * creates the at.htl.client.styles for the error/warning/info.
     * <p/>
     * ERROR's in RED.<br>
     * WARNING's in YELLOW.<br>
     * INFO's in WHITE.
     *
     * @param level Specifies the level of the error.
     * @return the at.htl.client.styles for the textfield.
     */
    public static String getStyle(Level level) {
        String styleString = "-fx-background-color: transparent;";
        if (level == Level.ERROR) {
            styleString += "-fx-text-fill: crimson";
        } else if (level == Level.WARN) {
            styleString += "-fx-text-fill: yellow";
        } else if (level == Level.INFO) {
            styleString += "-fx-text-fill: white";
        } else if (level == Level.OFF) {
            styleString = "-fx-background-color: white;";
        }
        return styleString;
    }

    /**
     * @param level   is the level the message (ERROR, INFO, WARN)
     * @param message is the text to show
     */
    public static void log(Level level, String message) {
        LogManager.getLogger().log(level, message);
    }

    /**
     * @param obj     object
     * @param level   is the level the message (ERROR, INFO, WARN)
     * @param message is the text to show
     */
    public static void log(Object obj, Level level, String message) {
        LogManager.getLogger(obj.getClass()).log(level, message);
    }
}
