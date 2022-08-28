package entity.car;

import main.ContactChecker;
import main.KeyHandler;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class PlayerCar extends SuperCar {

    // Constructor to create Player Car
    public PlayerCar(main.Panel panel) {
        this.panel = panel;

        setDefaultValues();
    }

    @Override
    public void update() {
        if(panel.running) {
            // Update location
            updateDir();
            updateLocation();
            // Manage events
            handleDeadlyCollision();
            handleMoneyCollection();
        }
    }

    // Helper Method to update the Car's direction and nitro values based on key input
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

    // Helper method to update the player's coordinates and collision area based on the direction and speed
    private void updateLocation() {
        switch(direction) {
            case 'R':
                xPos = xPos + speed;
                collisionArea = new Rectangle(xPos, yPos + panel.UNIT_SIZE/4,
                                              panel.UNIT_SIZE, panel.UNIT_SIZE/2);
                break;
            case 'L':
                xPos = xPos - speed;
                collisionArea = new Rectangle(xPos, yPos + panel.UNIT_SIZE/4,
                                              panel.UNIT_SIZE, panel.UNIT_SIZE/2);
                break;
            case 'U':
                yPos = yPos - speed;
                collisionArea = new Rectangle(xPos+ panel.UNIT_SIZE/4, yPos,
                                              panel.UNIT_SIZE/2, panel.UNIT_SIZE);
                break;
            case 'D':
                yPos = yPos + speed;
                collisionArea = new Rectangle(xPos+ panel.UNIT_SIZE/4, yPos,
                                        panel.UNIT_SIZE/2, panel.UNIT_SIZE);
                break;
        }
    }

    // Helper method to respond to collision events that should end the game
    private void handleDeadlyCollision() {
        // First, check for a deadly collision with a tile
        if(ContactChecker.checkTileCollision(this) == true) {
            dead = true;
            panel.running = false;
            System.out.println("Deadly Collision - Tile");
        }
        // Second, check for a deadly collision with another car
        if(ContactChecker.checkCarCollision(this, panel.copCar) == true) {
            dead = true;
            panel.running = false;
            System.out.println("Deadly Collision - Cop Car");
        }
        // @TODO update after the bullet class is built
        // Check for a deadly collision with a bullet
//        if(ContactChecker.checkBulletCollision(this, panel.bullet) == true) {
//            death = true;
//            panel.running = false;
//            System.out.println("deadly collision - bullet");
//        }
    }

    // @TODO update after the money class is built
    // Helper method to check if money is collected
    private void handleMoneyCollection() {

    }

    @Override
    public BufferedImage getImage() {
        BufferedImage image = null;

        // Return the explosion image if the car has died
        if(dead == true) {
            String filePath = "/Users/aaroncorona/eclipse-workspace/GTA/src/assets/images/collisions/explosion.png";
            try {
                image = ImageIO.read(new File(filePath));
            } catch (IOException e) {
                e.printStackTrace();
            }
            return image;
        }

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
            image = ImageIO.read(new File(filePath));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return image;
    }

    @Override
    public void draw(Graphics g) {
        g.drawImage(getImage(), xPos, yPos, panel.UNIT_SIZE, panel.UNIT_SIZE, null);

        // ad hoc check of the collision area
//        g.setColor(Color.BLACK);
//        g.drawRect(collisionArea.x, collisionArea.y, collisionArea.width, collisionArea.height);
//        g.drawRect(xPos, yPos, 5, 5);
    }
}
