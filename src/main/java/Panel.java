
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
    static final int SCREEN_WIDTH = 1400;
    static final int SCREEN_HEIGHT = 900;
    static final int UNIT_SIZE = 25;
    static final int PLAYER_UNIT_SIZE = UNIT_SIZE*2; // player takes up 2 by 2 units (4 total)
    static final int ROAD_SIZE = UNIT_SIZE*5;
    static final int WATER_SIZE = UNIT_SIZE*2;
    static final int SIDEWALK_SIZE = UNIT_SIZE;

    // Constants for building dimensions
    static final int BUILDING_WIDTH = 160;
    static final int b1XStart = WATER_SIZE + SIDEWALK_SIZE + ROAD_SIZE + UNIT_SIZE+20;
    static final int b1YStart = WATER_SIZE + SIDEWALK_SIZE + ROAD_SIZE + UNIT_SIZE;

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
    static int[][] grid; // tracks all background particles (non-player)

    // Menus
    public static JPopupMenu pauseMenu = new JPopupMenu();
    public static JPopupMenu gameOverMenu = new JPopupMenu();
    public static JPopupMenu highScoreMenu = new JPopupMenu();

    // Create game panel (constructor)
    Panel() {
        // Add Panel details
        this.setBackground(Color.LIGHT_GRAY);
        this.setPreferredSize(new Dimension(SCREEN_WIDTH,SCREEN_HEIGHT));
        this.setFocusable(true);

        // Add Game Timer
        // Note: timer creates a new panel object (this) after every delay time
        timer = new Timer(100, this);
        timer.start();

        // Add Particle details
        fillGrid();

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
        // Reset player and cop locations
        generateNewPlayerLocation();
        generateNewCopLocation();

        // Reset trigger variables
        running = true;
        pause = false;
        nitro = false;

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
            checkBuildingCollision();
            checkGameOver();
            repaint();
        }
    }

    public static void fillGrid() {
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
                // Building #1 area
                else if(i >= b1XStart
                        && i <= b1XStart + BUILDING_WIDTH
                        && j >= b1YStart
                        && j <= b1YStart + BUILDING_WIDTH-20) {
                    grid[i][j] = 3;
                }
            }
        }
    }

    @ Override
    // Runs automatically on Frame rendering to execute drawEverything()
    public void paint(Graphics g) {
        super.paint(g);

        // Paint panel based on the particle mapping from the grid
        for(int i = 0; i < grid.length; i++) {
            for(int j = 0; j < grid[i].length; j++) {
                // Draw Water
                if(grid[i][j] == 1) {
                    g.setColor(Color.BLUE.darker());
                    g.fillRect(i, j, WATER_SIZE, WATER_SIZE);
                }
                // Draw Roads
                else if(grid[i][j] == 2) {
                    g.setColor(Color.GRAY.darker());
                    g.fillOval(i, j, ROAD_SIZE, ROAD_SIZE);
                }
                // For ad hoc building checks
//                else if(grid[i][j] == 3) {
//                    g.setColor(Color.RED.darker());
//                    g.fillOval(i, j, 1, 1);
//                }
            }
        }

        // Draw street lines
        Graphics2D g2d = (Graphics2D) g.create();
        Stroke dashed = new BasicStroke(3, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL,
                0, new float[]{9}, 0);
        g2d.setColor(Color.yellow);
        g2d.setStroke(dashed);
        // Horizontal
        g2d.drawLine(150, 140, SCREEN_WIDTH - 160, 140);
        g2d.drawLine(150, SCREEN_HEIGHT - 160, SCREEN_WIDTH - 160, SCREEN_HEIGHT - 160);
        // Vertical
        g2d.drawLine(140, 220, 140, SCREEN_HEIGHT - 240);
        g2d.drawLine(SCREEN_WIDTH - 160, 220, SCREEN_WIDTH - 160, SCREEN_HEIGHT - 240);
        g2d.drawLine(515, 220, 515, SCREEN_HEIGHT - 240);
        g2d.drawLine(860, 220, 860, SCREEN_HEIGHT - 240);
        g2d.dispose();

        // Draw building 1
        String filePathB1 = "/Users/aaroncorona/eclipse-workspace/GTA/src/assets/images/building1.png";
        ImageIcon b1 = new ImageIcon(new ImageIcon(filePathB1).getImage()
                .getScaledInstance(BUILDING_WIDTH, BUILDING_WIDTH-30, Image.SCALE_DEFAULT));
        b1.paintIcon(this, g, b1XStart, b1YStart);

        // Draw building 2
        String filePathB2 = "/Users/aaroncorona/eclipse-workspace/GTA/src/assets/images/building2.png";
        ImageIcon b2 = new ImageIcon(new ImageIcon(filePathB2).getImage().getScaledInstance(BUILDING_WIDTH-40, BUILDING_WIDTH-40, Image.SCALE_DEFAULT));
        b2.paintIcon(this, g,
                        WATER_SIZE + SIDEWALK_SIZE + ROAD_SIZE + UNIT_SIZE + 40,
                        WATER_SIZE + SIDEWALK_SIZE + ROAD_SIZE + UNIT_SIZE + BUILDING_WIDTH);

        // Draw building 3
        String filePathB3 = "/Users/aaroncorona/eclipse-workspace/GTA/src/assets/images/building3.png";
        ImageIcon b3 = new ImageIcon(new ImageIcon(filePathB3).getImage().getScaledInstance(BUILDING_WIDTH, BUILDING_WIDTH-40, Image.SCALE_DEFAULT));
        b3.paintIcon(this, g,
                        WATER_SIZE + SIDEWALK_SIZE + ROAD_SIZE + UNIT_SIZE + 20,
                        WATER_SIZE + SIDEWALK_SIZE + ROAD_SIZE + UNIT_SIZE + BUILDING_WIDTH*2 - 10);

        // Draw building 4
        String filePathB4 = "/Users/aaroncorona/eclipse-workspace/GTA/src/assets/images/building4.png";
        ImageIcon b4 = new ImageIcon(new ImageIcon(filePathB4).getImage().getScaledInstance(BUILDING_WIDTH, BUILDING_WIDTH+135, Image.SCALE_DEFAULT));
        b4.paintIcon(this, g,
                        SCREEN_WIDTH/4 + WATER_SIZE + SIDEWALK_SIZE + ROAD_SIZE + UNIT_SIZE + 35,
                        WATER_SIZE + SIDEWALK_SIZE + ROAD_SIZE + UNIT_SIZE);

        // Draw building 5 (b1 again)
        b1.paintIcon(this, g,
                        SCREEN_WIDTH/4 + WATER_SIZE + SIDEWALK_SIZE + ROAD_SIZE + UNIT_SIZE + 35,
                        WATER_SIZE + SIDEWALK_SIZE + ROAD_SIZE + UNIT_SIZE + BUILDING_WIDTH*2 - 10);

        // Draw building 6
        String filePathB6 = "/Users/aaroncorona/eclipse-workspace/GTA/src/assets/images/building6.png";
        ImageIcon b6 = new ImageIcon(new ImageIcon(filePathB6).getImage().getScaledInstance(BUILDING_WIDTH, BUILDING_WIDTH, Image.SCALE_DEFAULT));
        b6.paintIcon(this, g,
                        SCREEN_WIDTH/2 + WATER_SIZE + SIDEWALK_SIZE + ROAD_SIZE + UNIT_SIZE + 40,
                        WATER_SIZE + SIDEWALK_SIZE + ROAD_SIZE + UNIT_SIZE);

        // Draw building 7
        String filePathB7 = "/Users/aaroncorona/eclipse-workspace/GTA/src/assets/images/building7.png";
        ImageIcon b7 = new ImageIcon(new ImageIcon(filePathB7).getImage().getScaledInstance(BUILDING_WIDTH, BUILDING_WIDTH, Image.SCALE_DEFAULT));
        b7.paintIcon(this, g,
                SCREEN_WIDTH/2 + WATER_SIZE + SIDEWALK_SIZE + ROAD_SIZE + UNIT_SIZE + 40,
                WATER_SIZE + SIDEWALK_SIZE + ROAD_SIZE + UNIT_SIZE + BUILDING_WIDTH + 15);

        // Draw building 8
        String filePathB8 = "/Users/aaroncorona/eclipse-workspace/GTA/src/assets/images/building8.png";
        ImageIcon b8 = new ImageIcon(new ImageIcon(filePathB8).getImage().getScaledInstance(BUILDING_WIDTH, BUILDING_WIDTH - 70, Image.SCALE_DEFAULT));
        b8.paintIcon(this, g,
                SCREEN_WIDTH/2 + WATER_SIZE + SIDEWALK_SIZE + ROAD_SIZE + UNIT_SIZE + 40,
                WATER_SIZE + SIDEWALK_SIZE + ROAD_SIZE + UNIT_SIZE + BUILDING_WIDTH*2 + 30);

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
            if(direction == 'e') { // make explosions larger
                ImageIcon player = new ImageIcon(new ImageIcon(filePathCar).getImage().getScaledInstance(PLAYER_UNIT_SIZE*2, PLAYER_UNIT_SIZE*2, Image.SCALE_DEFAULT));
                player.paintIcon(this, g, playerXLocation - UNIT_SIZE, playerYLocation - UNIT_SIZE);
            } else {
                ImageIcon player = new ImageIcon(new ImageIcon(filePathCar).getImage().getScaledInstance(PLAYER_UNIT_SIZE, PLAYER_UNIT_SIZE, Image.SCALE_DEFAULT));
                player.paintIcon(this, g, playerXLocation - UNIT_SIZE, playerYLocation - UNIT_SIZE);
            }
            // Draw the cop
            String filePathCop = "/Users/aaroncorona/eclipse-workspace/GTA/src/assets/images/cop.png";
            ImageIcon cop = new ImageIcon(new ImageIcon(filePathCop).getImage().getScaledInstance(PLAYER_UNIT_SIZE, PLAYER_UNIT_SIZE, Image.SCALE_DEFAULT));
            cop.paintIcon(this, g, copXLocation - 20, copYLocation - 20);
            // Display current score (money)
            g.setColor(Color.GREEN.brighter());
            g.setFont(new Font("Serif", Font.PLAIN, 50));
            g.drawString("Bank Account: $" + money,20,40); // coordinates start in the top left
            // Display stop watch
            g.setColor(Color.WHITE);
            g.setFont(new Font("Serif", Font.PLAIN, 25));
            g.drawString(getTimePassed(),20,70);
            // TODO - Only display the control menu when the game is paused
            // Display control menu
//            int menuSize = 320;
//            String filePathControlMenu = "/Users/aaroncorona/eclipse-workspace/GTA/src/assets/images/control_menu.png";
//            ImageIcon controlMenu = new ImageIcon(new ImageIcon(filePathControlMenu).getImage().getScaledInstance(menuSize, menuSize, Image.SCALE_DEFAULT));
//            controlMenu.paintIcon(this, g, SCREEN_WIDTH-menuSize-10, 10);
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
        // Change position of the player using the direction variable
        int spacesToMove;
        if(nitro == true) {
            spacesToMove = UNIT_SIZE*2; // 200% speed
        } else {
            spacesToMove = UNIT_SIZE;
        }
        switch(direction) {
            case 'R':
                playerXLocation = playerXLocation + spacesToMove;
                oldDirection = direction;
                break;
            case 'L':
                playerXLocation = playerXLocation - spacesToMove;
                oldDirection = direction;
                break;
            case 'U':
                playerYLocation = playerYLocation - spacesToMove;
                oldDirection = direction;
                break;
            case 'D':
                playerYLocation = playerYLocation + spacesToMove;
                oldDirection = direction;
                break;
        }
    }

    // Check if the units that comprise the image for the Car and Cop collide
    public static void checkCopCollision() {
        if(Math.abs(playerXLocation - copXLocation) <= UNIT_SIZE
           && Math.abs(playerYLocation - copYLocation) <= UNIT_SIZE) {
            direction = 'e'; // triggers explosion
            generateNewCopLocation();
            running = false;
        }
    }

    public static void checkWallCollision() {
        // Check for the player colliding with one of the 4 borders
        if(playerXLocation < PLAYER_UNIT_SIZE
                || playerXLocation > SCREEN_WIDTH - PLAYER_UNIT_SIZE*2
                || playerYLocation < PLAYER_UNIT_SIZE
                || playerYLocation > SCREEN_HEIGHT - PLAYER_UNIT_SIZE*2) {
            direction = ' '; // car disappears
            running = false; // stop the game (which triggers end game message)
            System.out.println("* GAME OVER (Out of Bounds)");
        }
    }

    public static void checkBuildingCollision() {
        // Check for the player colliding with one of the buildings
        if(grid[playerXLocation][playerYLocation] == 3) {
            running = false;
            direction = 'e';
            System.out.println("* GAME OVER (Building Collision)");
        }
    }

    public static void checkGameOver() {
        // When the game ends, log the final score if the game ends with a minimum score achieved
        if(running == false && pause == false) {

            // Log final score in the CSV file if it's past a certain minimum
            if (money >= 20) {
                logFinalScore(money);
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
            highScoreMenu.setLocation(450, 450);
            highScoreMenu.setBackground(new Color(255, 105, 97));
            highScoreMenu.setBorder(BorderFactory.createLineBorder(new Color(255, 105, 97), 30));
            highScoreMenu.setFocusable(false);
            JLabel highScoreMenuLabel = new JLabel(getHighScoreMessage());
            highScoreMenuLabel.setFont(new Font("Verdana", Font.PLAIN, 30)); // Buffer
            highScoreMenuLabel.setForeground(Color.BLACK);
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
        // Create Array to hold the scores
        // TODO -- Use TreeMap instead for connecting unique scores to a timestamp or name.
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
        message = "<html> Final Score: <b>" + money + "</b><br>";
        if (money > score1) {
            message += "CONGRATS! You have the all time best score! <br>";
        }
        else if (money > score3) {
            message += "CONGRATS! That's a new high score. You are top 3 all time <br>";
        } else{
            message += "<i>Your score is not top 3 all time</i><br>";
        }
        // Add the top 3 high scores
        message += ("<u>1st place</u>: <b>" + score1 + "</b> on " + ts1 + "<br>");
        message += ("<u>2nd place</u>: <b>" + score2 + "</b> on " + ts2 + "<br>");
        message += ("<u>3rd place</u>: <b>" + score3 + "</b> on " + ts3 + "</html>");
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
            } else{
                nitro = false;
            }
        }
    }
}


/*
Aaron AIs
1) grid Physics - car crashes on building or road, which ends the game
2) grid Physics - car and cop do not respawn on building
3) bullet that comes out of car based on dir
4) bullet that comes from cop randomly
5) bullet movement goes offscreen unless it hits NPC
6) Cop "dies" by disappearing and turning into money (random amount)
7) Player collects money by driving over it for high score
8) player blows up when dying from contact with a bullet, building, or screen ending
9) add environment icon like trees or citizens for a better UI
10) update readme
 */

// For Ad hoc checks
//            g.setColor(Color.RED);
//                    g.fillRect(copXLocation, copYLocation, 20, 20);
//                    g.fillRect(playerXLocation, playerYLocation, 20, 20);