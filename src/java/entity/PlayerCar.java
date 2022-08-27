package entity;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class PlayerCar extends Car {

    // Constructor to create Player Car
    public PlayerCar(main.Panel panel, main.KeyHandler key) {
        this.panel = panel;
        this.key = key;

        setDefaultValues();
    }

    @Override
    public void update() {
        if(panel.running) {
            updateDir();
            updateLocation();
        }
    }

    // Helper Method to update the Car's direction and nitro values based on key input
    private void updateDir() {
        // Map key presses to determine the player's direction
        if(key.upPress == true) {
            direction = 'U';
            key.upPress = false; // movement processed
        }
        if(key.downPress == true) {
            direction = 'D';
            key.downPress = false;
        }
        if(key.rightPress == true) {
            direction = 'R';
            key.rightPress = false;
        }
        if(key.leftPress == true) {
            direction = 'L';
            key.leftPress = false;
        }

        // R key for nitro
        if(key.rPress == true && nitro == false) {
            nitro = true;
            key.rPress = false;
        }
        if(key.rPress == true && nitro == true) {
            nitro = false;
            key.rPress = false;
        }
    }

    // Helper method to update the player's coordinates based on the direction and nitro
    private void updateLocation() {
        // Change position of the player using the direction variable
        int spacesToMove;
        if(nitro == true) {
            spacesToMove = panel.UNIT_SIZE*2; // 200% speed while checking for collisions to avoid skips
        } else {
            spacesToMove = panel.UNIT_SIZE;
        }

        switch(direction) {
            case 'R':
                xPos = xPos + spacesToMove;
                break;
            case 'L':
                xPos = xPos - spacesToMove;
                break;
            case 'U':
                yPos = yPos - spacesToMove;
                break;
            case 'D':
                yPos = yPos + spacesToMove;
                break;
        }
    }

    @Override
    public BufferedImage getImage() {
        String filePath = "/Users/aaroncorona/eclipse-workspace/GTA/src/assets/images/player_car/player_car_";
        BufferedImage image = null;
        // Determine the image direction and nitro status
        filePath += direction;
        // Determine if it should be a nitro image
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
//        g.drawOval(xPos, yPos, 1, 1);
    }
}
