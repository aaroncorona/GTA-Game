
import javax.swing.*;
import javax.swing.Timer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.*;
import java.sql.Timestamp;
import java.util.*;

public class Panel extends JPanel implements ActionListener {

    // Constants
    static final int UNIT_SIZE = 25;
    static final int PLAYER_UNIT_SIZE = UNIT_SIZE*2; // player takes up 2 by 2 units (4 total)
    static final int SCREEN_WIDTH = 1400;
    static final int SCREEN_HEIGHT = 900;
    static final int GAME_SPEED = UNIT_SIZE*10;
    static final int GAME_SPEED_NITRO = GAME_SPEED / 3;

    // Helper variable flags to trigger events
    static boolean running = false;
    static boolean initialPause = true;
    static boolean pause = false;
    static boolean stopHs = false;
    static boolean nitro = false;

    // Helper variables for track dynamic information
    static long startTime;
    static long[][] highScoreArray;
    static int finalScore = 0;
    static int playerXLocation;
    static int playerYLocation;
    static char direction;
    static char oldDirection;
    static int money = 0;
    static int copXLocation;
    static int copYLocation;
    static int elapsedMins; // Make public
    static int elapsedSecondsRemainder; // Make public
    static Timer myTimer;

    // Menus
    // Pause Menu
    public static JPopupMenu pauseMenu = new JPopupMenu();
    // Game Over Menu
    public static JPopupMenu gameOverMenu = new JPopupMenu();
    // High Score Menu
    public static JPopupMenu highScoreMenu = new JPopupMenu();
    public static JLabel highScoreMenuLabel = new JLabel(); //buffer

    // Create game panel (constructor)
    Panel() {

        // Panel details
        this.setBackground(Color.LIGHT_GRAY);
        this.setPreferredSize(new Dimension(SCREEN_WIDTH,SCREEN_HEIGHT));
        this.setFocusable(true); // Events are only dispatched to the component that has focus. So your KeyEvent will only be dispatched to the panel if it is "focusable"

        // Add Timer
        myTimer = new Timer(GAME_SPEED, this); // creates a new object (this) after every delay time
        myTimer.start(); // Start timer to begin creating objects after every delay, this makes the actionPerformed function keep running its inner functions

        // Map Keys to Action responses
        this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0, false),"rightAction"); // KeyEvent responds to a right key, lower case, and false for it being pressed rather than released
        this.getActionMap().put("rightAction", new RightAction());
        this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0, false),"leftAction");
        this.getActionMap().put("leftAction", new LeftAction());
        this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0, false),"upAction");
        this.getActionMap().put("upAction", new UpAction());
        this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0, false),"downAction");
        this.getActionMap().put("downAction", new DownAction());
        this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0, false),"enterAction");
        this.getActionMap().put("enterAction", new EnterAction());
        this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_BACK_SPACE, 0, false),"deleteAction");
        this.getActionMap().put("deleteAction", new DeleteAction());
        this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, 0, false),"spaceAction");
        this.getActionMap().put("spaceAction", new SpaceAction());
