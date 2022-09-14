package menu;

import main.Panel;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

// Class for a Pause Menu, which is Popup Menu with buttons and no graphics
public class PauseMenu implements Menu {

    private JPopupMenu pauseMenu;
    public boolean open;
    // Position tracking
    private static final int X_SCREEN_POS = 640;
    private static final int Y_SCREEN_POS = 240;
    private static final int WIDTH = 400;
    private static final int HEIGHT = 170;
    // Sub Components
    private JLabel label;
    private JButton button1, button2, button3, button4;

    // Constructor
    public PauseMenu() {

        // Instantiate menu and related objects
        pauseMenu = new JPopupMenu();

        // Menu label
        label = new JLabel("PAUSED");
        pauseMenu.add(label);

        // Menu Buttons and their corresponding click events
        button1 = new JButton("Resume");
        button1.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                Panel.resumeGame();
            }
        });
        pauseMenu.add(button1);
        button2 = new JButton("Controls");
        button2.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                if(Panel.controlMenu.open == false) {
                    Panel.controlMenu.open = true;
                } else {
                    Panel.controlMenu.open = false;
                }
            }
        });
        pauseMenu.add(button2);
        button3 = new JButton("Restart");
        button3.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                Panel.resetGame();
            }
        });
        pauseMenu.add(button3);
        button4 = new JButton("Quit");
        button4.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                System.exit(0);
            }
        });
        pauseMenu.add(button4);

        setDefaultValues();
    }

    @Override
    public void setDefaultValues() {
        open = false;

        // Menu UI
        pauseMenu.setLocation(X_SCREEN_POS, Y_SCREEN_POS);
        pauseMenu.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        pauseMenu.setBackground(Color.CYAN);
        pauseMenu.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));
        pauseMenu.setFocusable(false); // Prevent the menu from taking focus from the panel
        pauseMenu.setVisible(false);

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
    public void loadImages() {
        // Not currently used
    }

    @Override
    public void draw(Graphics g) {
        // No graphics to draw, just reveal the menu based on the game state
        if(open) {
            pauseMenu.setVisible(true);
        } else {
            pauseMenu.setVisible(false);
        }
    }
}

