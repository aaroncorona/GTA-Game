package menu;

import javax.swing.*;
import java.awt.*;

// Class for a Pause Menu, which is Popup Menu with buttons and no graphics
public class PauseMenu implements Menu {

    public JPopupMenu pauseMenu;
    public boolean open;
    // Position tracking
    int xPos, yPos;
    int width, height;
    // Sub Components
    JLabel label;
    JButton button1, button2, button3, button4;

    // Constructor
    public PauseMenu() {

        // Instantiate menu and related objects
        pauseMenu = new JPopupMenu();

        // Menu label
        label = new JLabel("PAUSED");
        pauseMenu.add(label);

        // Menu Button
        // TODO add click listeners
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
        xPos = 640;
        yPos = 240;
        width = 400;
        height = 100;
        open = false;

        // Menu UI
        pauseMenu.setLocation(xPos, yPos);
        pauseMenu.setPreferredSize(new Dimension(400, 170));
        pauseMenu.setBackground(Color.CYAN);
        pauseMenu.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));
        pauseMenu.setFocusable(false); // Prevent the menu from taking focus from the panel

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
    public void loadImage() {
        // No images
    }

    @Override
    public void draw(Graphics g) {
        // No graphics to draw
        if(open) {
            pauseMenu.setVisible(true);
        } else {
            pauseMenu.setVisible(false);
        }
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
