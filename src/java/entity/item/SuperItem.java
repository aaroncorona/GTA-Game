package entity.item;

import entity.Entity;
import java.awt.*;

public abstract class SuperItem implements Entity {
    // Position tracking vars
    public int xPos, yPos;
    public char direction;
    public int speed;

    // Collision tracking
    public Rectangle collisionArea;
}
