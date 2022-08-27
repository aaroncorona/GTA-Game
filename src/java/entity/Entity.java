package entity;

import java.awt.*;
import java.awt.image.BufferedImage;

// Interface to create the players and NPCs in the game
public interface Entity {
    // Method to reset the entity settings (e.g. location, direction)
    public void setDefaultValues();
    // Method to update the entity settings based on key input
    public void update();
    // Method to get the correct image file for the entity based on its status
    public BufferedImage getImage();
    // Method to draw the entity based on the entity settings
    public void draw(Graphics g);
}
