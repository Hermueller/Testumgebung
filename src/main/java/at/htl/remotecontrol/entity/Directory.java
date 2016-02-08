package at.htl.remotecontrol.entity;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.LinkedList;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * @timeline .
 * 29.10.2015: GNA 020  creating files and directories
 * 30.10.2015: MET 001  created class
 * 30.10.2015: MET 045  implementation of zipping files and directories
 * 31.10.2015: MET 005  Function "creating files" provided with messages
 * 31.10.2015: MET 030  corrected zipping folders and files and provided with messages
 * 31.10.2015: MET 025  delete one or more folders
 * 01.11.2015: MET 015  improved deleting directories and provided with messages
 * 01.11.2015: MET 005  Bug found: deleting files nonfunctional
 * 14.11.2015: MET 040  improving the method zip() and implementation of recursive zipping
 */
public class Directory {

    /**
     * create a directory on the computer/laptop.
     *
     * @param path Specifies the name of the directory and
     *             the location of the directory.
     * @return the success of it.
     */
    public static boolean create(String path) {
        boolean created = false;
        File dir = new File(path);
        if (dir.exists()) {
            System.out.println(String.format("Directory %s already exists!", path));
        } else if (!dir.mkdir()) {
            System.out.println(String.format("Directory %s can't be created!", path));
        } else {
            created = true;
        }
        return created;
    }

    /**
     * zips directories and files
     *
     * @param fileName    path of source file or directory
     * @param zipFileName path of zip archive
     * @return successful
     */
    public static boolean zip(String fileName, String zipFileName) {
        if (fileName.contains(".zip")) {
            System.out.println(String.format("%s is already zipped!", fileName));
            return true;
        }
        boolean created = false;
        File file = new File(fileName);
        if (!file.exists()) {
            System.out.println(fileName + " not exist!");
        } else if (!zipFileName.contains(".zip")) {
            System.out.println(zipFileName + " is a invalid filename of an zip archive!");
        } else {
            System.out.println(String.format("creating zip file %s ...", zipFileName));
            try {
                ZipOutputStream zos = new ZipOutputStream(
                        new FileOutputStream(new File(zipFileName)));
                wrapRecursive(zos, file, fileName);
                zos.close();
                System.out.println("finished creating zip archive " + zipFileName);
                created = true;
            } catch (IOException e) {
                System.out.println("error by zipping " + fileName);
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
                System.out.println(String.format("  %s added", f.getPath()));
            }
        }
    }

    /**
     * gets all Files in a directory.
     *
     * @param dir Specifies the directory of the files.
     * @return all files in the directory.
     */
    private static LinkedList<File> getAllFiles(File dir) {
        LinkedList<File> allFiles = new LinkedList<File>();
        File[] files = dir.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    allFiles.addAll(getAllFiles(file));
                } else {
                    allFiles.add(file);
                }
            }
        }
        return allFiles;
    }

    /**
     * deletes all files from the list
     *
     * @param paths Specifies the list of the files to delete
     * @return the success of it
     */
    public static boolean deleteAll(LinkedList<String> paths) {
        boolean error = false;
        for (String path : paths) {
            if (!delete(path))
                error = true;
        }
        if (error) {
            System.out.println("not all paths are deleted");
            return false;
        } else {
            System.out.println("given paths are deleted");
            return true;
        }
    }

    /**
     * delete a file.
     *
     * @param path Specifies the file to delete
     * @return the success of it
     */
    public static boolean delete(String path) {
        boolean deleted = false;
        File[] files = new File(path).listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory() && !delete(file.getAbsolutePath())) {
                    System.out.println(file.getPath() + " not deleted");
                } else if (!file.delete()) {
                    System.out.println(file.getPath() + " not deleted");
                } else {
                    System.out.println(file.getPath() + " deleted");
                    deleted = true;
                }
            }
        }
        return deleted;
    }

}
