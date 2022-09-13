package entity.car;

import entity.item.ItemManager;
import tile.*;
import main.Panel;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Random;

public class CopCar extends SuperCar {

    // Test
    private LinkedList<PathFinder.Node> currentPath;

    // Protected Constructor to create a single Cop obj. Only the CopManager should use this method
    protected CopCar() {
        setDefaultValues();
    }

    // Cop should spawn in a random location
    @Override
    public void setDefaultValues() {
        xMapPos = new Random().nextInt(1200);
        yMapPos = new Random().nextInt(1400);
        direction = 'R';
        speed = defaultSpeed;
        hitTaken = false;
        health = 1;
        collisionArea = new Rectangle(xMapPos, yMapPos + Panel.UNIT_SIZE/4,
                                      Panel.UNIT_SIZE, Panel.UNIT_SIZE/2);

        // Reset if the Cop spawn on a tile that would cause an instant collision
        if(CollisionChecker.checkTileCollision(this) == true
           || TileManager.getClosestTile(xMapPos, yMapPos).causeCollision) {
            setDefaultValues();
        }
    }

    // Overriding equals() to be able to compare two Cop objects for collision checking
    @Override
    public boolean equals(Object otherObj) {
        // If the object is compared with itself then return true
        if (otherObj == this) {
            return true;
        }
        // Check if different class object
        if (!(otherObj instanceof CopCar)) {
            return false;
        }
        // Check Object member variable equality as an additional check
        if(this.xMapPos == ((CopCar) otherObj).xMapPos
                && this.yMapPos == ((CopCar) otherObj).yMapPos
                && this.health == ((CopCar) otherObj).health) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void update() {
        // Update location
        updateDir();
        updateLocation();
        // Manage events
        handleSpeed();
        handleCollision();
        handleShooting();
        handleHealth();
        handleDeath();
    }

    // Helper Method to update the Cop's direction based on the wanted level & defined path
    private void updateDir() {
        // Update dir occasionally at random if there's no wanted level (illustrates cruising around and patrolling)
        if(CopCarManager.wantedLevel == 0) {
            // Only update the path 1/10 tries. Otherwise, continue the same direction
            int randomNumForUpdate = new Random().nextInt(10);
            if(randomNumForUpdate == 0) {
                direction = PathFinder.getRandomDir();
            }
        }
        // If there is a wanted level, the cop should chase the player. Get the next best Dir to reach the player
        else if(CopCarManager.wantedLevel >= 1) {
            // Only update the path 1/10 tries. Otherwise, continue the same direction
            int randomNumForUpdate = new Random().nextInt(10);
            if(randomNumForUpdate == 0) {
                direction = PathFinder.getNextBestDir(xMapPos, yMapPos, Panel.playerCar.xMapPos, Panel.playerCar.yMapPos);
            }
        }
    }

    // Helper method to update the cop's coordinates and collision area based on the direction and speed
    private void updateLocation() {
        switch(direction) {
            case 'R':
                xMapPos = xMapPos + speed;
                collisionArea = new Rectangle(xMapPos, yMapPos + Panel.UNIT_SIZE/4,
                        Panel.UNIT_SIZE, Panel.UNIT_SIZE/2);
                break;
            case 'L':
                xMapPos = xMapPos - speed;
                collisionArea = new Rectangle(xMapPos, yMapPos + Panel.UNIT_SIZE/4,
                        Panel.UNIT_SIZE, Panel.UNIT_SIZE/2);
                break;
            case 'U':
                yMapPos = yMapPos - speed;
                collisionArea = new Rectangle(xMapPos + Panel.UNIT_SIZE/4, yMapPos,
                        Panel.UNIT_SIZE/2, Panel.UNIT_SIZE);
                break;
            case 'D':
                yMapPos = yMapPos + speed;
                collisionArea = new Rectangle(xMapPos + Panel.UNIT_SIZE/4, yMapPos,
                        Panel.UNIT_SIZE/2, Panel.UNIT_SIZE);
                break;
        }
    }

    // Helper Method to update the speed based on Nitro and Tile traversal cost
    private void handleSpeed() {
        // Check if the car is on a Tile with a traversal cost
        Tile currentTile = TileManager.getClosestTile(xMapPos, yMapPos);
        // Update speed based on the tile traversal cost
        if(currentTile.movementCost == 0) { // reset
            speed = defaultSpeed;
        } else if(currentTile.movementCost >= 1 // initial slowdown
                    && speed == defaultSpeed) {
            speed = speed - currentTile.movementCost;
        }
    }

    // Helper method to respond to collision events that redirect the cop
    private void handleCollision() {
        // First, check if the cop is offscreen (caused by a previous collision reaction)
        if(xMapPos < 0 || yMapPos < 0) { // OOB check
            hitTaken = true;
        }
        // Second, check for a collision with a tile
        if(CollisionChecker.checkTileCollision(this) == true) {
            // Move away from impact
            switch(direction) {
                case 'R':
                    direction = 'L';
                    xMapPos = xMapPos - Panel.UNIT_SIZE/2; // avoid getting stuck on a tile
                    break;
                case 'L':
                    direction = 'R';
                    xMapPos = xMapPos + Panel.UNIT_SIZE/2;
                    break;
                case 'U':
                    direction = 'D';
                    yMapPos = yMapPos + Panel.UNIT_SIZE/2;
                    break;
                case 'D':
                    direction = 'U';
                    yMapPos = yMapPos - Panel.UNIT_SIZE;
                    break;
            }
        }
        // Last, check for a collision with another cop car
        for(int i = 0; i < CopCarManager.cops.size(); i++) {
            if(!CopCarManager.cops.get(i).equals(this)
                    && CollisionChecker.checkEntityCollision(this, CopCarManager.cops.get(i)) == true) {
                // Move away from impact
                switch(direction) {
                    case 'R':
                        direction = 'L';
                        xMapPos = xMapPos - Panel.UNIT_SIZE/2; // avoid getting stuck on a tile
                        break;
                    case 'L':
                        direction = 'R';
                        xMapPos = xMapPos + Panel.UNIT_SIZE/2;
                        break;
                    case 'U':
                        direction = 'D';
                        yMapPos = yMapPos - Panel.UNIT_SIZE/2;
                        break;
                    case 'D':
                        direction = 'U';
                        yMapPos = yMapPos - Panel.UNIT_SIZE/2;
                        break;
                }
            }
        }
    }

    // Helper Method to create a bullet from the Cop randomly
    private void handleShooting() {
        // Get random decision to shoot a bullet in the current frame or not if there is a Wanted level
        if(CopCarManager.wantedLevel >= 1) {
            int randomNum = new Random().nextInt(10);
            if(randomNum == 1) {
                // Spawn the bullet a safe distance from the player to avoid instant death
                switch(direction) {
                    case 'R':
                        ItemManager.createBullet(xMapPos + Panel.UNIT_SIZE, yMapPos, direction);
                        break;
                    case 'L':
                        ItemManager.createBullet(xMapPos - Panel.UNIT_SIZE, yMapPos, direction);
                        break;
                    case 'U':
                        ItemManager.createBullet(xMapPos, yMapPos - Panel.UNIT_SIZE, direction);
                        break;
                    case 'D':
                        ItemManager.createBullet(xMapPos, yMapPos + Panel.UNIT_SIZE, direction);
                        break;
                }
            }
        }
    }

    // Helper method to update player health based on hits taken
    private void handleHealth() {
        // First, check for a collision with a tile
        if(hitTaken) {
            health--;
        }
    }

    // Helper method to respond to Cop death by creating money and then respawning
    private void handleDeath() {
        if(hitTaken && health == 0) {
            ItemManager.createMoney(xMapPos, yMapPos);
            setDefaultValues();
        }
    }

    @Override
    public void loadImages() {
        // Return the explosion image if the car has died
        if(hitTaken) {
            String filePath = "/Users/aaroncorona/eclipse-workspace/GTA/src/assets/images/collisions/explosion.png";
            try {
                imageCar = ImageIO.read(new File(filePath));
            } catch (IOException e) {
                e.printStackTrace();
            }
            hitTaken = false; // reset
        } else {
            // Otherwise, return the correct sprite
            String filePath = "/Users/aaroncorona/eclipse-workspace/GTA/src/assets/images/entities/cop_car/cop_car_";
            // Update the file path for the direction
            filePath += direction;
            filePath += ".png";
            try {
                imageCar = ImageIO.read(new File(filePath));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void draw(Graphics g) {
        loadImages();
        // OOB check
        if(xMapPos < 0 ) {
            xMapPos = 0;
        }
        if(yMapPos < 0 ) {
            yMapPos = 0;
        }
        // Translate the map pos to a screen position and draw
        int xScreenPos = Camera.translateXMapToScreenPos()[xMapPos];
        int yScreenPos = Camera.translateYMapToScreenPos()[yMapPos];
        g.drawImage(imageCar, xScreenPos, yScreenPos, Panel.UNIT_SIZE, Panel.UNIT_SIZE, null);

        // Draw Cop path (for testing)
//        if(CopCarManager.wantedLevel >= 1) {
//            g.setColor(Color.BLUE);
//            try {
//                // Nodes for the start and end point
//                PathFinder.Node startNode = new PathFinder.Node(xMapPos, yMapPos, null);
//                PathFinder.Node destinationNode = new PathFinder.Node(Panel.playerCar.xMapPos, Panel.playerCar.yMapPos, null);
//                if(!startNode.collision && !Panel.pauseState) {
//                    currentPath = PathFinder.getShortestPathNodes(startNode, destinationNode);
//                    System.out.println();
//                    System.out.println(xMapPos + " " + yMapPos);
//                    System.out.println(PathFinder.getNextBestDir(xMapPos, yMapPos, Panel.playerCar.xMapPos, Panel.playerCar.yMapPos));
//                    System.out.println(currentPath);
//                    for (int i = 0; i < currentPath.size(); i++) {
//                        PathFinder.Node currentNode = currentPath.get(i);
//                        int xScreenPosPath = Camera.translateXMapToScreenPos()[currentNode.xMapPos] + 25;
//                        int yScreenPosPath = Camera.translateYMapToScreenPos()[currentNode.yMapPos] + 25;
//                        g.fillOval(xScreenPosPath, yScreenPosPath, 5, 5);
//                    }
//                }
//            } catch (NullPointerException | ArrayIndexOutOfBoundsException e) {}
//        }
    }
}
