
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

    // Constants for particle sizes
    static final int UNIT_SIZE = 25;
    static final int PLAYER_UNIT_SIZE = UNIT_SIZE*2; // player takes up 2 by 2 units (4 total)
    static final int ROAD_SIZE = UNIT_SIZE*6;
    static final int WATER_SIZE = UNIT_SIZE*2;
    static final int SIDEWALK_SIZE = UNIT_SIZE;
    static final int SCREEN_WIDTH = 1400;
    static final int SCREEN_HEIGHT = 900;

    // Constants for game speed
    static final int GAME_SPEED = UNIT_SIZE*10;
    static final int GAME_SPEED_NITRO = GAME_SPEED / 3;

    // Helper variables to trigger events
    static boolean running;
    static boolean pause;
    static boolean nitro = false;

    // Helper variables to track dynamic data that needs a global scope
    static int money;
    static char direction;
    static char oldDirection;
    static long startTime;
    static Timer timer;

    // Variables to track graphics
    static int playerXLocation;
    static int playerYLocation;
    static int copXLocation;
    static int copYLocation;
    static int[][] grid; // tracks all non-player particles

    // Menus
    public static JPopupMenu pauseMenu = new JPopupMenu();
    public static JPopupMenu gameOverMenu = new JPopupMenu();
    public static JPopupMenu highScoreMenu = new JPopupMenu();

    // Create game panel (constructor)
    Panel() {
        // Panel details
        this.setBackground(Color.LIGHT_GRAY);
        this.setPreferredSize(new Dimension(SCREEN_WIDTH,SCREEN_HEIGHT));
        this.setFocusable(true);

        // Add Game Timer
        // Note: timer creates a new panel object (this) after every delay time
        timer = new Timer(GAME_SPEED, this);
        timer.start();

        // Map Key Events in the Panel to Action response classes
        this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
                .put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0, false),"rightAction"); // KeyEvent responds to a right key, lower case, and false for it being pressed rather than released
        this.getActionMap().put("rightAction", new RightAction());
        this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
                .put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0, false),"leftAction");
        this.getActionMap().put("leftAction", new LeftAction());
        this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
                .put(KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0, false),"upAction");
        this.getActionMap().put("upAction", new UpAction());
        this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
                .put(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0, false),"downAction");
        this.getActionMap().put("downAction", new DownAction());
        this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
                .put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0, false),"enterAction");
        this.getActionMap().put("enterAction", new EnterAction());
        this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
                .put(KeyStroke.getKeyStroke(KeyEvent.VK_BACK_SPACE, 0, false),"deleteAction");
        this.getActionMap().put("deleteAction", new DeleteAction());
        this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
                .put(KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, 0, false),"spaceAction");
        this.getActionMap().put("spaceAction", new SpaceAction());
