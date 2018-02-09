package at.htl.server.logic;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class SoundController {

    public static void startWarnSound() {
//        startSound("/resources/sound/Fall.mp3");
    }

    private static void startSound(String filepath) {
        try {
            // Open an audio input stream.
            File soundFile = new File(filepath); //you could also get the sound file with an URL
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(soundFile);
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
