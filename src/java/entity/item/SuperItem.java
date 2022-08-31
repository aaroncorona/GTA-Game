package entity.item;

import entity.Entity;
import java.awt.*;
import java.awt.image.BufferedImage;

// This super class provides a foundation for creating item classes
public abstract class SuperItem implements Entity {
    // Item settings
    public static String name;
    BufferedImage image;
    public boolean dead;

    // Position tracking vars
    public int xPos, yPos;
    public char direction;
    public int speed;

    // Collision tracking
    public Rectangle collisionArea;
}
