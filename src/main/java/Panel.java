
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
    static final int BUILDING_SIZE = 160; // can vary by picture
    static final int B1_X_START = 245;
    static final int B1_Y_START = 230;
    static final int B2_X_START = B1_X_START;
    static final int B2_Y_START = B1_Y_START + BUILDING_SIZE+10;
    static final int B3_X_START = B1_X_START;
    static final int B3_Y_START = B2_Y_START + BUILDING_SIZE - 10;
    static final int B4_X_START = 610;
    static final int B4_Y_START = B1_Y_START;
    static final int B5_X_START = B4_X_START;
    static final int B5_Y_START = B4_Y_START + BUILDING_SIZE+150;
    static final int B6_X_START = 965;
    static final int B6_Y_START = B1_Y_START;
    static final int B7_X_START = B6_X_START;
    static final int B7_Y_START = B2_Y_START;
    static final int B8_X_START = B6_X_START;
    static final int B8_Y_START = B3_Y_START+20;;

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
    static int[][] backgroundGrid;
    static int[][] bulletGrid;
    static int[][] moneyGrid;

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

        // Initialize background
        fillStartingGrids();
        generateNewCopLocation();

        // Map Key Events in the Panel to Action response classes
        this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
                .put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0, false),"rightAction");
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
        this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
                .put(KeyStroke.getKeyStroke(KeyEvent.VK_E, 0, false),"eAction");
        this.getActionMap().put("eAction", new eAction());
        this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
                .put(KeyStroke.getKeyStroke(KeyEvent.VK_R, 0, false),"rAction");
        this.getActionMap().put("rAction", new rAction());
    }

    // Method to reset all game settings and start the game loop
    public static void startGame() {
        // Reset to default background
        fillStartingGrids();

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
            moveBullet();
            movePlayer();
            checkGameOver();
            repaint();
        }
    }

    // Method to establish all the default background particles (sidewalk, water, road, and building)
    public static void fillStartingGrids() {
        /*
        Background Grid particle mapping:
           0 = empty / sidewalk
           1 = water
           2 = road
           3 = building
         */
        backgroundGrid = new int[SCREEN_WIDTH][SCREEN_HEIGHT];
        for(int i = 0; i < backgroundGrid.length; i++) {
            for(int j = 0; j < backgroundGrid[i].length; j++) {
                // Water on screen edges (for island effect)
                if(i == 0
                        || i == SCREEN_WIDTH - WATER_SIZE
                        || j == 0
                        || j == SCREEN_HEIGHT - WATER_SIZE) {
                    backgroundGrid[i][j] = 1;
                }
                // Sidewalk around the water edges
                else if(i < WATER_SIZE + SIDEWALK_SIZE
                        || i >  SCREEN_WIDTH - (WATER_SIZE*4) - SIDEWALK_SIZE
                        || j < WATER_SIZE + SIDEWALK_SIZE
                        || j >  SCREEN_HEIGHT - (WATER_SIZE*4) - SIDEWALK_SIZE) {
                    backgroundGrid[i][j] = 0;
                }
                // Roads (vertical)
                else if(i == WATER_SIZE + SIDEWALK_SIZE // far left road
                        || i == SCREEN_WIDTH/4 + WATER_SIZE + SIDEWALK_SIZE*2 // 2nd road
                        || i == SCREEN_WIDTH/4 + SCREEN_WIDTH/4 + WATER_SIZE + SIDEWALK_SIZE*2 // 3rd road
                        || i == SCREEN_WIDTH - WATER_SIZE*4 - SIDEWALK_SIZE) { // far right road
                    backgroundGrid[i][j] = 2;
                }
                // Roads (horizontal)
                else if(j == WATER_SIZE + SIDEWALK_SIZE // top road
                        || j == SCREEN_HEIGHT - (WATER_SIZE*4) - SIDEWALK_SIZE) { // bottom road
                    backgroundGrid[i][j] = 2;
                }
                // Building #1 area
                else if(i >= B1_X_START
                        && i <= B1_X_START + BUILDING_SIZE
                        && j >= B1_Y_START
                        && j <= B1_Y_START + BUILDING_SIZE-50) {
                    backgroundGrid[i][j] = 3;
                }
                // Building #2 area
                else if(i >= B2_X_START
                        && i <= B2_X_START + BUILDING_SIZE - 40
                        && j >= B2_Y_START
                        && j <= B2_Y_START + BUILDING_SIZE - 40) {
                    backgroundGrid[i][j] = 3;
                }
                // Building #3 area
                else if(i >= B3_X_START
                        && i <= B3_X_START + BUILDING_SIZE
                        && j >= B3_Y_START
                        && j <= B3_Y_START + BUILDING_SIZE - 40) {
                    backgroundGrid[i][j] = 3;
                }
                // Building #4 area
                else if(i >= B4_X_START
                        && i <= B4_X_START + BUILDING_SIZE
                        && j >= B4_Y_START
                        && j <= B4_Y_START + BUILDING_SIZE + 130) {
                    backgroundGrid[i][j] = 3;
                }
                // Building #5 area
                else if(i >= B5_X_START
                        && i <= B5_X_START + BUILDING_SIZE
                        && j >= B5_Y_START
                        && j <= B5_Y_START + BUILDING_SIZE-60) {
                    backgroundGrid[i][j] = 3;
                }
                // Building #6 area
                else if(i >= B6_X_START
                        && i <= B6_X_START + BUILDING_SIZE
                        && j >= B6_Y_START
                        && j <= B6_Y_START + BUILDING_SIZE) {
                    backgroundGrid[i][j] = 3;
                }
                // Building #7 area
                else if(i >= B7_X_START
                        && i <= B7_X_START + BUILDING_SIZE
                        && j >= B7_Y_START
                        && j <= B7_Y_START + BUILDING_SIZE) {
                    backgroundGrid[i][j] = 3;
                }
                // Building #8 area
                else if(i >= B8_X_START
                        && i <= B8_X_START + BUILDING_SIZE
                        && j >= B8_Y_START
                        && j <= B8_Y_START + BUILDING_SIZE-80) {
                    backgroundGrid[i][j] = 3;
                }
                else{
                    backgroundGrid[i][j] = 0;
                }
            }
        }
        /*
        Bullet grid particle mapping:
        0 = empty
        1 = bullet right moving
        2 = bullet left moving
        3 = bullet up moving
        4 = bullet down moving
        5 = bullet splash
        6 = bullet explosion
        7 = bullet explosion that produces money
         */
        bulletGrid = new int[SCREEN_WIDTH][SCREEN_HEIGHT];
        for(int i = 0; i < bulletGrid.length; i++) {
            for(int j = 0; j < bulletGrid[i].length; j++) {
                bulletGrid[i][j] = 0;
            }
        }
       /*
        Money grid particle mapping:
        0 = empty
        1 = money exists
        */
        moneyGrid = new int[SCREEN_WIDTH][SCREEN_HEIGHT];
        for(int i = 0; i < moneyGrid.length; i++) {
            for(int j = 0; j < moneyGrid[i].length; j++) {
                moneyGrid[i][j] = 0;
            }
        }
    }

    @ Override
    // Runs automatically on Frame to render all graphics
    public void paint(Graphics g) {
        super.paint(g);

        // Paint water and roads
        for(int i = 0; i < backgroundGrid.length; i++) {
            for(int j = 0; j < backgroundGrid[i].length; j++) {
                // Draw Water
                if(backgroundGrid[i][j] == 1) {
                    g.setColor(Color.BLUE.darker());
                    g.fillRect(i, j, WATER_SIZE, WATER_SIZE);
                }
                // Draw Roads
                else if(backgroundGrid[i][j] == 2) {
                    g.setColor(Color.GRAY.darker());
                    g.fillOval(i, j, ROAD_SIZE, ROAD_SIZE);
                }
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

        // Draw building #1
        String filePathB1 = "/Users/aaroncorona/eclipse-workspace/GTA/src/assets/images/building1.png";
        ImageIcon b1 = new ImageIcon(new ImageIcon(filePathB1).getImage().getScaledInstance(BUILDING_SIZE, BUILDING_SIZE-30, Image.SCALE_DEFAULT));
        b1.paintIcon(this, g, B1_X_START, B1_Y_START);

        // Draw building #2
        String filePathB2 = "/Users/aaroncorona/eclipse-workspace/GTA/src/assets/images/building2.png";
        ImageIcon b2 = new ImageIcon(new ImageIcon(filePathB2).getImage().getScaledInstance(BUILDING_SIZE-40, BUILDING_SIZE-40, Image.SCALE_DEFAULT));
        b2.paintIcon(this, g, B2_X_START, B2_Y_START);

        // Draw building #3
        String filePathB3 = "/Users/aaroncorona/eclipse-workspace/GTA/src/assets/images/building3.png";
        ImageIcon b3 = new ImageIcon(new ImageIcon(filePathB3).getImage().getScaledInstance(BUILDING_SIZE, BUILDING_SIZE-40, Image.SCALE_DEFAULT));
        b3.paintIcon(this, g, B3_X_START, B3_Y_START);

        // Draw building #4
        String filePathB4 = "/Users/aaroncorona/eclipse-workspace/GTA/src/assets/images/building4.png";
        ImageIcon b4 = new ImageIcon(new ImageIcon(filePathB4).getImage().getScaledInstance(BUILDING_SIZE, BUILDING_SIZE+130, Image.SCALE_DEFAULT));
        b4.paintIcon(this, g, B4_X_START, B4_Y_START);

        // Draw building #5 (b1 copied to a different location)
        b1.paintIcon(this, g, B5_X_START, B5_Y_START);

        // Draw building #6
        String filePathB6 = "/Users/aaroncorona/eclipse-workspace/GTA/src/assets/images/building6.png";
        ImageIcon b6 = new ImageIcon(new ImageIcon(filePathB6).getImage().getScaledInstance(BUILDING_SIZE, BUILDING_SIZE, Image.SCALE_DEFAULT));
        b6.paintIcon(this, g, B6_X_START, B6_Y_START);

        // Draw building #7
        String filePathB7 = "/Users/aaroncorona/eclipse-workspace/GTA/src/assets/images/building7.png";
        ImageIcon b7 = new ImageIcon(new ImageIcon(filePathB7).getImage().getScaledInstance(BUILDING_SIZE, BUILDING_SIZE, Image.SCALE_DEFAULT));
        b7.paintIcon(this, g, B7_X_START, B7_Y_START);

        // Draw building #8
        String filePathB8 = "/Users/aaroncorona/eclipse-workspace/GTA/src/assets/images/building8.png";
        ImageIcon b8 = new ImageIcon(new ImageIcon(filePathB8).getImage().getScaledInstance(BUILDING_SIZE, BUILDING_SIZE - 70, Image.SCALE_DEFAULT));
        b8.paintIcon(this, g, B8_X_START, B8_Y_START);

        // Draw bullet effects (splash or explosion)
        for(int i = 0; i < bulletGrid.length; i++) {
            for(int j = 0; j < bulletGrid[i].length; j++) {
                // Draw splash
                if(bulletGrid[i][j] == 5) {
                    String filePathSplash = "/Users/aaroncorona/eclipse-workspace/GTA/src/assets/images/car_S_N.png";
                    ImageIcon splash = new ImageIcon(new ImageIcon(filePathSplash).getImage().getScaledInstance(PLAYER_UNIT_SIZE, PLAYER_UNIT_SIZE, Image.SCALE_DEFAULT));
                    splash.paintIcon(this, g, i, j);
                    bulletGrid[i][j] = 0; // reset
                    generateNewCopBullet(); // cop waits to shoot again until impact
                    // Draw Explosion
                } else if(bulletGrid[i][j] == 6 || bulletGrid[i][j] == 7){
                    String filePathExplosion = "/Users/aaroncorona/eclipse-workspace/GTA/src/assets/images/car_E_N.png";
                    ImageIcon explosion = new ImageIcon(new ImageIcon(filePathExplosion).getImage().getScaledInstance(PLAYER_UNIT_SIZE, PLAYER_UNIT_SIZE, Image.SCALE_DEFAULT));
                    explosion.paintIcon(this, g, i-25, j);
                    if(bulletGrid[i][j] == 7) {
                        moneyGrid[i][j] = 1; // 7 results in money, 6 does not
                        bulletGrid[i][j] = 0;
                        generateNewCopLocation();
                    } else {
                        bulletGrid[i][j] = 0;
                    }
                    generateNewCopBullet(); // cop waits to shoot again until impact
                }
            }
        }

        // Draw Money
        for(int i = 0; i < moneyGrid.length; i++) {
            for(int j = 0; j < moneyGrid[i].length; j++) {
                if(moneyGrid[i][j] == 1) {
                    String filePathMoney = "/Users/aaroncorona/eclipse-workspace/GTA/src/assets/images/money.png";
                    ImageIcon money = new ImageIcon(new ImageIcon(filePathMoney).getImage().getScaledInstance(PLAYER_UNIT_SIZE, PLAYER_UNIT_SIZE, Image.SCALE_DEFAULT));
                    money.paintIcon(this, g, i, j);
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
        // Player Icons - draw this after the game starts
        else {
            // Draw the car icon based on the movement direction so it faces the correct way
            // Note: Must draw icon on slightly modified player location to align with the image expansion
            char regularOrNitroImage = 'R';
            if(nitro == true) {
                regularOrNitroImage = 'N';
            }
            String filePathCar = "/Users/aaroncorona/eclipse-workspace/GTA/src/assets/images/car_" + direction + "_" + regularOrNitroImage + ".png";
            if(direction == 'e') { // make explosions larger
                ImageIcon player = new ImageIcon(new ImageIcon(filePathCar).getImage().getScaledInstance(PLAYER_UNIT_SIZE*2, PLAYER_UNIT_SIZE*2, Image.SCALE_DEFAULT));
                player.paintIcon(this, g, playerXLocation - 20, playerYLocation - 20);
            } else {
                ImageIcon player = new ImageIcon(new ImageIcon(filePathCar).getImage().getScaledInstance(PLAYER_UNIT_SIZE, PLAYER_UNIT_SIZE, Image.SCALE_DEFAULT));
                player.paintIcon(this, g, playerXLocation - 20, playerYLocation - 15);
            }
            // Draw the cop
            String filePathCop = "/Users/aaroncorona/eclipse-workspace/GTA/src/assets/images/cop.png";
            ImageIcon cop = new ImageIcon(new ImageIcon(filePathCop).getImage().getScaledInstance(PLAYER_UNIT_SIZE, PLAYER_UNIT_SIZE, Image.SCALE_DEFAULT));
            cop.paintIcon(this, g, copXLocation - 15, copYLocation - 20);
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

    // Method to stop the game loop and bring up the pause menu
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

    // Method to run the game again and hide the pause menu
    public static void resumeGame() {
        pause = false;
        running = true;
        pauseMenu.setVisible(false);
    }

    // Method to respawn the player at its default location
    public static void generateNewPlayerLocation() {
        playerXLocation = WATER_SIZE + ROAD_SIZE - UNIT_SIZE; // spawn on the first road
        playerYLocation = WATER_SIZE + ROAD_SIZE - UNIT_SIZE;
        direction = 'R';
        oldDirection = 'R';
    }

    // Method to respawn the cop player on a road tile
    public static void generateNewCopLocation() {
        copXLocation = new Random().nextInt((int) (SCREEN_WIDTH/UNIT_SIZE)) * UNIT_SIZE;
        copYLocation = new Random().nextInt((int) (SCREEN_HEIGHT/UNIT_SIZE)) * UNIT_SIZE;
        // Only spawn on the road
        if(backgroundGrid[copXLocation][copYLocation] != 2) {
            generateNewCopLocation();
        }
        // Generate next cop bullet to restart cycle of shots
        generateNewCopBullet();
    }

    // Method to create another bullet traveling west or north from the cop
    public static void generateNewCopBullet() {
        Random rand = new Random();
        int bulletType = rand.nextInt(2) + 2;
        switch (bulletType) {
            case 2:
                bulletGrid[copXLocation - UNIT_SIZE*2][copYLocation] = 2; // avoid shooting oneself
                break;
            case 3:
                bulletGrid[copXLocation][copYLocation - UNIT_SIZE*2] = 3;
                break;
        }
    }

    // Method to update the player's coordinates
    public static void movePlayer() {
        // Change position of the player using the direction variable
        int spacesToMove;
        if(nitro == true) {
            spacesToMove = UNIT_SIZE*2; // 200% speed while checking for collisions to avoid skips
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
        checkPlayerContact();
    }

    // Method to move the bullets to the next position based on its direction
    public static void moveBullet() {
        // Move bullet right and down (traverse grid ascending)
        for(int i = 0; i < bulletGrid.length; i++) {
            for(int j = 0; j < bulletGrid[i].length; j++) {
                // No bullet present
                if(bulletGrid[i][j] == 0) {
                    // do nothing
                }
                // Water check: Bullet becomes a splash
                else if(backgroundGrid[i][j] == 1) {
                    bulletGrid[i][j] = 5;
                }
                // Building check: Bullet becomes an explosion
                else if(backgroundGrid[i][j] == 3) {
                    bulletGrid[i][j] = 6;
                }
                // Cop check: Bullet becomes an explosion and then money
                else if((Math.abs(i - copXLocation) <= UNIT_SIZE
                          && Math.abs(j - copYLocation) <= UNIT_SIZE)
                        || (i == copXLocation
                             && j == copYLocation-UNIT_SIZE)) {
                    bulletGrid[i][j] = 7;
                }
                // Otherwise, move bullet right or down continually
                else if(bulletGrid[i][j] == 1) {
                    bulletGrid[i][j] = 0;
                    bulletGrid[i+UNIT_SIZE][j] = 1;
                    checkPlayerContact();
                }
                else if(bulletGrid[i][j] == 4) {
                    bulletGrid[i][j] = 0;
                    bulletGrid[i][j+UNIT_SIZE] = 4;
                    checkPlayerContact();
                }
            }
        }
        // Move bullet left and up (traverse grid descending)
        for(int i = bulletGrid.length-1; i > 0; i--) {
            for(int j = bulletGrid[i].length-1; j > 0; j--) {
                // No bullet present
                if(bulletGrid[i][j] == 0) {
                    // do nothing
                }
                // Water check: Bullet becomes a splash
                else if(backgroundGrid[i][j] == 1) {
                    bulletGrid[i][j] = 5;
                }
                // Building check: Bullet becomes an explosion
                else if(backgroundGrid[i][j] == 3) {
                    bulletGrid[i][j] = 6;
                }
                // Cop check: Bullet becomes an explosion and then money
                else if((Math.abs(i - copXLocation) <= UNIT_SIZE
                        && Math.abs(j - copYLocation) <= UNIT_SIZE)
                        || (i == copXLocation
                        && j == copYLocation-UNIT_SIZE)) {
                    bulletGrid[i][j] = 7;
                }
                // Otherwise, move bullet left or up continually
                else if(bulletGrid[i][j] == 2) {
                    bulletGrid[i][j] = 0;
                    bulletGrid[i-UNIT_SIZE][j] = 2;
                    checkPlayerContact();
                }
                else if(bulletGrid[i][j] == 3) {
                    bulletGrid[i][j] = 0;
                    bulletGrid[i][j-UNIT_SIZE] = 3;
                    checkPlayerContact();
                }
            }
        }
    }

    // Check if the player hits any particle that causes a reaction (not road or sidewalk)
    public static void checkPlayerContact() {
        // Cop: Check for player to cop collision, which ends the game
        if(Math.abs(playerXLocation - copXLocation) < UNIT_SIZE
                && Math.abs(playerYLocation - copYLocation) <= UNIT_SIZE) {
            direction = 'E'; // triggers explosion image
            generateNewCopLocation();
            running = false;
        }
        // Water: Check for player to water collision, which ends the game
        else if(playerXLocation < PLAYER_UNIT_SIZE
                || playerXLocation > SCREEN_WIDTH - PLAYER_UNIT_SIZE
                || playerYLocation < PLAYER_UNIT_SIZE
                || playerYLocation > SCREEN_HEIGHT - PLAYER_UNIT_SIZE) {
            direction = 'S'; // triggers splash image
            running = false; // stop the game (which triggers end game message)
            System.out.println("* GAME OVER (You Drowned)");
        }
        // Building: Check for player to building collision, which ends the game
        else if(backgroundGrid[playerXLocation][playerYLocation] == 3) {
            direction = 'E';
            running = false;
            System.out.println("* GAME OVER (You crashed into a Building)");
        }
        // Money: Check for player driving over money, which increases the score between 10-20
        else if(moneyGrid[playerXLocation][playerYLocation] == 1) {
            Random rand = new Random();
            int result = rand.nextInt(20-10) + 10;
            money = money + result;
            moneyGrid[playerXLocation][playerYLocation] = 0;
        }
        // Money: Check again on a nearby tile
        else if(moneyGrid[playerXLocation-UNIT_SIZE][playerYLocation] == 1) {
            Random rand = new Random();
            int result = rand.nextInt(20-10) + 10;
            money = money + result;
            moneyGrid[playerXLocation-UNIT_SIZE][playerYLocation] = 0;
        }
        // Bullet: Check for player getting shot, which ends the game
        else if(bulletGrid[playerXLocation][playerYLocation] >= 1) {
            direction = 'E';
            running = false;
            System.out.println("* GAME OVER (You were shot)");
        }
    }

    // Method to check if the game stopped running and therefore log the score and display the game over menus
    public static void checkGameOver() {
        // When the game ends, log the final score if the game ends with a minimum score achieved
        if(running == false && pause == false) {

            // Log final score in the CSV file if it's past a certain minimum
            if (money >= 20) {
                try {
                    // Create or append file
                    FileWriter fw = new FileWriter("/Users/aaroncorona/eclipse-workspace/GTA/src/assets/gta_high_scores.csv", true); // FileWriter append mode is triggered with true (also creates new file if none exists)
                    PrintWriter write = new PrintWriter(fw);
                    // Print the score to the csv file and the time on the column next to it
                    write.println(); // Skip to new row
                    write.print(money);
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
            highScoreMenu.setLocation(550, 450);
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

    // Method to extract the min & seconds passed for the current game
    public static String getTimePassed() {
        long now = System.currentTimeMillis();
        int elapsedTime = (int) (now - startTime); // Convert timestamp difference to seconds
        int elapsedMins = (int) Math.floor(elapsedTime / 1000 / 60);
        int elapsedSecondsRemainder = (int) Math.floor(elapsedTime / 1000 % 60);
        return elapsedMins + " Mins and " + elapsedSecondsRemainder + " Seconds";
    }

    // Method to read the high score file and return the high score results
    public static String getHighScoreMessage() {
        // Create Array to hold the scores
        // TODO -- Use TreeMap instead for connecting unique scores to a timestamp or name.
        long[][] highScoreArray = new long[5000][2];
        // Read the local CSV file
        try {
            File fileObj = new File("/Users/aaroncorona/eclipse-workspace/GTA/src/assets/gta_high_scores.csv");
            Scanner myScanner = new Scanner(fileObj);
            myScanner.useDelimiter("\\n|,|\\s*\\$"); // Treats commas and whitespace as delimiters to read the CSV
            // Fill the score Array using the CSV
            for(int i = 0; myScanner.hasNext(); i++) {
                for(int a = 0; a <= 1; a++) {
                    highScoreArray[i][a] = ((long)Long.parseLong(myScanner.next().trim()));
                }
            }
            myScanner.close();
        } catch (FileNotFoundException e) {
            System.out.println(e);
        }
        // Sort Array in ascending order to easily get the highest scores
        // Note: this shouldn't be needed with a TreeMap
        Arrays.sort(highScoreArray, Comparator.comparingDouble(a -> a[0]));
        // Get the top 3 scores
        int score1 = (int) highScoreArray[highScoreArray.length - 1][0];
        Date date1 = new Date(new Timestamp(highScoreArray[highScoreArray.length - 1][1]).getTime());
        String date1Short = date1.toString().substring(4, 10);
        int score2 = (int) highScoreArray[highScoreArray.length - 2][0];
        Date date2 = new Date(new Timestamp(highScoreArray[highScoreArray.length - 2][1]).getTime());
        String date2Short = date2.toString().substring(4, 10);
        int score3 = (int) highScoreArray[highScoreArray.length - 3][0];
        Date date3 = new Date(new Timestamp(highScoreArray[highScoreArray.length - 3][1]).getTime());
        String date3Short = date3.toString().substring(4, 10);
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
        message += ("<u>1st place</u>: <b>" + score1 + "</b> on " + date1Short + "<br>");
        message += ("<u>2nd place</u>: <b>" + score2 + "</b> on " + date2Short + "<br>");
        message += ("<u>3rd place</u>: <b>" + score3 + "</b> on " + date3Short + "</html>");
        return message;
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

    public static class eAction extends AbstractAction {
        @Override
        public void actionPerformed(ActionEvent e) {
            // Set bullet direction based on car direction
            int bulletType = 0;
            switch(direction) {
                case 'R':
                    bulletType = 1;
                    break;
                case 'L':
                    bulletType = 2;
                    break;
                case 'U':
                    bulletType = 3;
                    break;
                case 'D':
                    bulletType = 4;
                    break;
            }
            // Shoot bullet
            bulletGrid[playerXLocation][playerYLocation] = bulletType;
        }
    }
}


