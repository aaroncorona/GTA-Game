package entity.car;

import entity.item.ItemManager;
import main.CollisionChecker;
import main.Panel;
import tile.Camera;
import tile.PathFinder;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Random;

public class CopCar extends SuperCar {

    LinkedList<PathFinder.Node> currentPath;

    // Protected Constructor to create a single Cop obj. Only the CopManager should use this method
    protected CopCar() {
        setDefaultValues();
    }

    // Cop should spawn in a random location
    @Override
    public void setDefaultValues() {
        xMapPos = new Random().nextInt(1000);
        yMapPos = new Random().nextInt(1000);
        direction = 'R';
        speed = 5;
        hitTaken = false;
        health = 1;
        collisionArea = new Rectangle(xMapPos, yMapPos + Panel.UNIT_SIZE/4,
                                      Panel.UNIT_SIZE, Panel.UNIT_SIZE/2);

        // Reset if the Cop spawn on a tile that would cause an instant collision
        if(CollisionChecker.checkTileCollision(this) == true) {
            setDefaultValues();
        } else {
            currentPath = PathFinder.getShortestPath(xMapPos, yMapPos, speed, Panel.playerCar.xMapPos, Panel.playerCar.yMapPos);
        }
    }

    @Override
    public void update() {
        // Update location
        updateDir();
        updateLocation();
        // Manage events
        handleShooting();
        handleCollision();
        handleHealth();
        handleDeath();
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

    // Helper Method to update the Cop's direction based on the defined path
    private void updateDir() {
        currentPath = PathFinder.getShortestPath(xMapPos, yMapPos, speed, Panel.playerCar.xMapPos, Panel.playerCar.yMapPos);
        // Update dir occasionally at random if there's no wanted level (illustrates cruising around and patrolling)
        int randomNum = new Random().nextInt(100);
        if(CopCarManager.wantedLevel == 0 && randomNum == 1) {
            // Get random direction to travel
            int randomDir = new Random().nextInt(4);
            switch(randomDir) {
                case 0:
                    direction = 'R';
                    break;
                case 1:
                    direction = 'L';
                    break;
                case 2:
                    direction = 'U';
                    break;
                case 3:
                    direction = 'D';
                    break;
            }
        }
        // If there is a wanted level, the cop should chase the player
        else if(CopCarManager.wantedLevel >= 1) {
            // Get the next best direction
            direction = PathFinder.getShortestPathDir(xMapPos, yMapPos, speed, Panel.playerCar.xMapPos, Panel.playerCar.yMapPos).get(0);
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

    // Helper method to respond to collision events that redirect the cop
    private void handleCollision() {
        // First, check for a collision with a tile
        if(CollisionChecker.checkTileCollision(this) == true) {
            // Move away from impact
            switch(direction) {
                case 'R':
                    direction = 'L';
                    yMapPos = yMapPos - speed; // avoid getting stuck on a tile
                    break;
                case 'L':
                    direction = 'R';
                    yMapPos = yMapPos + speed; // avoid getting stuck on a tile
                    break;
                case 'U':
                    direction = 'L';
                    xMapPos = xMapPos - speed;
                    break;
                case 'D':
                    direction = 'U';
                    xMapPos = xMapPos + speed;
                    break;
            }
        }
        // Second, check for a collision with another cop car
        for(int i = 0; i < CopCarManager.cops.size(); i++) {
            if(!CopCarManager.cops.get(i).equals(this)
                    && CollisionChecker.checkEntityCollision(this, CopCarManager.cops.get(i)) == true) {
                // Move away from impact
                switch(direction) {
                    case 'R':
                        direction = 'L';
                        break;
                    case 'L':
                        direction = 'R';
                        break;
                    case 'U':
                        direction = 'D';
                        break;
                    case 'D':
                        direction = 'U';
                        break;
                }
                updateLocation();
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
        // Connect the map position to a screen position
        try{
            int xScreenPos = Camera.translateXMapToScreenPos()[xMapPos];
            int yScreenPos = Camera.translateYMapToScreenPos()[yMapPos];
            g.drawImage(imageCar, xScreenPos, yScreenPos, Panel.UNIT_SIZE, Panel.UNIT_SIZE, null);
        } catch (ArrayIndexOutOfBoundsException e) {
            if(CopCarManager.cops.size() == 0) {
                CopCarManager.createCop();
            }
        }
        // draw path (test)
        for(int i = 0; i < currentPath.size(); i++) {
            PathFinder.Node currentNode = currentPath.get(i);
            int xScreenPos = Camera.translateXMapToScreenPos()[currentNode.xMapPos];
            int yScreenPos = Camera.translateYMapToScreenPos()[currentNode.yMapPos];
            g.fillRect(xScreenPos, yScreenPos, speed, speed);
        }
    }
}
