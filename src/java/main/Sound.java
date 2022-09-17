package main;


import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

// The Sound class loads and plays .wav files
public class Sound {

    private static Clip backgroundMusic;

    // Private constructor - Noninstantiable class
    private Sound() {}

    // Method to continually play background music
    public static void playBackgroundMusic() {
        // Create sound clip
        String filePath = "/Users/aaroncorona/eclipse-workspace/GTA/src/assets/sounds/all_downhill.wav";
        try {
            // Convert WAV file to clip
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(new File(filePath));
            backgroundMusic = AudioSystem.getClip();
            backgroundMusic.open(audioIn);
            if(Panel.backgroundMusicAllowed) {
                backgroundMusic.start();
            }
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    public static void pauseBackgroundMusic() {
        backgroundMusic.stop();
    }

    public static void resumeBackgroundMusic() {
        if(Panel.backgroundMusicAllowed) {
            backgroundMusic.start();
        }
    }

    public static boolean isBackgroundMusicOn() {
        return backgroundMusic.isActive();
    }

    public static void playGameOver() {
        String filePath = "/Users/aaroncorona/eclipse-workspace/GTA/src/assets/sounds/game_over.wav";
        try {
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(new File(filePath));
            Clip clip = AudioSystem.getClip();
            clip.open(audioIn);
            if(Panel.soundEffectAllowed) {
                clip.start();
            }
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
            if(Panel.soundEffectAllowed) {
                clip.start();
            }
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
            if(Panel.soundEffectAllowed) {
                clip.start();
            }
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
            if(Panel.soundEffectAllowed) {
                clip.start();
            }
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }
}
