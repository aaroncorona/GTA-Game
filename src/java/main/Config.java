package main;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

import static java.lang.Integer.parseInt;

// The Config class saves and loads settings using a local text file
public class Config {

    private static final File CONFIG_FILE = new File("/Users/aaroncorona/eclipse-workspace/GTA/src/assets/configs/configs.txt");

    // Private constructor - Noninstantiable class
    private Config(){}

    // Helper method to get the current file settings
    private static String getConfigFileContents() {
        StringBuffer content = new StringBuffer();
        try {
            Scanner scan = new Scanner(CONFIG_FILE);
            // Reading lines of the file and appending them to StringBuffer
            while (scan.hasNextLine()) {
                content.append(scan.nextLine() + System.lineSeparator());
            }
            scan.close();
        } catch (IOException e) {
            System.out.println(e);
        }
        return String.valueOf(content);
    }

    // Method to get the Screen Width columns config setting
    public static double getScreenWidthConfig() {
         // Find the screen setting in the file
        String configs = getConfigFileContents();
        String configName = "screen_width_adjuster = ";
        int configIndex = configs.indexOf(configName) + configName.length();
        double setting = Double.parseDouble(configs.substring(configIndex, configIndex+3));
        return setting;
    }

    // Method to get the Background music setting
    public static boolean getBackgroundMusicConfig() {
        // Find the background music setting in the file
        String configs = getConfigFileContents();
        String configName = "background_music_allowed = ";
        int configIndex = configs.indexOf(configName) + configName.length();
        int setting = parseInt(configs.substring(configIndex, configIndex+1));
        // Convert int to bool
        if(setting == 1) {
            return true;
        }
        return false;
    }

    // Method to get the Sound effect setting
    public static boolean getSoundEffectConfig() {
        // Find the sound effect setting in the file
        String configs = getConfigFileContents();
        String configName = "sound_effects_allowed = ";
        int configIndex = configs.indexOf(configName) + configName.length();
        int setting = parseInt(configs.substring(configIndex, configIndex+1));
        // Convert int to bool
        if(setting == 1) {
            return true;
        }
        return false;
    }

    // Method to get the difficulty level setting
    public static int getDifficultyConfig() {
        // Find the screen setting in the file
        String configs = getConfigFileContents();
        String configName = "difficulty = ";
        int configIndex = configs.indexOf(configName) + configName.length();
        int setting = parseInt(configs.substring(configIndex, configIndex+1));
        return setting;
    }

    // Method to set the Screen Width config setting in the file
    public static void setScreenWidthConfig(double screenWidthAdjuster) {
        // Get the config file contents
        StringBuilder configs = new StringBuilder(getConfigFileContents());
        String configName = "screen_width_adjuster = ";
        int configIndex = configs.indexOf(configName) + configName.length();
        // Update the config setting
        configs.replace(configIndex, configIndex+3, String.valueOf(screenWidthAdjuster));
        try {
            // Override file with new setting
            FileWriter fw = new FileWriter(CONFIG_FILE);
            fw.write(String.valueOf(configs));
            fw.flush();
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    // Method to set the Background music setting in the file
    public static void setBackgroundMusicConfig(boolean backgroundMusicAllowed) {
        // Get the config file contents
        StringBuilder configs = new StringBuilder(getConfigFileContents());
        String configName = "background_music_allowed = ";
        int configIndex = configs.indexOf(configName) + configName.length();
        // Translate bool to int for the file
        int setting = 0;
        if(backgroundMusicAllowed) {
            setting = 1;
        }
        // Update the config setting
        configs.replace(configIndex, configIndex+1, String.valueOf(setting));
        try {
            // Override file with new setting
            FileWriter fw = new FileWriter(CONFIG_FILE);
            fw.write(String.valueOf(configs));
            fw.flush();
        } catch (IOException e) {
            System.out.println(e);
        }
        // Update the setting instantly
        Panel.backgroundMusicAllowed = getBackgroundMusicConfig();
    }

    // Method to set the Sound effect setting in the file
    public static void setSoundEffectConfig(boolean soundEffectsAllowed) {
        // Get the config file contents
        StringBuilder configs = new StringBuilder(getConfigFileContents());
        String configName = "sound_effects_allowed = ";
        int configIndex = configs.indexOf(configName) + configName.length();
        // Translate bool to int for the file
        int setting = 0;
        if(soundEffectsAllowed) {
            setting = 1;
        }
        // Update the config setting
        configs.replace(configIndex, configIndex+1, String.valueOf(setting));
        try {
            // Override file with new setting
            FileWriter fw = new FileWriter(CONFIG_FILE);
            fw.write(String.valueOf(configs));
            fw.flush();
        } catch (IOException e) {
            System.out.println(e);
        }
        // Update the setting instantly
        Panel.soundEffectAllowed = getSoundEffectConfig();
    }

    // Method to set the difficulty level setting in the file
    public static void setDifficultyConfig(int difficulty) {
        // Get the config file contents
        StringBuilder configs = new StringBuilder(getConfigFileContents());
        String configName = "difficulty = ";
        int configIndex = configs.indexOf(configName) + configName.length();
        // Update the config setting
        configs.replace(configIndex, configIndex+1, String.valueOf(difficulty));
        try {
            // Override file with new setting
            FileWriter fw = new FileWriter(CONFIG_FILE);
            fw.write(String.valueOf(configs));
            fw.flush();
        } catch (IOException e) {
            System.out.println(e);
        }
        // Update the setting instantly
        Panel.difficultyLevel = getDifficultyConfig();
    }
}
