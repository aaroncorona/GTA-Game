package menu;

import javax.swing.*;
import java.awt.*;

public class PauseMenu implements Menu {

    public JPopupMenu pauseMenu;
    int xPos, yPos;
    int width, height;

    // Constructor
    public PauseMenu() {
        pauseMenu = new JPopupMenu();

        setDefaultValues();
    }

    @Override
    public void setDefaultValues() {
        // Menu placement
        xPos = 500;
        yPos = 500;
        width = 400;
        height = 100;
        pauseMenu.setLocation(xPos, yPos);
        pauseMenu.setPreferredSize(new Dimension(500, 30));
        closeMenu();
    }

    @Override
    public void openMenu() {
        pauseMenu.setVisible(true);
    }

    @Override
    public void closeMenu() {
        pauseMenu.setVisible(false);
    }

    @Override
    public void loadImages() {

    }

    @Override
    public void draw(Graphics g) {
        // Menu UI
        pauseMenu.setBackground(Color.WHITE);
        pauseMenu.setBorder(BorderFactory.createLineBorder(Color.PINK, 3));
        pauseMenu.setFocusable(false); // Prevent the menu from taking focus from the panel

        // Create Menu Label
        JLabel pauseMenuLabel = new JLabel("Press SPACE to Resume");
        pauseMenuLabel.setFont(new Font("Verdana", Font.PLAIN, 18));
        pauseMenuLabel.setForeground(Color.BLACK);
        pauseMenuLabel.setAlignmentX(Panel.CENTER_ALIGNMENT);
        pauseMenuLabel.setAlignmentY(Panel.CENTER_ALIGNMENT);
        pauseMenu.add(pauseMenuLabel);
    }
}
