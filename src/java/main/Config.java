package main;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

// The Config class saves and loads settings using a local text file
public class Config {

    private static final File CONFIG_FILE = new File("/Users/aaroncorona/eclipse-workspace/GTA/src/assets/configs/configs.txt");

    // Private constructor - Noninstantiable class
    private Config(){}

    // Method to get the Screen Width columns config setting
    public static int getScreenWidthConfig() {
        double screenWidthAdjuster = 1;
        // Get setting from file
        try {
            Scanner myScanner = new Scanner(CONFIG_FILE);
            // Find the setting
            for(int i = 0; myScanner.hasNext(); i++) {
                if(myScanner.next().equals("screen_width_adjuster")) {
                    screenWidthAdjuster = Double.parseDouble(myScanner.next());
                }
            }
            myScanner.close();
        } catch (FileNotFoundException e) {
            System.out.println(e);
        }
        // Convert adjuster setting to screen columns
        int maxScreenCols = 30;
        int ScreenCols = (int) (screenWidthAdjuster * maxScreenCols);
        return ScreenCols;
    }

    // Method to get the Background music setting
    public static boolean getBackgroundMusicConfig() {
        int backgroundMusicAllowed = 1;
        // Get setting from file
        try {
            Scanner myScanner = new Scanner(CONFIG_FILE);
            // Find the setting
            for(int i = 0; myScanner.hasNext(); i++) {
                if(myScanner.next().equals("background_music_allowed")) {
                    backgroundMusicAllowed = Integer.parseInt(myScanner.next());
                }
            }
            myScanner.close();
        } catch (FileNotFoundException e) {
            System.out.println(e);
        }
        // Convert int to bool
        if(backgroundMusicAllowed == 1) {
            return true;
        }
        return false;
    }

    // Method to get the Sound effect setting
    public static boolean getSoundEffectConfig() {
        int soundEffectsAllowed = 1;
        // Get setting from file
        try {
            Scanner myScanner = new Scanner(CONFIG_FILE);
            // Find the setting
            for(int i = 0; myScanner.hasNext(); i++) {
                if(myScanner.next().equals("sound_effects_allowed")) {
                    soundEffectsAllowed = Integer.parseInt(myScanner.next());
                }
            }
            myScanner.close();
        } catch (FileNotFoundException e) {
            System.out.println(e);
        }
        // Convert int to bool
        if(soundEffectsAllowed == 1) {
            return true;
        }
        return false;
    }

    // Method to get the difficulty level setting
    public static int getDifficultyConfig() {
        int difficulty = 1;
        // Read the config file
        try {
            Scanner myScanner = new Scanner(CONFIG_FILE);
            // Find the setting
            for(int i = 0; myScanner.hasNext(); i++) {
                if(myScanner.next().equals("difficulty")) {
                    difficulty = Integer.parseInt(myScanner.next());
                }
            }
            myScanner.close();
        } catch (FileNotFoundException e) {
            System.out.println(e);
        }
        return difficulty;
    }

    // Method to set the Screen Width config setting in the file
    public static void setScreenWidthConfig(double screenWidthAdjuster) {
    }

    // Method to set the Background music setting in the file
    public static void setBackgroundMusicConfig(int backgroundMusicAllowed) {
    }

    // Method to set the Sound effect setting in the file
    public static void setSoundEffectConfig(int soundEffectsAllowed) {
    }

    // Method to set the difficulty level setting in the file
    public static void setDifficultyConfig(int difficulty) {
    }

}
