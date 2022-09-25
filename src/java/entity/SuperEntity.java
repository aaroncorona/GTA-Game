package entity.car;

import entity.Entity;
import main.Panel;

import java.awt.*;
import java.awt.image.BufferedImage;

// This super class provides a foundation for creating car classes
public abstract class SuperEntity implements Entity {
    // Position tracking vars
    public int xMapPos, yMapPos;
    public char direction;
    public int speed;
    public boolean dead;

    // Health tracking
    public boolean hitTaken;
    public int health;

    // Collision tracking
    public Rectangle collisionArea;
}
