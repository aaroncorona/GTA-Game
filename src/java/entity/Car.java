package entity;

import java.awt.*;

public abstract class Car implements Entity {
    // Game mechanics vars
    public static main.Panel panel;
    public static main.KeyHandler key;

    // Position tracking vars
    public int xPos, yPos;
    public char direction;
    public int speed;  // pixels to move per frame
    public boolean nitro;

    // Collision tracking vars
    public Rectangle collisionArea;

    // Default method implementation for setting the reset position
    @Override
    public void setDefaultValues() {
        xPos = 150;
        yPos = 150;
        direction = 'R';
        speed = 5;
        nitro = false;
        collisionArea = new Rectangle(xPos, yPos + panel.UNIT_SIZE/4,
                                      panel.UNIT_SIZE, panel.UNIT_SIZE/2);
    }
}
