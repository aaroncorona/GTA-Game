package main;

import entity.car.CopCar;
import entity.car.PlayerCar;
import entity.item.ItemManager;
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

    // Variables for the game state
    public static boolean running;
    public static boolean pause;

    // Menus
    // @TODO migrate to menu class
    public static JPopupMenu pauseMenu = new JPopupMenu();
    public static JPopupMenu gameOverMenu = new JPopupMenu();
    public static JPopupMenu highScoreMenu = new JPopupMenu();

    // Key handler
    public static KeyHandler key = KeyHandler.getInstance();

    // Managers
    private static TileManager tileManager = TileManager.getInstance();
    private static ItemManager itemManager = ItemManager.getInstance();

    // Entity objects
    public static PlayerCar playerCar = new PlayerCar();
    public static CopCar copCar = new CopCar();

    // Create game panel (constructor)
    Panel() {
        // Add Panel details
        this.setBackground(Color.LIGHT_GRAY);
        this.setPreferredSize(new Dimension(SCREEN_WIDTH,SCREEN_HEIGHT));
        this.setFocusable(true);

        // Add the Key listener to the panel
        this.addKeyListener(key);
    }

    // Method to launch the game loop
    public void startGameThread() {
        // Start thread
        Thread gameThread = new Thread(this);
        gameThread.start();
    }

    // Game thread logic that the Thread will run
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
        if(running) {
            // Object states
            itemManager.update();
            playerCar.update();
            copCar.update();
            // Game state
            handleGameOver();
        }
    }

    // Helper method to handle key inputs for basic game mechanics
    private void handleKeyInput() {
        // Enter key to restart game (if stopped) or start the game from the initial pause menu
        if (key.enterPress == true && running == false) {
            resetGame();
            key.enterPress = false;
        }
        // Delete key to exit game (if stopped)
        if (key.backSpacePress == true && running == false) {
            System.exit(0);
            key.backSpacePress = false;
        }
        // @TODO migrate to menu class
        // Space bar to pause game
        if (key.spacePress == true && pause == false && running == true) {
            pauseGame();
            key.spacePress = false;
        }
        // Space bar to resume game
        if (key.spacePress == true && pause == true && running == false) {
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

        // @TODO migrate to menu class
        // Initial Pause menu
        if(running == false && itemManager.moneyValueTotal == 0) {
            // Draw the pause menu
            String filePathStartMenu = "/Users/aaroncorona/eclipse-workspace/GTA/src/assets/images/menus/start_menu.png";
            ImageIcon startMenu = new ImageIcon(new ImageIcon(filePathStartMenu).getImage().getScaledInstance((SCREEN_WIDTH/2)+50, SCREEN_HEIGHT-50, Image.SCALE_DEFAULT));
            startMenu.paintIcon(this, g, SCREEN_WIDTH/4, 10);
            // Draw text
            g.setColor(Color.ORANGE.brighter());
            g.setFont(new Font("Serif", Font.ITALIC, 50));
            g.drawString("Press ENTER to Play",(SCREEN_WIDTH/4)+150,660);
        }

        tileManager.draw(g);
        itemManager.draw(g);
        playerCar.draw(g);
        copCar.draw(g);
    }

    // Method to check for an event that ends the game and respond accoringly
    public void handleGameOver() {
        // The player dying is currently the only way to lose
        if(playerCar.dead) {
            // Stop the game
            running = false;
            // Draw the player explosion before the menus
            repaint();
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
            gameOverMenu.setVisible(true);

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
//            highScoreMenu.setVisible(true);
        }
    }

    // Method to stop the game loop and bring up the pause menu
    public void pauseGame() {
        pause = true;
        running = false;
        // @TODO add to menu class
        pauseMenu = new JPopupMenu();
        pauseMenu.setLocation(600, 400);
        pauseMenu.setPreferredSize(new Dimension(500, 30));
        pauseMenu.setBackground(Color.WHITE);
        pauseMenu.setBorder(BorderFactory.createLineBorder(Color.PINK, 3));
        pauseMenu.setFocusable(false); // Prevent the menu from taking focus from the panel
        // Create Pause Menu Label
        JLabel pauseMenuLabel = new JLabel("Press SPACE to Resume");
        pauseMenuLabel.setFont(new Font("Verdana", Font.PLAIN, 18));
        pauseMenuLabel.setForeground(Color.BLACK);
        pauseMenuLabel.setAlignmentX(CENTER_ALIGNMENT);
        pauseMenuLabel.setAlignmentY(CENTER_ALIGNMENT);
        pauseMenu.add(pauseMenuLabel);
//        pauseMenu.setVisible(true);
    }

    // Method to run the game again and hide the pause menu
    public void resumeGame() {
        pause = false;
        running = true;
        // @TODO add to menu class
        pauseMenu.setVisible(false);
    }

    // Method to reset all game mechanics
    public void resetGame() {
        // Reset object values
        itemManager.setDefaultValues();
        playerCar.setDefaultValues();
        copCar.setDefaultValues();
        // @TODO menus should be closed

        // Start the game
        running = true;
        pause = false;
    }


    // @TODO add to cop car class
    // Method to create another bullet traveling west or north from the cop
    public void generateNewCopBullet() {
    }

    // @TODO add to Menu class
    // Method to read the high score file and return the high score results
    public String getHighScoreMessage() {
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

