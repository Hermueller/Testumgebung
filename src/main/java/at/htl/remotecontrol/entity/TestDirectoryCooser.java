package at.htl.remotecontrol.entity;

import java.io.File;
import java.io.IOException;

/**
 * Created by gnadlinger on 27.10.15.
 */
public class TestDirectoryCooser {

    private static TestDirectoryCooser instance = null;
    String filePath;
    private String username;

    public static TestDirectoryCooser getInstance() {
        if (instance == null) {
            instance = new TestDirectoryCooser();
        }
        return instance;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public void CreateDirectory(){

        System.out.println("UserName "+getUsername());
        System.out.println("FilePath "+getFilePath());



        if(getFilePath() != null && getUsername()!= null){
            File file = new File(getFilePath() + "/"+ getUsername());
            file.mkdir();
            System.out.println("Verzeichnis erstellt: "+getFilePath() +"/"+getUsername());
        }
        else if(getUsername() == null) {
            System.out.println("Kein User vorhanden");
        }
        else{
            System.out.println("Something happend -Microsoft ");
        }



    }


}
