package at.htl.remotecontrol.entity;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
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
 * 01.11.2015:  Tobias      Löschen von Verzeichnissen verbessert und mit Meldungen versehen
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

    public static boolean zip(String path, String zipFileName) {
        if (path.contains(".zip")) {
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

    private static LinkedList<File> getAllFiles(File dir) {
        LinkedList<File> allFiles = new LinkedList<File>();
        File[] files = dir.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    allFiles.addAll(getAllFiles(file));
                } else {
                    allFiles.add(file);
                    System.out.println(file.getPath() + " added");
                }
            }
        }
        return allFiles;
    }

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
