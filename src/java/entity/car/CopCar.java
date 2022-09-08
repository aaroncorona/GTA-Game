package entity.car;

import entity.item.ItemManager;
import tile.CollisionChecker;
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

    LinkedList<PathFinder.Node> currentPathDraw = new LinkedList<>();
    LinkedList<Character> currentPath = new LinkedList<>();

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
        handleCollision();
        handleShooting();
        handleHealth();
        handleDeath();
    }

    // Helper Method to update the Cop's direction based on the wanted level & defined path
    private void updateDir() {
        // Update dir occasionally at random if there's no wanted level (illustrates cruising around and patrolling)
        if(CopCarManager.wantedLevel == 0) {
            direction = getRandomDir(direction);
        }
        // If there is a wanted level, the cop should chase the player. Get the next best Dir to reach the player
        else if(CopCarManager.wantedLevel >= 1) {
            // Only update the path 1/10 tries or when the path runs out. Otherwise, continue on the same path
            int randomNumForUpdate = new Random().nextInt(10);
            if(randomNumForUpdate == 0
                    || currentPath.size() == 0) {
                currentPath = PathFinder.getShortestPathDir(xMapPos, yMapPos, speed, Panel.playerCar.xMapPos, Panel.playerCar.yMapPos);
                currentPathDraw = PathFinder.getShortestPath(xMapPos, yMapPos, speed, Panel.playerCar.xMapPos, Panel.playerCar.yMapPos);// TODO remove after test
            }
            direction = currentPath.get(0);
            currentPath.remove(0);
            currentPathDraw.remove(0); // TODO remove after test
        }
    }

    // Helper method to get a random direction for NPC movement
    private char getRandomDir(char currentDir) {
        // Only update the dir 1/100 tries
        char newDir = currentDir;
        int randomNumForUpdate = new Random().nextInt(100);
        if(randomNumForUpdate == 0) {
            int randomNumForDir = new Random().nextInt(4);
            switch(randomNumForDir) {
                case 0:
                    newDir = 'R';
                    break;
                case 1:
                    newDir = 'L';
                    break;
                case 2:
                    newDir = 'U';
                    break;
                case 3:
                    newDir = 'D';
                    break;
            }
        }
        return newDir;
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
                    xMapPos = xMapPos - Panel.UNIT_SIZE/2; // avoid getting stuck on a tile
//                    direction = getRandomDir(direction); // avoid getting stuck on a tile
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
                    yMapPos = yMapPos - Panel.UNIT_SIZE/2;
                    break;
            }
            System.out.println("Cop on Tile Collision"); // TODO remove after test
        }
        // Second, check for a collision with another cop car
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
                        direction = 'L';
                        yMapPos = yMapPos - Panel.UNIT_SIZE/2;
                        break;
                    case 'D':
                        direction = 'R';
                        yMapPos = yMapPos - Panel.UNIT_SIZE/2;
                        break;
                }
                System.out.println("Cop on Cop Collision"); // TODO remove after test
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
        try {
            for (int i = 0; i < currentPath.size(); i++) {
                PathFinder.Node currentNode = currentPathDraw.get(i);
                int xScreenPos = Camera.translateXMapToScreenPos()[currentNode.xMapPos];
                int yScreenPos = Camera.translateYMapToScreenPos()[currentNode.yMapPos];
                g.fillRect(xScreenPos, yScreenPos, speed, speed);
            }
        } catch (Exception e) {}
    }
}
