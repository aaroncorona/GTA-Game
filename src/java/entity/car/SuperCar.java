package entity.car;

import entity.Entity;
import main.Panel;

import java.awt.*;
import java.awt.image.BufferedImage;

// This super class provides a foundation for creating car classes
public abstract class SuperCar implements Entity {
    // Position tracking vars
    public int xMapPos, yMapPos;
    public char direction;
    public int speed;  // pixels to move per frame
    public boolean nitro;

    // Health tracking
    public boolean hitTaken;
    public int health;
    BufferedImage imageCar;
    BufferedImage imageHealth;

    // Collision tracking
    public Rectangle collisionArea;

    // Default method implementation for setting the reset position
    @Override
    public void setDefaultValues() {
        xMapPos = 900;
        yMapPos = 950;
        direction = 'R';
        speed = 5;
        nitro = false;
        hitTaken = false;
        health = 3;
        collisionArea = new Rectangle(xMapPos, yMapPos + Panel.UNIT_SIZE/4,
                                      Panel.UNIT_SIZE, Panel.UNIT_SIZE/2);
    }
}
