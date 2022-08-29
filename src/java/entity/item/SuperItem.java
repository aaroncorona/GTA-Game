package entity.item;

import entity.Entity;
import java.awt.*;
import java.awt.image.BufferedImage;

// This super class provides a foundation for creating item classes
public abstract class SuperItem implements Entity {
    // Position tracking vars
    public int xPos, yPos;
    public char direction;
    public int speed;
    public boolean dead;
    BufferedImage image;

    // Collision tracking
    public Rectangle collisionArea;
}
