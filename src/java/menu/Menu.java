package menu;

import java.awt.*;

// Interface to create menu classes
public interface Menu {
    // Method to establish the basic menu settings
    void setDefaultValues();
    // Method to get the correct image file for the menu
    void loadImages();
    // Method to draw graphics for the menu
    void draw(Graphics g);
}
