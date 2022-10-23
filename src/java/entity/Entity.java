package entity;

import java.awt.*;

// Interface to create the players and NPCs in the game
public interface Entity {
    // Method to reset the entity settings (e.g. location, direction)
    void setDefaultValues();
    // Method to update the entity settings
    void update();
    // Method to get the correct image file for the entity based on its status
    void loadImages();
    // Method to draw the entity based on the entity settings
    void draw(Graphics g);
}
