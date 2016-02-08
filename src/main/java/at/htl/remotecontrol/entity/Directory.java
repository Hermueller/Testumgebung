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
     * compromises a directory and saves it on the same location.
     *
     * @param path        Specifies the directory to compromise.
     * @param zipFileName Specifies the name of the compromised directory.
     * @return the success of it.
     */
    public static boolean zip(String path, String zipFileName) {
        if (path.contains(".zip")) {
            System.out.println(String.format("Directory %s is already zipped!", path));
            return true;
        }
        boolean created = false;
        File dir = new File(path);
        if (!dir.isDirectory()) {
            System.out.println(path + " is a invalid directory");
        } else if (!zipFileName.contains(".zip")) {
            System.out.println(zipFileName + " is a invalid filename of an zip archive!");
        } else {
            System.out.println(String.format("create zip file %s ...", zipFileName));
            byte[] buffer = new byte[8192];
            try {
                LinkedList<File> files = getAllFiles(dir);
                File zipFile = new File(String.format("%s/%s", path, zipFileName));
                FileOutputStream fos = new FileOutputStream(zipFile);
                ZipOutputStream zos = new ZipOutputStream(fos);
                for (File file : files) {
                    FileInputStream fis = new FileInputStream(file.getPath());
                    zos.putNextEntry(new ZipEntry(file.getPath().replace(path, "")));
                    int length;
                    while ((length = fis.read(buffer)) > 0)
                        zos.write(buffer, 0, length);
                    zos.closeEntry();
                    fis.close();
                }
                zos.close();
                fos.close();
                System.out.println("created zip archive");
                created = true;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return created;
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
