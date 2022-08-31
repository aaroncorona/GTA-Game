package menu;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class PauseMenu implements Menu {

    public final JPopupMenu pauseMenu;
    int xPos, yPos;
    int width, height;
    JMenuItem item1;

    // Constructor
    public PauseMenu() {

        // Instantiate menu objects
        pauseMenu = new JPopupMenu();
        JMenuItem item1 = new JMenuItem("Option 1");
        pauseMenu.add(item1);

        setDefaultValues();
    }

    @Override
    public void setDefaultValues() {
        // Placement vars
        xPos = 500;
        yPos = 500;
        width = 400;
        height = 100;

        // Default Menu UI settings
        pauseMenu.setLocation(xPos, yPos);
        pauseMenu.setPreferredSize(new Dimension(500, 200));
        pauseMenu.setBackground(Color.WHITE);
        pauseMenu.setBorder(BorderFactory.createLineBorder(Color.PINK, 3));
        pauseMenu.setFocusable(false); // Prevent the menu from taking focus from the panel
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

    }
}

// Add JMenuItems


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