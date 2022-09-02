package menu;

import java.awt.*;

// Interface to create menu classes
public interface Menu {
    // Method to establish the basic menu settings
    public void setDefaultValues();
    // Method to get the correct image file for the menu
    public void loadImages();
    // Method to draw graphics for the menu
    public void draw(Graphics g);
}