//        this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_E, 0, false),"eAction");
//        this.getActionMap().put("eAction", new eAction());
        this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_R, 0, false),"rAction");
        this.getActionMap().put("rAction", new rAction());
    }

    public static void startGame() {
        resetPlayer();
        gameOverMenu.setVisible(false); // Close menu in case it's open
        initialPause = false; // reset (to remove initial pause menu)
        pause = false; // reset (to remove the pause menu if restarted during a pause)
        stopHs = false; // reset (to allow final score to be printed again if restarted)
        running = true; // Launch graphic drawings and action listener
        pauseMenu.setVisible(false);
        generateNewCopLocation();

        // Start stopwatch
        startTime = System.currentTimeMillis();
    }

    @ Override
    // Runs automatically on Frame rendering to execute drawEverything()
    public void paint(Graphics g) {
        super.paint(g);

        // Initial Pause menu
        if(initialPause == true) {
            // Draw the pause menu
            String filePathStartMenu = "/Users/aaroncorona/eclipse-workspace/GTA/src/assets/images/start_menu.png";
            ImageIcon startMenu = new ImageIcon(new ImageIcon(filePathStartMenu).getImage().getScaledInstance((SCREEN_WIDTH/2)+50, SCREEN_HEIGHT-50, Image.SCALE_DEFAULT));
            startMenu.paintIcon(this, g, SCREEN_WIDTH/4, 10);
            // Draw text
            g.setColor(Color.ORANGE.brighter());
            g.setFont(new Font("Serif", Font.ITALIC, 50));
            g.drawString("Press ENTER to Play",(SCREEN_WIDTH/4)+150,660);
        }
        // Image Icons - only draw after the game starts
        else {
            // Draw the car icon based on the movement direction so it faces the correct way
            String filePathCar = "/Users/aaroncorona/eclipse-workspace/GTA/src/assets/images/car_" + direction + ".png";
            ImageIcon player = new ImageIcon(new ImageIcon(filePathCar).getImage().getScaledInstance(PLAYER_UNIT_SIZE, PLAYER_UNIT_SIZE, Image.SCALE_DEFAULT));
            player.paintIcon(this, g, playerXLocation, playerYLocation);
            // Draw the cop
            String filePathCop = "/Users/aaroncorona/eclipse-workspace/GTA/src/assets/images/cop.png";
            ImageIcon cop = new ImageIcon(new ImageIcon(filePathCop).getImage().getScaledInstance(PLAYER_UNIT_SIZE, PLAYER_UNIT_SIZE, Image.SCALE_DEFAULT));
            cop.paintIcon(this, g, copXLocation, copYLocation);
            // Display current score
            g.setColor(Color.GREEN.darker());
            g.setFont(new Font("Serif", Font.PLAIN, 50));
            g.drawString("Bank Account: " + money,30,80); // coordinates start in the top left
            // Display stop watch
            g.setColor(Color.GRAY);
            g.setFont(new Font("Serif", Font.PLAIN, 25));
            g.drawString("Time Elapsed: " + elapsedMins + " Mins and " + elapsedSecondsRemainder + " Seconds",30,112); // coordinates start in the top left
            // Display control menu
            int menuSize = 320;
            String filePathControlMenu = "/Users/aaroncorona/eclipse-workspace/GTA/src/assets/images/control_menu.png";
            ImageIcon controlMenu = new ImageIcon(new ImageIcon(filePathControlMenu).getImage().getScaledInstance(menuSize, menuSize, Image.SCALE_DEFAULT));
            controlMenu.paintIcon(this, g, SCREEN_WIDTH-menuSize-10, 10);
            // For ad hoc checks
//            for(int i = 0;i<SCREEN_HEIGHT/UNIT_SIZE;i++){
//                g.drawLine(i*UNIT_SIZE,0,i*UNIT_SIZE,SCREEN_HEIGHT);
//                g.drawLine(0,i*UNIT_SIZE,SCREEN_WIDTH,i*UNIT_SIZE);
//            }
//            g.drawOval(playerXLocation,playerYLocation,UNIT_SIZE,UNIT_SIZE);
//            g.drawOval(copXLocation,copYLocation,UNIT_SIZE,UNIT_SIZE);
        }
    }

    public static void generateNewCopLocation(){ // populates new coordinates (int variable values) for a cop, which is then created with draw (graphics object)
        copXLocation = new Random().nextInt((int) (SCREEN_WIDTH/UNIT_SIZE)) * UNIT_SIZE; //get random coordinate within the boundary of the unit fully fitting (use division), while also being an exact coordinate (i.e. divisable by the unit size, so use multiplication)
        copYLocation = new Random().nextInt((int) (SCREEN_HEIGHT/UNIT_SIZE)) * UNIT_SIZE; //get random coordinate within the boundary of the unit fully fitting (use division), while also being an exact coordinate (i.e. divisable by the unit size, so use multiplication)
        // Generate a new location if the cop spawns on the wall (for improved UX)
        if(copXLocation == 0 || copYLocation == 0
                || copXLocation == SCREEN_WIDTH || copYLocation == SCREEN_HEIGHT) {
            generateNewCopLocation();
        }
    }

    public static void pauseGame() {
        pause = true;
        running = false;
        pauseMenu = new JPopupMenu();
        pauseMenu.setLocation(600, 400);
        pauseMenu.setPreferredSize(new Dimension(500, 40));
        pauseMenu.setBackground(Color.BLUE.darker());
        pauseMenu.setBorder(BorderFactory.createLineBorder(Color.white));
        pauseMenu.setFocusable(false); // Prevent the menu from taking focus from the panel
        // Create Pause Menu Label
        JLabel pauseMenuLabel = new JLabel("Press SPACE to Resume");
        pauseMenuLabel.setFont(new Font("Verdana", Font.PLAIN, 18));
        pauseMenuLabel.setForeground(Color.WHITE);
        pauseMenuLabel.setAlignmentX(CENTER_ALIGNMENT);
        pauseMenuLabel.setAlignmentY(CENTER_ALIGNMENT);
        pauseMenu.add(pauseMenuLabel);
        pauseMenu.setVisible(true);
    }

    public static void resumeGame() {
        pause = false;
        running = true;
        pauseMenu.setVisible(false);
    }

    public static void quitGame() {
        System.exit(0);
    }

    public static void resetPlayer() {
        playerXLocation = 200;
        playerYLocation = 200;
        direction = 'R';
        oldDirection = 'R';
    }

    // Method to update the player's coordinates
    public static void movePlayer() {
        // Change position of the head using the direction variable (which is updated by the keyboard)
        switch(direction) {
            case 'R':
                playerXLocation = playerXLocation + UNIT_SIZE;
                oldDirection = direction;
                break;
            case 'L':
                playerXLocation = playerXLocation - UNIT_SIZE;
                oldDirection = direction;
                break;
            case 'U':
                playerYLocation = playerYLocation - UNIT_SIZE; //coordinates are backwards compared to a regular map
                oldDirection = direction;
                break;
            case 'D':
                playerYLocation = playerYLocation + UNIT_SIZE;
                oldDirection = direction;
                break;
        }
    }

    // Check if the units that comprise the image collide
    public static void checkCopCollision() {
        if(playerXLocation == copXLocation
                && (playerYLocation == copYLocation
                    || playerYLocation == copYLocation+UNIT_SIZE // car is 2 unit sizes
                    || playerYLocation == copYLocation-UNIT_SIZE)) {
            generateNewCopLocation();
            System.out.println("COP COLLISION");
        }
    }

    public static void checkStopWatch() {
        long now = System.currentTimeMillis();
        int elapsedTime = (int) (now - startTime); // Convert timestamp difference to seconds
        elapsedMins = (int) Math.floor(elapsedTime / 1000 / 60);
        elapsedSecondsRemainder = (int) Math.floor(elapsedTime / 1000 % 60);
    }

    // Append final score to text file
    public static void logFinalScore() {
        try {
            // Create or append file
            FileWriter fw = new FileWriter("/Users/aaroncorona/eclipse-workspace/GTA/src/assets/gta_high_scores.csv", true); // FileWriter append mode is triggered with true (also creates new file if none exists)
            PrintWriter write = new PrintWriter(fw);
            // Print the score to the csv file and the time on the column next to it
            write.println(); // Skip to new row
            write.print(finalScore);
            write.print(","); // comma separate to print to the next column
            write.print(System.currentTimeMillis()); // print current date
            // Close and finish the job
            write.close();
        } catch(IOException e){
            System.out.print(e);
        }
    }

    // Read high score file
    public static void generateHighScoreArray() {
        // Reset/create Array
        highScoreArray = new long[5000][2];
        try {
            // Create file object
            File fileObj = new File("/Users/aaroncorona/eclipse-workspace/GTA/src/assets/gta_high_scores.csv");
            // Create scanner object
            Scanner myScanner = new Scanner(fileObj);
            myScanner.useDelimiter("\\n|,|\\s*\\$"); // Treats commas and whitespace as dilimiters to read the csv properly (\n = line break, s = whitespace, $ = until end of line, an anchor to ensure that the entire string is matched instead of just a substring). Reads results as string
            // Fill Array by reading the file
            for(int i = 0; myScanner.hasNext(); i++) { // loop for rows
                for(int a = 0; a <= 1; a++) { // loop for columns
                    highScoreArray[i][a] = ((long)Long.parseLong(myScanner.next().trim()));
                }
            }
            // Sort Array in ascending order
            Arrays.sort(highScoreArray, Comparator.comparingDouble(a -> a[0])); // Lamba function that compares numbers
            myScanner.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred finding the file.");
            e.printStackTrace();
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(running) {
            movePlayer();
            checkCopCollision();
            checkStopWatch();
            repaint();
        }

        // When the game ends, log the final score if the game ends with a minimum score achieved
        if(running == false && pause == false && initialPause == false && stopHs == false) {

            // Update the final score variable
            finalScore = money;

            // Log final score in the CSV file if it's past a certain minimum
            if(finalScore >= 20) {
                logFinalScore();
            }

            // Generate Array of Scores from the current CSV file
            generateHighScoreArray();

            // Get info on the top 3 scores
            int score1 = (int) highScoreArray[highScoreArray.length-1][0]; //Array is sorted in ascending order
            Timestamp ts1 = new Timestamp(highScoreArray[highScoreArray.length - 1][1]);
            int score2 = (int) highScoreArray[highScoreArray.length-2][0];
            Timestamp ts2 = new Timestamp(highScoreArray[highScoreArray.length-2][1]);
            int score3 = (int) highScoreArray[highScoreArray.length-3][0];
            Timestamp ts3 = new Timestamp(highScoreArray[highScoreArray.length-3][1]);

            // Special message if the player reached a top 3 high score
            System.out.println("* Your final score is " + finalScore); // Array is sorted in ascending order
            if(finalScore > score3 && finalScore >= 20) {
                System.out.println("* CONGRATS! That's a new high score. That puts you at top 3 all time."); // Array is sorted in ascending order
            } else {
                System.out.println("* Sorry, your score was not good enough for top 3 all time."); // Array is sorted in ascending order
            }

            // Show top 3 high scores and the times they were achieved
            System.out.println("1st place: " + score1 + " on " + ts1);
            System.out.println("2nd place: " + score2 + " on " + ts2);
            System.out.println("3rd place: " + score3 + " on " + ts3);

            // End this process
            stopHs = true;

            // Lose game Menu
            if(running == false && pause == false && initialPause == false) {
                // Create Game Over Menu
                gameOverMenu.setLocation(600,300);
                gameOverMenu.setBackground(Color.red);
                gameOverMenu.setBorder(BorderFactory.createLineBorder(Color.white));
                gameOverMenu.setFocusable(false); // Prevent the menu from taking focus from the panel

                // Create High Score Menu
                highScoreMenu = new JPopupMenu();
                highScoreMenu.setLocation(1000,60);
                highScoreMenu.setBackground(Color.orange.darker());
                highScoreMenu.setBorder(BorderFactory.createLineBorder(Color.white));
                highScoreMenu.setFocusable(false); // Prevent the menu from taking focus from the panel
                highScoreMenuLabel.setFont(new Font("Verdana", Font.PLAIN, 30)); // Buffer
                highScoreMenuLabel.setForeground(Color.WHITE);
                highScoreMenuLabel.setAlignmentX(CENTER_ALIGNMENT);
                highScoreMenuLabel.setAlignmentY(CENTER_ALIGNMENT);
                highScoreMenu.add(highScoreMenuLabel);
                highScoreMenu.setVisible(true); // Prevent the menu from taking focus from the panel
            }

            // Continually rerun the graphics
            repaint();
        }
    }

    // Define actions to be performed (these map to key strokes)
    public static class RightAction extends AbstractAction {
        @Override
        public void actionPerformed(ActionEvent e) {
            // Set Direction based on key input
            direction = 'R';
        }
    }
    public static class LeftAction extends AbstractAction {
        @Override
        public void actionPerformed(ActionEvent e) {
            // Set Direction based on key input
            direction = 'L';
        }
    }
    public static class UpAction extends AbstractAction {
        @Override
        public void actionPerformed(ActionEvent e) {
            // Set Direction based on key input
            direction = 'U';
        }
    }
    public static class DownAction extends AbstractAction {
        @Override
        public void actionPerformed(ActionEvent e) {
            // Set Direction based on key input
            direction = 'D';
        }
    }
    public static class EnterAction extends AbstractAction {
        @Override
        public void actionPerformed(ActionEvent e) {
            // Enter key to restart game (if stopped)
            if (running == false) {
                startGame(); // Restart
            }
        }
    }
    public static class DeleteAction extends AbstractAction {
        @Override
        public void actionPerformed(ActionEvent e) {
            // Delete key to quit game (if stopped)
            if (running == false){
                quitGame();
            }
        }
    }
    public static class SpaceAction extends AbstractAction {
        @Override
        public void actionPerformed(ActionEvent e) {
            // Space bar to pause or resume game
            if (pause == false && initialPause == false && running == true) {
                pauseGame();
            } else if (pause == true && initialPause == false && running == false) {
                resumeGame();
            }
        }
    }
    public static class rAction extends AbstractAction {
        @Override
        public void actionPerformed(ActionEvent e) {
            // The r key toggles nitro activation
            if(nitro == false) {
                myTimer.setDelay(GAME_SPEED_NITRO);
                nitro = true;
            } else{
                myTimer.setDelay(GAME_SPEED);
                nitro = false;
            }
        }
    }
}




/*
AIs
1) Add Car gridpoints
2) Add Cop gridpoints
3) grid - create road tiles
4) grid - create building tiles
5) grid - create road end tiles on the walls
6) grid Physics - car crashes on building or road end
7) grid Physics - car and cop do not respawn on building
8) bullet that comes out of car based on dir
9) bullet movement goes offscrean unless it hits NPC
10) NPC "dies" by disappearing into money dropped
11) Player collects money for high score (random amount of money)
12) Lose menu shows WASTED
13) menus

Ideas:
cop shoots randomly?
Data structure: Queue for movements?
Data structure: HashMap to map NPC to alive status?
Data structure: HashMap to map high score int to time or name?
 */
