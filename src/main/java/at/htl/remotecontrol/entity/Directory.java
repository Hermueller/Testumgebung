package at.htl.remotecontrol.entity;

import java.io.*;
import java.util.LinkedList;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * 29.10.2015:  Gnadi       Erstellen von Verzeichnissen
 * 30.10.2015:  Tobias      Klasse erstellt
 * 30.10.2015:  Tobias      Funktion für das zippen von Ordnern
 * 31.10.2015:  Tobias      Funktion "Erstellen von Ordnern" mit Meldungen versehen
 * 31.10.2015:  Tobias      Mothode "getAllPaths" implementiert
 * 31.10.2015:  Tobias      Zippen von Ordnern korrigiert und mit Meldungen versehen
 * 31.10.2015:  Tobias      einen oder mehrere Ordner löschen
 */
public class Directory {

    public static boolean create(String path) {
        boolean created = false;
        File dir = new File(path);
        if (dir.exists()) {
            System.out.println(String.format("Directory %s is already exist!", path));
        } else if (!dir.mkdir()) {
            System.out.println(String.format("Directory %s failed to create!", path));
        } else {
            System.out.println("created directory " + path);
            created = true;
        }
        return created;
    }

    public static boolean createFile(File file) {
        boolean created = false;
            if (file.exists()) {
            System.out.println(String.format("File %s is already exist!", file.getName()));
        } else try {
            if (!file.createNewFile()) {
                System.out.println(String.format("File %s failed to create!", file.getName()));
            } else {
                System.out.println("created directory " + file.getName());
                created = true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return created;
    }

    public static boolean zip(String path, String zipFileName) {
        if (!path.contains(".zip")) {
            System.out.println(String.format("Directory %s is already zipped!", path));
            return true;
        }
        boolean created = false;
        File dir = new File(path);
        if (!dir.isDirectory()) {
            System.out.println(path + "is a invalid directory");
        } else if (!zipFileName.contains(".zip")) {
            System.out.println(zipFileName + " is a invalid filename of an zip archive!");
        } else {
            byte[] buffer = new byte[8192];
            try {
                FileOutputStream fos = new FileOutputStream(zipFileName);
                ZipOutputStream zos = new ZipOutputStream(fos);
                LinkedList<String> fileNames = getAllPaths(dir);
                for (String fileName : fileNames) {
                    FileInputStream fis = new FileInputStream(fileName);
                    zos.putNextEntry(new ZipEntry(fileName));
                    int length;
                    while ((length = fis.read(buffer, 0, buffer.length)) > 0)
                        zos.write(buffer, 0, length);
                    zos.closeEntry();
                    fis.close();
                }
                zos.close();
                fos.close();
                System.out.println("created zip archive");
                created = true;
            } catch (FileNotFoundException e) {
                System.out.println("Directory not found!");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return created;
    }

    private static LinkedList<String> getAllPaths(File dir) {
        LinkedList<String> allFiles = new LinkedList<String>();
        File[] files = dir.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory())
                    allFiles.addAll(getAllPaths(file));
                allFiles.add(file.getAbsolutePath());
            }
        }
        return allFiles;
    }

    public static boolean deleteAll(LinkedList<String> paths) {
        boolean error = false;
        for (String path : paths) {
            if (!delete(path)) {
                System.out.println(path + " not deleted");
                error = true;
            } else {
                System.out.println(path + " deleted");
            }
        }
        System.out.println("given paths are deleted");
        return !error;
    }

    public static boolean delete(String path) {
        boolean error = false;
        File dir = new File(path);
        if (dir.isDirectory()) {
            File[] files = dir.listFiles();
            if (files != null)
                for (File f : files)
                    if (!delete(f.getAbsolutePath()))
                        error = true;
        }
        return dir.delete() && !error;
    }

}
