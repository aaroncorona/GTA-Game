package menu;

import main.Panel;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

// Class for a Pause Menu, which is Popup Menu with buttons and no graphics
public class PauseMenu implements Menu {

    // Position tracking constants
    protected static final int X_SCREEN_POS = 620; // manually center
    protected static final int Y_SCREEN_POS = 240;
    protected static final int WIDTH = 400;
    protected static final int HEIGHT = 220;

    // Menu component
    private JPopupMenu pauseMenu;
    public boolean open;

    // Sub Components
    private JLabel labelPause, labelSpace;
    private JButton buttonResume, buttonControls, buttonSettings, buttonRestart, buttonQuit;

    // Constructor
    public PauseMenu() {

        // Instantiate menu and related objects
        pauseMenu = new JPopupMenu();

        // Menu label
        labelPause = new JLabel("PAUSED");
        pauseMenu.add(labelPause);
        labelSpace = new JLabel(" "); // add space
        pauseMenu.add(labelSpace);

        // Menu Buttons and their corresponding click events
        buttonResume = new JButton("Resume");
        buttonResume.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                Panel.resumeGame();
            }
        });
        pauseMenu.add(buttonResume);
        buttonControls = new JButton("Controls");
        buttonControls.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                if(Panel.controlMenu.open == false) {
                    Panel.controlMenu.open = true;
                } else {
                    Panel.controlMenu.open = false;
                }
            }
        });
        pauseMenu.add(buttonControls);
        buttonSettings = new JButton("Settings");
        buttonSettings.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                if(Panel.settingsMenu.open == false) {
                    Panel.settingsMenu.open = true;
                } else {
                    Panel.settingsMenu.open = false;
                }
            }
        });
        pauseMenu.add(buttonSettings);
        buttonRestart = new JButton("Restart");
        buttonRestart.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                Panel.resetGame();
            }
        });
        pauseMenu.add(buttonRestart);
        buttonQuit = new JButton("Quit");
        buttonQuit.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                System.exit(0);
            }
        });
        pauseMenu.add(buttonQuit);

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
        labelPause.setFont(new Font("Verdana", Font.PLAIN, 30));
        labelPause.setForeground(Color.GRAY);
        labelPause.setAlignmentX(Panel.CENTER_ALIGNMENT);
        labelPause.setAlignmentY(Panel.CENTER_ALIGNMENT);
        labelSpace.setFont(new Font("Verdana", Font.PLAIN, 8));

        // Button UI
        buttonResume.setAlignmentX(Panel.CENTER_ALIGNMENT);
        buttonControls.setAlignmentX(Panel.CENTER_ALIGNMENT);
        buttonSettings.setAlignmentX(Panel.CENTER_ALIGNMENT);
        buttonRestart.setAlignmentX(Panel.CENTER_ALIGNMENT);
        buttonQuit.setAlignmentX(Panel.CENTER_ALIGNMENT);
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

