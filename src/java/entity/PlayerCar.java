package entity;

import java.awt.*;

public class PlayerCar extends Car {

    public PlayerCar(main.Panel panel, main.KeyHandler key) {
        this.panel = panel;
        this.key = key;

        setDefaultValues();
        loadImages();
    }

    @Override
    public void loadImages() {

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
    public void draw(Graphics g) {

    }
}
