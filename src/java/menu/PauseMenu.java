package menu;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import static java.awt.Component.CENTER_ALIGNMENT;

public class PauseMenu implements Menu {

    public final JPopupMenu pauseMenu;
    int xPos, yPos;
    int width, height;
    JLabel label;
    JButton button1, button2, button3, button4;
    BufferedImage image;

    // Constructor
    public PauseMenu() {

        // Instantiate menu and related objects
        pauseMenu = new JPopupMenu();

        // Menu label
        label = new JLabel("PAUSED");
        pauseMenu.add(label);

        // Menu Items
        button1 = new JButton("Resume");
        pauseMenu.add(button1);
        button2 = new JButton("Controls");
        pauseMenu.add(button2);
        button3 = new JButton("Restart");
        pauseMenu.add(button3);
        button4 = new JButton("Quit");
        pauseMenu.add(button4);

        setDefaultValues();
    }

    @Override
    public void setDefaultValues() {
        // Placement vars
        xPos = 650;
        yPos = 300;
        width = 400;
        height = 100;

        // Menu UI
        pauseMenu.setLocation(xPos, yPos);
        pauseMenu.setPreferredSize(new Dimension(400, 170));
        pauseMenu.setBackground(Color.CYAN);
        pauseMenu.setBorder(BorderFactory.createLineBorder(Color.GRAY, 2));
        pauseMenu.setFocusable(false); // Prevent the menu from taking focus from the panel
        closeMenu();

        // Label UI
        label.setFont(new Font("Verdana", Font.PLAIN, 30));
        label.setAlignmentX(Panel.CENTER_ALIGNMENT);
        label.setAlignmentY(Panel.CENTER_ALIGNMENT);

        // Button UI
        button1.setAlignmentX(Panel.CENTER_ALIGNMENT);
        button2.setAlignmentX(Panel.CENTER_ALIGNMENT);
        button3.setAlignmentX(Panel.CENTER_ALIGNMENT);
        button4.setAlignmentX(Panel.CENTER_ALIGNMENT);
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

    }
}

//        pauseMenu.add(new JMenuItem(new AbstractAction("Option 2") {
//            public void actionPerformed(ActionEvent e) {
//            }
//        }));

// Add JButton
//        final JButton button = new JButton("Options");
//        button.addMouseListener(new MouseAdapter() {
//            public void mousePressed(MouseEvent e) {
//                pauseMenu.show(e.getComponent(), e.getX(), e.getY());
//            }
//        });

// Icon
//        item1.setIcon(new ImageIcon("/Users/aaroncorona/eclipse-workspace/GTA/src/assets/images/collisions/explosion.png"));