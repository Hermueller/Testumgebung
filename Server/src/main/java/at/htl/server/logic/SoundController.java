package at.htl.server.logic;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class SoundController {

    private static SoundController instance;

    private static SoundController getInstance() {
        if (instance == null) {
            instance = new SoundController();
        }
        return instance;
    }

    public static void startWarnSound() {
//        startSound("sound/Fall.wav");
    }

    private static void startSound(String filepath) {
        try {
            // Open an audio input stream.
            //TODO load Stream correct
            ClassLoader classLoader = getInstance().getClass().getClassLoader();
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(classLoader.getResourceAsStream(filepath));
            // Get a sound clip resource.
            Clip clip = AudioSystem.getClip();
            // Open audio clip and load samples from the audio input stream.
            clip.open(audioIn);
            clip.start();
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }
}
