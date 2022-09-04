package main;


import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

// The Sound class loads and plays .wav files
public class Sound {

    private static Clip backgroundMusic;
    private static Clip shotSound;
    private static Clip sirenSound;

    // Private constructor - Noninstantiable class
    private Sound() {}

    // Method to continually play background music
    public static void playBackgroundMusic() {
        String filePath = "/Users/aaroncorona/eclipse-workspace/GTA/src/assets/sounds/all_downhill.wav";
        try {
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(new File(filePath));
            backgroundMusic = AudioSystem.getClip();
            backgroundMusic.open(audioIn);
            backgroundMusic.start();
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    public static void pauseBackgroundMusic() {
        backgroundMusic.stop();
    }

    public static void resumeBackgroundMusic() {
        backgroundMusic.start();
    }

    public static void playGameOver() {
        String filePath = "/Users/aaroncorona/eclipse-workspace/GTA/src/assets/sounds/game_over.wav";
        try {
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(new File(filePath));
            Clip clip = AudioSystem.getClip();
            clip.open(audioIn);
            clip.start();
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    public static void playShot() {
        String filePath = "/Users/aaroncorona/eclipse-workspace/GTA/src/assets/sounds/shot.wav";
        try {
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(new File(filePath));
            Clip clip = AudioSystem.getClip();
            clip.open(audioIn);
            clip.start();
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    public static void playSiren() {
        String filePath = "/Users/aaroncorona/eclipse-workspace/GTA/src/assets/sounds/siren.wav";
        try {
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(new File(filePath));
            Clip clip = AudioSystem.getClip();
            clip.open(audioIn);
            clip.start();
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    public static void playCoins() {
        String filePath = "/Users/aaroncorona/eclipse-workspace/GTA/src/assets/sounds/coins.wav";
        try {
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(new File(filePath));
            Clip clip = AudioSystem.getClip();
            clip.open(audioIn);
            clip.start();
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }
}
