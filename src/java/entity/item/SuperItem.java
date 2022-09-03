package entity.item;

import entity.Entity;
import java.awt.*;
import java.awt.image.BufferedImage;

// This super class provides a foundation for creating item classes
public abstract class SuperItem implements Entity {
    // Item settings
    public static int type;
    public boolean dead;
    BufferedImage imageItem;

    // Position tracking vars
    public int xMapPos, yMapPos;
    public char direction;
    public int speed;

    // Collision tracking
    public Rectangle collisionArea;
}
