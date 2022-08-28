package entity.item;

import entity.Entity;
import java.awt.*;

public abstract class SuperItem implements Entity {
    // Panel to track game state
    public static main.Panel panel;

    // Position tracking vars
    public int xPos, yPos;
    public char direction;
    public int speed; // pixels to move per frame

    // Collision tracking
    public Rectangle collisionArea;
}
