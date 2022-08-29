package entity.car;

import entity.Entity;

import java.awt.*;
import java.awt.image.BufferedImage;

// This super class provides a foundation for creating car classes
public abstract class SuperCar implements Entity {
    // Panel to track game state
    public static main.Panel panel;

    // Position tracking vars
    public int xPos, yPos;
    public char direction;
    public int speed;  // pixels to move per frame
    public boolean nitro;
    public boolean dead;
    BufferedImage image;

    // Collision tracking
    public Rectangle collisionArea;

    // Default method implementation for setting the reset position
    @Override
    public void setDefaultValues() {
        xPos = 150;
        yPos = 150;
        direction = 'R';
        speed = 5;
        nitro = false;
        dead = false;
        collisionArea = new Rectangle(xPos, yPos + panel.UNIT_SIZE/4,
                                      panel.UNIT_SIZE, panel.UNIT_SIZE/2);
    }
}