package menu;

import java.awt.*;

// Interface to create menu classes
public interface Menu {
    // Method to open the menu
    public void openMenu();
    // Method to hide the menu
    public void closeMenu();
    // Method to get the correct image file for the menu
    public void loadImage();
    // Method to draw the menu
    public void draw(Graphics g);
}