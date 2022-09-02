package entity.car;

import entity.Entity;
import main.Panel;

import java.awt.*;
import java.awt.image.BufferedImage;

// This super class provides a foundation for creating car classes
public abstract class SuperCar implements Entity {
    // Position tracking vars
    public int xPos, yPos;
    public char direction;
    public int speed;  // pixels to move per frame
    public boolean nitro;

    // Health tracking
    public int health;
    BufferedImage imageCar;
    BufferedImage imageHealth;

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
        health = 3;
        collisionArea = new Rectangle(xPos, yPos + Panel.UNIT_SIZE/4,
                                      Panel.UNIT_SIZE, Panel.UNIT_SIZE/2);
    }
}
