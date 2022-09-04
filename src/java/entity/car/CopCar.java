package entity.car;

import entity.item.ItemManager;
import main.CollisionChecker;
import main.Panel;
import tile.TileManager;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.Random;

public class CopCar extends SuperCar {

    // Protected Constructor to create a single Cop obj. Only the CopManager should use this method
    protected CopCar() {
        setDefaultValues();
    }

    // Cop should spawn in a random location
    @Override
    public void setDefaultValues() {
        xMapPos = new Random().nextInt((int) (Panel.SCREEN_WIDTH/Panel.UNIT_SIZE)) * Panel.UNIT_SIZE;
        yMapPos = new Random().nextInt((int) (Panel.SCREEN_HEIGHT/Panel.UNIT_SIZE)) * Panel.UNIT_SIZE;
        direction = 'R';
        speed = Panel.UNIT_SIZE; // default speed is moving 1 full position
        health = 3;

        collisionArea = new Rectangle(xMapPos, yMapPos + Panel.UNIT_SIZE/4,
                                      Panel.UNIT_SIZE, Panel.UNIT_SIZE/2);

        // Reset if the Cop spawn on a tile that would cause an instant collision
        if(CollisionChecker.checkTileCollision(this) == true) {
            setDefaultValues();
        }
    }

    @Override
    public void update() {
        // Manage events
        handleShooting();
        handleCollision();
        handleDeath();
    }

    // Helper Method to create a bullet from the Cop randomly
    private void handleShooting() {
        // Get random decision to shoot a bullet in the current frame or not if there is a Wanted level
        if(CopCarManager.wantedLevel >= 1) {
            int randomNum = new Random().nextInt(10);
            if(randomNum == 1) {
                // Get random direction to shoot
                int randomDir = new Random().nextInt(4);
                char bulletDir = 'R';
                switch(randomDir) {
                    case 0:
                        bulletDir = 'R';
                        break;
                    case 1:
                        bulletDir = 'L';
                        break;
                    case 2:
                        bulletDir = 'U';
                        break;
                    case 3:
                        bulletDir = 'D';
                        break;
                }
                // Spawn the bullet a safe distance from the cop to avoid instant death
                switch(bulletDir) {
                    case 'R':
                        ItemManager.createBullet(xMapPos + Panel.UNIT_SIZE, yMapPos, bulletDir);
                        break;
                    case 'L':
                        ItemManager.createBullet(xMapPos - Panel.UNIT_SIZE, yMapPos, bulletDir);
                        break;
                    case 'U':
                        ItemManager.createBullet(xMapPos, yMapPos - Panel.UNIT_SIZE, bulletDir);
                        break;
                    case 'D':
                        ItemManager.createBullet(xMapPos, yMapPos + Panel.UNIT_SIZE, bulletDir);
                        break;
                }
            }
        }
    }

    // Helper method to respond to collision events that should end the game
    private void handleCollision() {
        // Check for a deadly collision with the player
        if(CollisionChecker.checkEntityCollision(this, Panel.playerCar) == true) {
            health = 0;
        }
    }

    // Helper method to respond to Cop death by creating money and then respawning
    private void handleDeath() {
        if(health == 0) {
            ItemManager.createMoney(xMapPos, yMapPos);
            setDefaultValues();
        }
    }

    @Override
    public void loadImages() {
        // Return the explosion image if the car has died
        if(health == 0) {
            String filePath = "/Users/aaroncorona/eclipse-workspace/GTA/src/assets/images/collisions/explosion.png";
            try {
                imageCar = ImageIO.read(new File(filePath));
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            // Otherwise, return the correct sprite
            String filePath = "/Users/aaroncorona/eclipse-workspace/GTA/src/assets/images/entities/cop_car/cop_car_";
            // Update the file path for the direction
            filePath += direction;
            // Update the file path for the nitro status
            if(nitro == true) {
                filePath += "_nitro";
            }
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
        int xScreenPos = TileManager.tileMapScreenXPos[xMapPos];
        int yScreenPos = TileManager.tileMapScreenYPos[yMapPos];
//        System.out.println("Cop xPos = " + xMapPos);
//        System.out.println("Cop xScreenPos = " + xScreenPos);
        g.drawImage(imageCar, xScreenPos, yScreenPos, Panel.UNIT_SIZE, Panel.UNIT_SIZE, null);
    }
}
