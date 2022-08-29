package entity.car;

import entity.item.ItemManager;
import main.CollisionChecker;
import main.KeyHandler;
import main.Panel;
import tile.Tile;
import tile.TileManager;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.Random;

public class CopCar extends SuperCar {

    // Constructor to create Cop NPC
    public CopCar() {
        setDefaultValues();
    }

    // Cop should spawn in a random location
    @Override
    public void setDefaultValues() {
        xPos = new Random().nextInt((int) (Panel.SCREEN_WIDTH/Panel.UNIT_SIZE)) * Panel.UNIT_SIZE;
        yPos = new Random().nextInt((int) (Panel.SCREEN_HEIGHT/Panel.UNIT_SIZE)) * Panel.UNIT_SIZE;
        direction = 'R';
        speed = Panel.UNIT_SIZE; // default speed is moving 1 full position
        dead = false;

        collisionArea = new Rectangle(xPos, yPos + Panel.UNIT_SIZE/4,
                                      Panel.UNIT_SIZE, Panel.UNIT_SIZE/2);

        // Reset if the Cop spawn on a tile that would cause an instant collision
        if(TileManager.tiles[TileManager.tileMap[yPos / Panel.UNIT_SIZE][xPos / Panel.UNIT_SIZE]].collision == true) {
            setDefaultValues();
        }
    }

    @Override
    public void update() {
        // Manage events
        handleShooting();
        handleDeadlyCollision();
        handleDeath();
    }

    // Helper Method to create a bullet from the Cop randomly
    private void handleShooting() {
        // Get random decision to shoot a bullet in the current frame or not
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
                    ItemManager.createBullet(xPos + Panel.UNIT_SIZE, yPos, bulletDir);
                    break;
                case 'L':
                    ItemManager.createBullet(xPos - Panel.UNIT_SIZE, yPos, bulletDir);
                    break;
                case 'U':
                    ItemManager.createBullet(xPos, yPos - Panel.UNIT_SIZE, bulletDir);
                    break;
                case 'D':
                    ItemManager.createBullet(xPos, yPos + Panel.UNIT_SIZE, bulletDir);
                    break;
            }
        }
    }

    // Helper method to respond to collision events that should end the game
    private void handleDeadlyCollision() {
        // Check for a deadly collision with the player
        if(CollisionChecker.checkEntityCollision(this, Panel.playerCar) == true) {
            dead = true;
        }
    }

    // Helper method to respond to Cop death by creating money and then respawning
    private void handleDeath() {
        if(dead) {
            ItemManager.createMoney(xPos, yPos);
            setDefaultValues();
        }
    }

    @Override
    public void loadImage() {
        // Return the explosion image if the car has died
        if(dead == true) {
            String filePath = "/Users/aaroncorona/eclipse-workspace/GTA/src/assets/images/collisions/explosion.png";
            try {
                image = ImageIO.read(new File(filePath));
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
                image = ImageIO.read(new File(filePath));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void draw(Graphics g) {
        loadImage();
        g.drawImage(image, xPos, yPos, Panel.UNIT_SIZE, Panel.UNIT_SIZE, null);

        // ad hoc check of the collision area
//        g.setColor(Color.BLACK);
//        g.drawRect(collisionArea.x, collisionArea.y, collisionArea.width, collisionArea.height);
//        g.drawRect(xPos, yPos, 5, 5);
    }
}
