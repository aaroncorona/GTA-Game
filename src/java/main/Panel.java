package main;

import entity.car.CopCar;
import entity.car.PlayerCar;
import entity.item.ItemManager;
import menu.ControlMenu;
import menu.PauseMenu;
import menu.TitleMenu;
import tile.TileManager;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.sql.Timestamp;
import java.util.*;

public class Panel extends JPanel implements Runnable {

    // Constants for screen size
    public static final int UNIT_SIZE = 50; // most granular measurement
    public static final int SCREEN_COLS = 21;
    public static final int SCREEN_ROWS = 15;
    public static final int SCREEN_WIDTH = SCREEN_COLS * UNIT_SIZE;
    public static final int SCREEN_HEIGHT = SCREEN_ROWS * UNIT_SIZE;;

    // Game state
    public static boolean titleState;
    public static boolean playState;
    public static boolean pauseState;

    // Key handler
    public static KeyHandler key = KeyHandler.getInstance();

    // Tile and Item Managers
    private static TileManager tileManager = TileManager.getInstance();
    private static ItemManager itemManager = ItemManager.getInstance();

    // Entity objects
    public static PlayerCar playerCar = new PlayerCar();
    public static CopCar copCar = new CopCar();

    // Menus
    public static TitleMenu titleMenu = new TitleMenu();
    public static PauseMenu pauseMenu = new PauseMenu();
    public static ControlMenu controlMenu = new ControlMenu();
    // @TODO migrate to menu classes
    public static JPopupMenu gameOverMenu = new JPopupMenu();
    public static JPopupMenu highScoreMenu = new JPopupMenu();

    // Constructor to create the game panel within a Frame
    Panel() {
        // Panel UI settings
        this.setBackground(Color.LIGHT_GRAY);
        this.setPreferredSize(new Dimension(SCREEN_WIDTH,SCREEN_HEIGHT));
        this.setFocusable(true);
        // Add a Key listener
        this.addKeyListener(key);
        // Start the game loop
        startGameThread(this);
        // Initial game state
        titleState = true;
        playState = false;
        pauseState = false;
        titleMenu.open = true;
    }

    // Method to launch the game loop
    public void startGameThread(Panel panel) {
        // Start thread
        Thread gameThread = new Thread(panel);
        gameThread.start();
    }