//        this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
//                .put(KeyStroke.getKeyStroke(KeyEvent.VK_E, 0, false),"eAction");
//        this.getActionMap().put("eAction", new eAction());
        this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_R, 0, false),"rAction");
        this.getActionMap().put("rAction", new rAction());
    }

    public static void startGame() {
        // Reset player and NPC
        generateNewPlayerLocation();
        generateNewCopLocation();

        // Reset trigger variables
        running = true;
        pause = false;
        nitro = false;
        timer.setDelay(GAME_SPEED);

        // Hide menus that may be open upon restarting
        pauseMenu.setVisible(false);
        gameOverMenu.setVisible(false);
        highScoreMenu.setVisible(false);

        // Restart money at 1
        money = 1;

        // Start stopwatch
        startTime = System.currentTimeMillis();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // Continually run these functions
        if(running) {
            movePlayer();
            checkCopCollision();
            checkWallCollision();
            checkGameOver();
            repaint();
        }
    }

    @ Override
    // Runs automatically on Frame rendering to execute drawEverything()
    public void paint(Graphics g) {
        super.paint(g);

        /* Fill grid with this particle mapping
           0 = sidewalk
           1 = water
           2 = road
           3 = building
         */
        grid = new int[SCREEN_WIDTH][SCREEN_HEIGHT];
        for(int i = 0; i < grid.length; i++) {
            for(int j = 0; j < grid[i].length; j++) {
                // Water on screen edges (for island effect)
                if(i == 0
                        || i == SCREEN_WIDTH - WATER_SIZE
                        || j == 0
                        || j == SCREEN_HEIGHT - WATER_SIZE) {
                    grid[i][j] = 1;
                }
                // Sidewalk around the water edges
                else if(i < WATER_SIZE + SIDEWALK_SIZE
                        || i >  SCREEN_WIDTH - (WATER_SIZE*4) - SIDEWALK_SIZE
                        || j < WATER_SIZE + SIDEWALK_SIZE
                        || j >  SCREEN_HEIGHT - (WATER_SIZE*4) - SIDEWALK_SIZE) {
                    grid[i][j] = 0;
                }
                // Roads (vertical)
                else if(i == WATER_SIZE + SIDEWALK_SIZE // far left road
                        || i == SCREEN_WIDTH/4 + WATER_SIZE + SIDEWALK_SIZE*2 // 2nd road
                        || i == SCREEN_WIDTH/4 + SCREEN_WIDTH/4 + WATER_SIZE + SIDEWALK_SIZE*2 // 3rd road
                        || i == SCREEN_WIDTH - WATER_SIZE*4 - SIDEWALK_SIZE) { // far right road
                    grid[i][j] = 2;
                }
                // Roads (horizontal)
                else if(j == WATER_SIZE + SIDEWALK_SIZE // top road
                        || j == SCREEN_HEIGHT - (WATER_SIZE*4) - SIDEWALK_SIZE) { // bottom road
                    grid[i][j] = 2;
                }
            }
        }

        // Paint panel based on the particle mapping from the grid
        for(int i = 0; i < grid.length; i++) {
            for(int j = 0; j < grid[i].length; j++) {
                if(grid[i][j] == 1) {
                    g.setColor(Color.BLUE.darker());
                    g.fillRect(i, j, WATER_SIZE, WATER_SIZE);
                }
                else if(grid[i][j] == 2) {
                    g.setColor(Color.GRAY.darker());
                    g.fillOval(i, j, ROAD_SIZE, ROAD_SIZE);
                }
            }
        }

        // Initial Pause menu
        if(running == false && money == 0) {
            // Draw the pause menu
            String filePathStartMenu = "/Users/aaroncorona/eclipse-workspace/GTA/src/assets/images/start_menu.png";
            ImageIcon startMenu = new ImageIcon(new ImageIcon(filePathStartMenu).getImage().getScaledInstance((SCREEN_WIDTH/2)+50, SCREEN_HEIGHT-50, Image.SCALE_DEFAULT));
            startMenu.paintIcon(this, g, SCREEN_WIDTH/4, 10);
            // Draw text
            g.setColor(Color.ORANGE.brighter());
            g.setFont(new Font("Serif", Font.ITALIC, 50));
            g.drawString("Press ENTER to Play",(SCREEN_WIDTH/4)+150,660);
        }
        // Image Icons - draw this after the game starts
        else {
            // Draw the car icon based on the movement direction so it faces the correct way
            String filePathCar = "/Users/aaroncorona/eclipse-workspace/GTA/src/assets/images/car_" + direction + ".png";
            ImageIcon player = new ImageIcon(new ImageIcon(filePathCar).getImage().getScaledInstance(PLAYER_UNIT_SIZE, PLAYER_UNIT_SIZE, Image.SCALE_DEFAULT));
            player.paintIcon(this, g, playerXLocation, playerYLocation);
            // Draw the cop
            String filePathCop = "/Users/aaroncorona/eclipse-workspace/GTA/src/assets/images/cop.png";
            ImageIcon cop = new ImageIcon(new ImageIcon(filePathCop).getImage().getScaledInstance(PLAYER_UNIT_SIZE, PLAYER_UNIT_SIZE, Image.SCALE_DEFAULT));
            cop.paintIcon(this, g, copXLocation, copYLocation);
            // Display current score (money)
            g.setColor(Color.GREEN.brighter());
            g.setFont(new Font("Serif", Font.PLAIN, 50));
            g.drawString("Bank Account: $" + money,105,120); // coordinates start in the top left
            // Display stop watch
            g.setColor(Color.WHITE);
            g.setFont(new Font("Serif", Font.PLAIN, 25));
            g.drawString(getTimePassed(),105,150);
            // TODO - Only display the control menu when the game is paused? or keep it as a building?
            // Display control menu
            int menuSize = 320;
            String filePathControlMenu = "/Users/aaroncorona/eclipse-workspace/GTA/src/assets/images/control_menu.png";
            ImageIcon controlMenu = new ImageIcon(new ImageIcon(filePathControlMenu).getImage().getScaledInstance(menuSize, menuSize, Image.SCALE_DEFAULT));
            controlMenu.paintIcon(this, g, SCREEN_WIDTH-menuSize-10, 10);
            // To see all units for ad hoc checks
//            for(int i = 0;i<SCREEN_HEIGHT/UNIT_SIZE;i++) {
//                g.drawLine(i*UNIT_SIZE,0,i*UNIT_SIZE,SCREEN_HEIGHT);
//                g.drawLine(0,i*UNIT_SIZE,SCREEN_WIDTH,i*UNIT_SIZE);
//            }
        }
    }

    public static void pauseGame() {
        pause = true;
        running = false;
        pauseMenu = new JPopupMenu();
        pauseMenu.setLocation(600, 400);
        pauseMenu.setPreferredSize(new Dimension(500, 40));
        pauseMenu.setBackground(Color.WHITE);
        pauseMenu.setBorder(BorderFactory.createLineBorder(Color.white));
        pauseMenu.setFocusable(false); // Prevent the menu from taking focus from the panel
        // Create Pause Menu Label
        JLabel pauseMenuLabel = new JLabel("Press SPACE to Resume");
        pauseMenuLabel.setFont(new Font("Verdana", Font.PLAIN, 18));
        pauseMenuLabel.setForeground(Color.BLACK);
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

    public static void generateNewPlayerLocation() {
        playerXLocation = WATER_SIZE + ROAD_SIZE - UNIT_SIZE; // spawn on the first road
        playerYLocation = WATER_SIZE + ROAD_SIZE - UNIT_SIZE;
        direction = 'R';
        oldDirection = 'R';
    }

    public static void generateNewCopLocation() { // populates new coordinates (int variable values) for a cop, which is then created with draw (graphics object)
        copXLocation = new Random().nextInt((int) (SCREEN_WIDTH/UNIT_SIZE)) * UNIT_SIZE; //get random coordinate within the boundary of the unit fully fitting (use division), while also being an exact coordinate (i.e. divisable by the unit size, so use multiplication)
        copYLocation = new Random().nextInt((int) (SCREEN_HEIGHT/UNIT_SIZE)) * UNIT_SIZE; //get random coordinate within the boundary of the unit fully fitting (use division), while also being an exact coordinate (i.e. divisable by the unit size, so use multiplication)
        // Generate a new location if the cop spawns on the wall (for improved UX)
        if(copXLocation == 0 || copYLocation == 0
                || copXLocation == SCREEN_WIDTH || copYLocation == SCREEN_HEIGHT) {
            generateNewCopLocation();
        }
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

    // Check if the units that comprise the image for the Car and Cop collide
    public static void checkCopCollision() {
        if(playerXLocation == copXLocation
                && (playerYLocation == copYLocation
                    || playerYLocation == copYLocation+UNIT_SIZE // car is 2 unit sizes
                    || playerYLocation == copYLocation-UNIT_SIZE)) {
            generateNewCopLocation();
        }
    }

    public static void checkWallCollision() {
        // check for the head colliding with one of the 4 borders
        if(playerXLocation < PLAYER_UNIT_SIZE
                || playerXLocation > SCREEN_WIDTH - PLAYER_UNIT_SIZE*2
                || playerYLocation < PLAYER_UNIT_SIZE
                || playerYLocation > SCREEN_HEIGHT - PLAYER_UNIT_SIZE*2) {
            running = false; // stop the game (which triggers end game message)
            System.out.println("* GAME OVER (Out of Bounds)");
        }
    }

    public static void checkGameOver() {
        // When the game ends, log the final score if the game ends with a minimum score achieved
        if(running == false && pause == false) {

            // Log final score in the CSV file if it's past a certain minimum
            if (money >= 20) {
                logFinalScore(money);
            }

            // Print Score results
            System.out.println(getHighScoreMessage());

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
            highScoreMenu.setLocation(710, 450);
            highScoreMenu.setBackground(Color.orange);
            highScoreMenu.setBorder(BorderFactory.createLineBorder(Color.white));
            highScoreMenu.setFocusable(false);
            JLabel highScoreMenuLabel = new JLabel("High Scores:");
            highScoreMenuLabel.setFont(new Font("Verdana", Font.PLAIN, 30)); // Buffer
            highScoreMenuLabel.setForeground(Color.WHITE);
            highScoreMenuLabel.setAlignmentX(CENTER_ALIGNMENT);
            highScoreMenuLabel.setAlignmentY(CENTER_ALIGNMENT);
            highScoreMenu.add(highScoreMenuLabel);
            highScoreMenu.setVisible(true);
        }
    }

    public static String getTimePassed() {
        long now = System.currentTimeMillis();
        int elapsedTime = (int) (now - startTime); // Convert timestamp difference to seconds
        int elapsedMins = (int) Math.floor(elapsedTime / 1000 / 60);
        int elapsedSecondsRemainder = (int) Math.floor(elapsedTime / 1000 % 60);
        return elapsedMins + " Mins and " + elapsedSecondsRemainder + " Seconds";
    }

    // Read high score file
    public static String getHighScoreMessage() {
        // Reset/create Array
        // TODO -- Change to TreeMap for connecting unique scores to a timestamp or name.
        long[][] highScoreArray = new long[5000][2];
        try {
            // Create file object to extract the high score CSV
            File fileObj = new File("/Users/aaroncorona/eclipse-workspace/GTA/src/assets/gta_high_scores.csv");
            // Create scanner object to read the file
            Scanner myScanner = new Scanner(fileObj);
            myScanner.useDelimiter("\\n|,|\\s*\\$"); // Treats commas and whitespace as dilimiters to read the csv
            // Fill the score Array
            for(int i = 0; myScanner.hasNext(); i++) {
                for(int a = 0; a <= 1; a++) {
                    highScoreArray[i][a] = ((long)Long.parseLong(myScanner.next().trim()));
                }
            }
            myScanner.close();
        } catch (FileNotFoundException e) {
            System.out.println(e);
        }
        // Sort Array in ascending order (note: wont be needed with TreeMap)
        Arrays.sort(highScoreArray, Comparator.comparingDouble(a -> a[0]));
        // Get the top 3 scores
        int score1 = (int) highScoreArray[highScoreArray.length - 1][0];
        Timestamp ts1 = new Timestamp(highScoreArray[highScoreArray.length - 1][1]);
        int score2 = (int) highScoreArray[highScoreArray.length - 2][0];
        Timestamp ts2 = new Timestamp(highScoreArray[highScoreArray.length - 2][1]);
        int score3 = (int) highScoreArray[highScoreArray.length - 3][0];
        Timestamp ts3 = new Timestamp(highScoreArray[highScoreArray.length - 3][1]);
        // Special message if the player reached a top 3 high score
        String message;
        System.out.println("* Your final score is " + money);
        if (money > score1) {
            message = "* CONGRATS! You have the all time best score!\n";
        }
        else if (money > score3) {
            message = "* CONGRATS! That's a new high score. You are top 3 all time\n";
        } else{
            message = "* Sorry, your score was not good enough for top 3 all time\n";
        }
        // Add the top 3 high scores
        message += ("1st place: " + score1 + " on " + ts1 + "\n");
        message += ("2nd place: " + score2 + " on " + ts2 + "\n");
        message += ("3rd place: " + score3 + " on " + ts3 + "\n");
        return message;
    }

    // Append final score to text file
    public static void logFinalScore(int finalScore) {
        try {
            // Create or append file
            FileWriter fw = new FileWriter("/Users/aaroncorona/eclipse-workspace/GTA/src/assets/gta_high_scores.csv", true); // FileWriter append mode is triggered with true (also creates new file if none exists)
            PrintWriter write = new PrintWriter(fw);
            // Print the score to the csv file and the time on the column next to it
            write.println(); // Skip to new row
            write.print(finalScore);
            write.print(","); // comma separate to print to the next column
            write.print(System.currentTimeMillis());
            // Close and finish the job
            write.close();
        } catch(IOException e) {
            System.out.print(e);
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
            if (running == false) {
                System.exit(0);
            }
        }
    }

    public static class SpaceAction extends AbstractAction {
        @Override
        public void actionPerformed(ActionEvent e) {
            // Space bar to pause or resume game
            if (pause == false && running == true) {
                pauseGame();
            } else if (pause == true && running == false) {
                resumeGame();
            }
        }
    }

    public static class rAction extends AbstractAction {
        @Override
        public void actionPerformed(ActionEvent e) {
            // The r key toggles nitro activation
            if(nitro == false) {
                nitro = true;
                timer.setDelay(GAME_SPEED_NITRO);
            } else{
                nitro = false;
                timer.setDelay(GAME_SPEED);
            }
        }
    }
}


/*
AIs
1) grid - create road tiles, lines inside?
2) grid - create building tiles
3) grid - create road end tiles on the walls
4) grid Physics - car crashes on building or road, which ends the game
5) grid Physics - car and cop do not respawn on building
6) bullet that comes out of car based on dir
7) bullet that comes from cop randomly
8) bullet movement goes offscreen unless it hits NPC
9) NPC "dies" by disappearing and turning into money (random amount)
10) Player collects money by driving over it for high score
11) player blows up when dying from contact with a bullet, building, or screen ending
12) add environment icon like trees or citizens for a better UI
13) update readme
 */
