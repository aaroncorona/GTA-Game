package entity.item;

import entity.Entity;
import main.Panel;

import java.awt.*;

public abstract class SuperItem implements Entity {

    // Position tracking vars
    public int xPos, yPos;
    public char direction;
    public int speed;  // pixels to move per frame

    // Collision tracking
    public Rectangle collisionArea;

    // Default method implementation for setting the reset position
    @Override
    public void setDefaultValues() {
        xPos = 400;
        yPos = 400;
        direction = 'R';
        speed = 5;
        collisionArea = new Rectangle(xPos, yPos + Panel.UNIT_SIZE/4,
                                      Panel.UNIT_SIZE, Panel.UNIT_SIZE/2);
    }
}