    // Game loop logic that the game thread runs
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
    public void update() {
        handleKeyInput();
        if(playState) {
            // Update all game components
            itemManager.update();
            playerCar.update();
            copCar.update();
            // Check for game over
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
        if (key.spacePress == true && pauseState == false) {
            pauseGame();
            key.spacePress = false;
        }
        // Space bar to resume game
        if (key.spacePress == true && pauseState == true) {
            resumeGame();
            key.spacePress = false;
        }
    }

    @ Override
    public void paint(Graphics g) {
        super.paint(g);

        // @TODO migrate to menu class
        // Display current score (money)
        g.setColor(Color.GREEN.brighter());
        g.setFont(new Font("Serif", Font.PLAIN, 50));
        g.drawString("Bank Account: $" + itemManager.moneyValueTotal,20,40); // coordinates start in the top left

        // Draw game components
        tileManager.draw(g);
        itemManager.draw(g);
        playerCar.draw(g);
        copCar.draw(g);

        // Draw menus
        titleMenu.draw(g);
        pauseMenu.draw(g);
        controlMenu.draw(g);
    }

    // Method to check for an event that ends the game and respond accordingly
    public void handleGameOver() {
        // Check for the player dying, this is currently the only way to lose
        if(playerCar.dead) {
            // Draw the player explosion first
            repaint();
            // Stop the game
            playState = false;
            System.out.println("Final Score: $" + itemManager.moneyValueTotal);
            // Trigger menus
            // @TODO add to Menu class
            // Log final score in the CSV file if it's past a certain minimum
            if (itemManager.moneyValueTotal >= 20) {
                try {
                    // Create or append file
                    FileWriter fw = new FileWriter("/Users/aaroncorona/eclipse-workspace/GTA/src/assets/scores/high_scores.csv", true); // FileWriter append mode is triggered with true (also creates new file if none exists)
                    PrintWriter write = new PrintWriter(fw);
                    // Print the score to the csv file and the time on the column next to it
                    write.println(); // Skip to new row
                    write.print(itemManager.moneyValueTotal);
                    write.print(","); // comma separate to print to the next column
                    write.print(System.currentTimeMillis());
                    // Close and finish the job
                    write.close();
                } catch(IOException e) {
                    System.out.print(e);
                }
            }

            // Create Game Over Menu
            gameOverMenu = new JPopupMenu();
            gameOverMenu.setLocation(600, 300);
            gameOverMenu.setBackground(Color.WHITE);
            gameOverMenu.setFocusable(false);
            JLabel gameOverMenuLabel = new JLabel("WASTED");
            gameOverMenuLabel.setFont(new Font("Verdana", Font.PLAIN, 100));
            gameOverMenuLabel.setForeground(Color.RED.darker());
            gameOverMenuLabel.setAlignmentX(CENTER_ALIGNMENT);
            gameOverMenuLabel.setAlignmentY(CENTER_ALIGNMENT);
            gameOverMenu.add(gameOverMenuLabel);
//            gameOverMenu.setVisible(true);

            // Create High Score Menu
            highScoreMenu = new JPopupMenu();
            highScoreMenu.setLocation(530, 440);
            highScoreMenu.setBackground(new Color(255, 105, 97));
            highScoreMenu.setBorder(BorderFactory.createLineBorder(new Color(255, 105, 97), 30));
            highScoreMenu.setFocusable(false);
            JLabel highScoreMenuLabel = new JLabel(getHighScoreMessage());
            highScoreMenuLabel.setFont(new Font("Verdana", Font.PLAIN, 30)); // Buffer
            highScoreMenuLabel.setForeground(Color.BLACK);
            highScoreMenuLabel.setAlignmentX(CENTER_ALIGNMENT);
            highScoreMenuLabel.setAlignmentY(CENTER_ALIGNMENT);
            highScoreMenu.add(highScoreMenuLabel);
        }
    }

    // Method to stop the game loop and open the pause menu
    public static void pauseGame() {
        playState = false;
        pauseState = true;
        pauseMenu.open = true;
    }

    // Method to run the game again and hide the pause menu
    public static void resumeGame() {
        playState = true;
        pauseState = false;
        pauseMenu.open = false;
        controlMenu.open = false;
    }

    // Method to reset all game mechanics
    public static void resetGame() {
        // Start the game
        titleState = false;
        playState = true;
        pauseState = false;

        // Reset all game components
        itemManager.setDefaultValues();
        playerCar.setDefaultValues();
        copCar.setDefaultValues();

        // Reset all menu settings
        titleMenu.setDefaultValues();
        pauseMenu.setDefaultValues();
        controlMenu.setDefaultValues();
    }

    // @TODO add to Menu class
    // Method to read the high score file and return the high score results
    public static String getHighScoreMessage() {
        // Create TreeMap to hold the scores for deduping and ordering
        TreeMap<Integer, String> scoreMap = new TreeMap<Integer, String>();
        File fileObj = new File("/Users/aaroncorona/eclipse-workspace/GTA/src/assets/scores/high_scores.csv");
        // Read the local CSV file
        try {
            Scanner myScanner = new Scanner(fileObj);
            myScanner.useDelimiter("\\n|,|\\s*\\$"); // Treats commas and whitespace as delimiters to read the CSV
            // Fill the tree map using the CSV
            for(int i = 0; myScanner.hasNext(); i++) {
                for(int j = 0; j < 1; j++) {
                    int score = (int) Integer.parseInt(myScanner.next().trim());
                    String date = new Date(new Timestamp((long) Long.parseLong(myScanner.next().trim())).getTime())
                                            .toString().substring(4, 10);
                    scoreMap.put(score, date);
                }
            }
            myScanner.close();
        } catch (FileNotFoundException e) {
            System.out.println(e);
        }

        // Return a message with the user's score and the top three scores of all time
        String message;
        message = "<html> Your Final Score is <b>" + itemManager.moneyValueTotal + "</b><br>";
        if (itemManager.moneyValueTotal > scoreMap.lastKey()) {
            message += "CONGRATS! You have the all time best score! <br>";
        } else{
            message += "<i>You did not beat the high score</i><br>";
        }
        int i = 0;
        for (Map.Entry entry : scoreMap.descendingMap().entrySet()) {
            if (i++ < 3) {
                int currentScore = (int) entry.getKey();
                message += ("<u> Score #" + i + "</u>: <b>" + currentScore + "</b> on " +
                        entry.getValue() + "<br>");
            }
        }
        return message;
    }
}

