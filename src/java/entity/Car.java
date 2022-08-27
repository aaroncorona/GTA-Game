package entity;

import java.awt.image.BufferedImage;

public abstract class Car implements Entity {
    public static main.Panel panel;
    public static main.KeyHandler key;

    public int xPos, yPos;
    public char direction;
    public boolean nitro = false;
    BufferedImage image;

    // Default method implementation for setting default position to the center of the screen
    @Override
    public void setDefaultValues() {
        xPos = 150;
        yPos = 150;
        direction = 'R';
        nitro = false;
    }
}

// @TODO find other commonalities for the abstract class?