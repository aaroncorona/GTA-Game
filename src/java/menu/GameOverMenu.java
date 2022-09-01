package menu;

import entity.item.ItemManager;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;

// Class for an ending Menu that displays the high scores
public class GameOverMenu implements Menu {

    private JPopupMenu gameOverMenu;
    public boolean open;
    private int xPos, yPos;
    private int width, height;
    JLabel label1, label2;

    // Constructor
    public GameOverMenu() {
        // Instantiate menu and related objects
        gameOverMenu = new JPopupMenu();

        // Label for Wasted
        label1 = new JLabel("WASTED");
        gameOverMenu.add(label1);
        // Label for the Score
        String scoreMessage = getHighScoreMessage();
        label2 = new JLabel(scoreMessage);
        gameOverMenu.add(label2);

        setDefaultValues();
    }

    @Override
    public void setDefaultValues() {
        // Placement vars
        xPos = 450;
        yPos = 240;
        width = 800;
        height = 340;
        open = false;

        // Menu UI
        gameOverMenu.setLocation(xPos, yPos);
        gameOverMenu.setPreferredSize(new Dimension(width, height));
        gameOverMenu.setBackground(Color.PINK);
        gameOverMenu.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));
        gameOverMenu.setFocusable(false);
        gameOverMenu.setVisible(false);

        // Wasted Label UI
        label1.setFont(new Font("Verdana", Font.PLAIN, 100));
        label1.setForeground(Color.RED.brighter());
        label1.setAlignmentX(Component.CENTER_ALIGNMENT);
        // Score Label UI
        label2.setFont(new Font("Verdana", Font.PLAIN, 25));
        label2.setForeground(Color.GRAY);
        label2.setAlignmentX(Component.CENTER_ALIGNMENT);
        label2.setHorizontalAlignment(SwingConstants.CENTER);
    }

    @Override
    public void loadImage() {
        // Not currently used
    }

    // Method to Log the score in the local CSV
    public void logScore() {
        // Log final score in the CSV file if it's past a certain minimum
        if (ItemManager.moneyValueTotal >= 20) {
            try {
                // Create or append file
                FileWriter fw = new FileWriter("/Users/aaroncorona/eclipse-workspace/GTA/src/assets/scores/high_scores.csv", true); // FileWriter append mode is triggered with true (also creates new file if none exists)
                PrintWriter write = new PrintWriter(fw);
                // Print the score to the csv file and the time on the column next to it
                write.println(); // Skip to new row
                write.print(ItemManager.moneyValueTotal);
                write.print(","); // comma separate to print to the next column
                write.print(System.currentTimeMillis());
                // Close and finish the job
                write.close();
            } catch(IOException e) {
                System.out.print(e);
            }
        }
    }

    // Helper variable to read all the high scores from the local CSV
    private TreeMap getHighScores() {
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
        return scoreMap;
    }

    // Helper variable to return the proper high score message and the top scores
    private String getHighScoreMessage() {
        // Draw high score and message
        TreeMap<Integer, String> scoreMap = getHighScores();
        // Return a message with the user's score and the top three scores of all time
        String scoreMessage;
        scoreMessage = "<html> <center> Your Final Score is <b>" + ItemManager.moneyValueTotal + "</b><br>";
        if (ItemManager.moneyValueTotal > scoreMap.lastKey()) {
            scoreMessage += "CONGRATS! You have the all time best score! <br>";
        } else{
            scoreMessage += "<i>You did not beat the high score</i><br>";
        }
        int i = 0;
        for (Map.Entry entry : scoreMap.descendingMap().entrySet()) {
            if (i++ < 3) {
                int currentScore = (int) entry.getKey();
                scoreMessage += ("<u> Score #" + i + "</u>: <b>" + currentScore + "</b> on " +
                        entry.getValue() + "<br>");
            }
        }
        return scoreMessage;
    }

    @Override
    public void draw(Graphics g) {
        // No graphics to draw, just reveal the menu based on the game state
        if(open) {
            // Update the label with the correct score
            String scoreMessage = getHighScoreMessage();
            label2.setText(scoreMessage);
            // Reveal Popup Menu
            gameOverMenu.setVisible(true);
        } else {
            gameOverMenu.setVisible(false);
        }
    }
}


