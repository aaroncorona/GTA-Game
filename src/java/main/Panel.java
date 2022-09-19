package main;

import entity.car.CopCarManager;
import entity.car.PlayerCar;
import entity.item.ItemManager;
import menu.*;
import tile.TileManager;

import javax.swing.*;
import java.awt.*;

public class Panel extends JPanel implements Runnable {

    // Constants for screen size
    public static final int UNIT_SIZE = 50; // the default tile and entity size in pixels
    public static final int SCREEN_ROWS = 15;
    public static final int SCREEN_HEIGHT = SCREEN_ROWS * UNIT_SIZE;
    public static final int MAX_SCREEN_COLS = 26;
    public static final int SCREEN_COLS = (int) (MAX_SCREEN_COLS * Config.getScreenWidthConfig());
    public static final int SCREEN_WIDTH = SCREEN_COLS * UNIT_SIZE;

    // Load Config settings
    public static boolean backgroundMusicAllowed;
    public static boolean soundEffectAllowed;
    public static int difficultyLevel;

    // Game state - default is the title menu
    public static boolean titleState = true;
    public static boolean playState = false;
    public static boolean pauseState = false;

    // Key handler
    public static KeyHandler key = KeyHandler.getInstance();

    // Tile and Item Managers
    private static TileManager tileManager = TileManager.getInstance();
    private static ItemManager itemManager = ItemManager.getInstance();

    // Entity objects
    public static PlayerCar playerCar = new PlayerCar();
    public static CopCarManager copCarManager = CopCarManager.getInstance();

    // Menus
    public static ControlMenu controlMenu = new ControlMenu();
    public static GameOverMenu gameOverMenu = new GameOverMenu();
    public static PauseMenu pauseMenu = new PauseMenu();
    public static SettingsMenu settingsMenu = new SettingsMenu();
    public static TitleMenu titleMenu = new TitleMenu();

    // Constructor to create the game panel within a Frame
    public Panel() {
        // Panel UI settings
        this.setBackground(Color.LIGHT_GRAY);
        this.setPreferredSize(new Dimension(SCREEN_WIDTH,SCREEN_HEIGHT));
        this.setFocusable(true);
        // Add a Key listener
        this.addKeyListener(key);
        // Start the game loop
        startGameThread(this);
        // Open title menu
        titleMenu.open = true;
        // Default Config setting
        backgroundMusicAllowed = Config.getBackgroundMusicConfig();
        soundEffectAllowed = Config.getSoundEffectConfig();
        difficultyLevel = Config.getDifficultyConfig();
    }

    // Method to launch the game loop
    private void startGameThread(Panel panel) {
        // Start thread
        Thread gameThread = new Thread(panel);
        gameThread.start();
    }

    // Game loop logic that the game thread will run
    @Override
    public void run() {
        // Set rendering frequency
        final double FPS = 15.0;
        double timeToNextUpdate = 1000000000.0 / FPS;
        long startTime = System.nanoTime();
        double timePassedSinceUpdate = 0;

        while(true) {
            // Track FPS timing
            long currentTime = System.nanoTime();
            timePassedSinceUpdate += (currentTime - startTime) / timeToNextUpdate;
            startTime = currentTime;

            // Update the screen and data once the FPS time has passed
            if(timePassedSinceUpdate >= 1) {
                // Update screen
                update();
                repaint();
                // Update timing tracker
                timePassedSinceUpdate = 0;
            }
        }
    }

    // Method to update positions of all entities and objects as well as and checking for collisions
    private void update() {
        handleKeyInput();
        if(playState) {
            // Update all game components
            itemManager.update();
            playerCar.update();
            copCarManager.update();
            // Check for a game over state
            handleGameOver();
        }
    }

    // Helper method to handle key inputs for the game state
    private static void handleKeyInput() {
        // Enter key to restart game (if stopped)
        if (key.enterPress == true && playState == false) {
            resetGame();
            key.enterPress = false;
        }
        // Delete key to exit game (if stopped)
        if (key.backSpacePress == true && playState == false) {
            System.exit(0);
            key.backSpacePress = false;
        }
        // Space bar to pause game
        if (key.spacePress == true && pauseState == false && titleState == false) {
            pauseGame();
            key.spacePress = false;
        }
        // Space bar to resume game
        if (key.spacePress == true && pauseState == true) {
            resumeGame();
            key.spacePress = false;
        }
        // C key to show the Control menu
        if (key.cPress == true && controlMenu.open == false) {
            controlMenu.open = true;
            key.cPress = false;
        }
        // C key to hide the Control menu
        if (key.cPress == true && controlMenu.open == true) {
            controlMenu.open = false;
            key.cPress = false;
        }
        // M key to turn on music
        if (key.mPress == true) {
            if(Sound.isBackgroundMusicOn()) {
                Sound.pauseBackgroundMusic();
            } else {
                backgroundMusicAllowed = true;
                Sound.resumeBackgroundMusic();
            }
            key.mPress = false;
        }
    }

    @ Override
    public void paint(Graphics g) {
        super.paint(g);

        // Draw game components
        tileManager.draw(g);
        itemManager.draw(g);
        playerCar.draw(g);
        copCarManager.draw(g);

        // Draw menus
        gameOverMenu.draw(g);
        pauseMenu.draw(g);
        settingsMenu.draw(g);
        titleMenu.draw(g);
        controlMenu.draw(g); // put last so it can display over the Title screen
    }

    // Method to check for an event that ends the game and respond accordingly
    private void handleGameOver() {
        // Check for the player dying, this is currently the only way to lose
        if(playerCar.health == 0) {
            // Draw the player explosion first
            repaint();
            // Then stop the game
            playState = false;
            System.out.println("Final Score: $" + itemManager.moneyValueTotal);
            // Log score and open ending menu
            gameOverMenu.logScore();
            gameOverMenu.open = true;
            // Stop music and play game over
            Sound.pauseBackgroundMusic();
            Sound.playGameOver();
        }
    }

    // Method to stop the game loop and open the pause menu
    public static void pauseGame() {
        playState = false;
        pauseState = true;
        pauseMenu.open = true;
        Sound.pauseBackgroundMusic();
    }

    // Method to run the game again and hide the pause menu
    public static void resumeGame() {
        playState = true;
        pauseState = false;
        pauseMenu.open = false;
        controlMenu.open = false;
        settingsMenu.open = false;
        Sound.resumeBackgroundMusic();
    }

    // Method to reset all game mechanics
    public static void resetGame() {
        // Start the game
        titleState = false;
        playState = true;
        pauseState = false;

        // Reset keys
        KeyHandler.setDefaultValues();

        // Reset all game components
        itemManager.setDefaultValues();
        playerCar.setDefaultValues();
        copCarManager.setDefaultValues();

        // Reset all menu settings
        controlMenu.setDefaultValues();
        gameOverMenu.setDefaultValues();
        pauseMenu.setDefaultValues();
        settingsMenu.setDefaultValues();
        titleMenu.setDefaultValues();

        // Reset sound
        Sound.playBackgroundMusic();
    }
}

