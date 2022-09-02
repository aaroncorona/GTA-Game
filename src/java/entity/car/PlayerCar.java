package entity.car;

import main.CollisionChecker;
import main.KeyHandler;
import main.Panel;
import entity.item.ItemManager;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class PlayerCar extends SuperCar {

    // Constructor to create Player Car
    public PlayerCar() {
        setDefaultValues();
    }

    @Override
    public void update() {
        // Update location
        updateDir();
        updateLocation();
        // Manage events
        handleNitro();
        handleShooting();
        handleCollision();
    }

    // Helper Method to update the Car's direction based on key input
    private void updateDir() {
        // Key mappings for the player's direction
        if(KeyHandler.upPress == true) {
            direction = 'U';
            KeyHandler.upPress = false; // movement processed
        }
        if(KeyHandler.downPress == true) {
            direction = 'D';
            KeyHandler.downPress = false;
        }
        if(KeyHandler.rightPress == true) {
            direction = 'R';
            KeyHandler.rightPress = false;
        }
        if(KeyHandler.leftPress == true) {
            direction = 'L';
            KeyHandler.leftPress = false;
        }
    }

    // Helper method to update the player's coordinates and collision area based on the direction and speed
    private void updateLocation() {
        switch(direction) {
            case 'R':
                xPos = xPos + speed;
                collisionArea = new Rectangle(xPos, yPos + Panel.UNIT_SIZE/4,
                                              Panel.UNIT_SIZE, Panel.UNIT_SIZE/2);
                break;
            case 'L':
                xPos = xPos - speed;
                collisionArea = new Rectangle(xPos, yPos + Panel.UNIT_SIZE/4,
                                              Panel.UNIT_SIZE, Panel.UNIT_SIZE/2);
                break;
            case 'U':
                yPos = yPos - speed;
                collisionArea = new Rectangle(xPos+ Panel.UNIT_SIZE/4, yPos,
                                        Panel.UNIT_SIZE/2, Panel.UNIT_SIZE);
                break;
            case 'D':
                yPos = yPos + speed;
                collisionArea = new Rectangle(xPos+ Panel.UNIT_SIZE/4, yPos,
                                        Panel.UNIT_SIZE/2, Panel.UNIT_SIZE);
                break;
        }
    }

    // Helper Method to update the Car's nitro values based on key input
    private void handleNitro() {
        // R key to toggle nitro (triple speed)
        if(KeyHandler.rPress == true && nitro == false) {
            nitro = true;
            speed = speed*3;
            KeyHandler.rPress = false;
        }
        if(KeyHandler.rPress == true && nitro == true) {
            nitro = false;
            speed = speed/3;
            KeyHandler.rPress = false;
        }
    }

    // Helper Method to create a bullet based on key input
    private void handleShooting() {
        // E key to shoot bullet
        if(KeyHandler.ePress == true) {
            // Spawn the bullet a safe distance from the player to avoid instant death
            switch(direction) {
                case 'R':
                    ItemManager.createBullet(xPos + Panel.UNIT_SIZE, yPos, direction);
                    break;
                case 'L':
                    ItemManager.createBullet(xPos - Panel.UNIT_SIZE, yPos, direction);
                    break;
                case 'U':
                    ItemManager.createBullet(xPos, yPos - Panel.UNIT_SIZE, direction);
                    break;
                case 'D':
                    ItemManager.createBullet(xPos, yPos + Panel.UNIT_SIZE, direction);
                    break;
            }
            KeyHandler.ePress = false;
        }
    }

    // Helper method to respond to collision events that should end the game
    private void handleCollision() {
        // First, check for a deadly collision with a tile
        if(CollisionChecker.checkTileCollision(this) == true) {
            health = 0;
            System.out.println("Deadly Collision - Tile");
        }
        // Second, check for a deadly collision with a cop car
        for(int i = 0; i < CopCarManager.cops.size(); i++) {
            if(CollisionChecker.checkEntityCollision(this, CopCarManager.cops.get(i)) == true) {
                health = 0;
                CopCarManager.cops.get(i).health = 0;
                System.out.println("Deadly Collision - Cop Car");
            }
        }
    }

    @Override
    public void loadImages() {
        // Get the explosion image if the car has died
        if(health == 0) {
            String filePath = "/Users/aaroncorona/eclipse-workspace/GTA/src/assets/images/collisions/explosion.png";
            try {
                imageCar = ImageIO.read(new File(filePath));
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            // Otherwise, return the correct sprite
            String filePath = "/Users/aaroncorona/eclipse-workspace/GTA/src/assets/images/entities/player_car/player_car_";
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
        // Get the correct health bar image
        String filePath = "/Users/aaroncorona/eclipse-workspace/GTA/src/assets/images/entities/player_car/health_";
        filePath += health;
        filePath += ".png";
        try {
            imageHealth = ImageIO.read(new File(filePath));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void draw(Graphics g) {
        if(Panel.titleState == false) {
            loadImages();
            g.drawImage(imageCar, xPos, yPos, Panel.UNIT_SIZE, Panel.UNIT_SIZE, null);
            g.drawImage(imageHealth, 350, 3, Panel.UNIT_SIZE*4, Panel.UNIT_SIZE-5, null);
        }
    }
}
