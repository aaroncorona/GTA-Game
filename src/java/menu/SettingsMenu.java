
package menu;

import main.Config;
import main.Panel;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

// Class for a Pause Menu, which is Popup Menu with buttons and no graphics
public class SettingsMenu implements Menu {

    // Position tracking constants
    private static final int X_SCREEN_POS = PauseMenu.X_SCREEN_POS;
    private static final int Y_SCREEN_POS = PauseMenu.Y_SCREEN_POS;
    private static final int WIDTH = PauseMenu.WIDTH;
    private static final int HEIGHT = 480;

    // Menu component
    private JPopupMenu settingsMenu;
    public boolean open;

    // Sub Components
    private JLabel labelScreen, labelBackgroundMusic, labelSoundEffect, labelDifficulty,
                   labelSpacer1, labelSpacer2, labelSpacer3, labelSpacer4;
    private JButton buttonBack, buttonScreenOption1, buttonScreenOption2,
                    buttonBackgroundMusicOption1, buttonBackgroundMusicOption2,
                    buttonSoundEffectOption1, buttonSoundEffectOption2,
                    difficultyOption1, difficultyOption2, difficultyOption3;

    // Constructor
    public SettingsMenu() {

        // Instantiate menu and related objects
        settingsMenu = new JPopupMenu();

        // Menu Button to go back
        buttonBack = new JButton("           BACK          ");
        buttonBack.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                open = false;
            }
        });
        settingsMenu.add(buttonBack);
        settingsMenu.add(labelSpacer1 = new JLabel(" "));

        // Screen Setting - Create Label and Buttons
        labelScreen = new JLabel("Screen Width:");
        settingsMenu.add(labelScreen); // todo add button with click event
        buttonScreenOption1 = new JButton("Full Screen");
        buttonScreenOption1.addMouseListener(new MouseAdapter() {
            // Update setting if needed and change color to reflect the current setting
            public void mousePressed(MouseEvent e) {
                if(Config.getScreenWidthConfig() == 0.7) {
                    Config.setScreenWidthConfig(1.0);
                    buttonScreenOption1.setForeground(Color.BLACK);
                    buttonScreenOption2.setForeground(Color.GRAY);
                }
            }
        });
        settingsMenu.add(buttonScreenOption1);
        buttonScreenOption2 = new JButton("70% Width");
        buttonScreenOption2.addMouseListener(new MouseAdapter() {
            // Update setting if needed and change color to reflect the current setting
            public void mousePressed(MouseEvent e) {
                if(Config.getScreenWidthConfig() == 1.0) {
                    Config.setScreenWidthConfig(0.7);
                    buttonScreenOption1.setForeground(Color.GRAY);
                    buttonScreenOption2.setForeground(Color.BLACK);
                }
            }
        });
        settingsMenu.add(buttonScreenOption2);
        settingsMenu.add(labelSpacer2 = new JLabel(" "));

        // Background Music Setting - Create Label and Buttons
        labelBackgroundMusic = new JLabel("Background Music:");
        settingsMenu.add(labelBackgroundMusic);
        buttonBackgroundMusicOption1 = new JButton("On");
        buttonBackgroundMusicOption1.addMouseListener(new MouseAdapter() {
            // Update setting if needed and change color to reflect the current setting
            public void mousePressed(MouseEvent e) {
                if(Config.getBackgroundMusicConfig() == false) {
                    Config.setBackgroundMusicConfig(true);
                    buttonBackgroundMusicOption1.setForeground(Color.BLACK);
                    buttonBackgroundMusicOption2.setForeground(Color.GRAY);
                }
            }
        });
        settingsMenu.add(buttonBackgroundMusicOption1);
        buttonBackgroundMusicOption2 = new JButton("Off");
        buttonBackgroundMusicOption2.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                // Update setting if needed and change color to reflect the current setting
                if(Config.getBackgroundMusicConfig() == true) {
                    Config.setBackgroundMusicConfig(false);
                    buttonBackgroundMusicOption1.setForeground(Color.GRAY);
                    buttonBackgroundMusicOption2.setForeground(Color.BLACK);
                }
            }
        });
        settingsMenu.add(buttonBackgroundMusicOption2);
        settingsMenu.add(labelSpacer3 = new JLabel(" "));

        // Sound Effect Setting - Create Label and Button
        labelSoundEffect = new JLabel("Sound Effect:");
        settingsMenu.add(labelSoundEffect);
        buttonSoundEffectOption1 = new JButton("On");
        buttonSoundEffectOption1.addMouseListener(new MouseAdapter() {
            // Update setting if needed and change color to reflect the current setting
            public void mousePressed(MouseEvent e) {
                if(Config.getSoundEffectConfig() == false) {
                    Config.setSoundEffectConfig(true);
                    buttonSoundEffectOption1.setForeground(Color.BLACK);
                    buttonSoundEffectOption2.setForeground(Color.GRAY);
                }
            }
        });
        settingsMenu.add(buttonSoundEffectOption1);
        buttonSoundEffectOption2 = new JButton("Off");
        buttonSoundEffectOption2.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                // Update setting if needed and change color to reflect the current setting
                if(Config.getSoundEffectConfig() == true) {
                    Config.setSoundEffectConfig(false);
                    buttonSoundEffectOption1.setForeground(Color.GRAY);
                    buttonSoundEffectOption2.setForeground(Color.BLACK);
                }
            }
        });
        settingsMenu.add(buttonSoundEffectOption2);
        settingsMenu.add(labelSpacer4 = new JLabel(" "));

        // Difficulty Setting - Label and Button
        labelDifficulty = new JLabel("Difficulty Level:");
        settingsMenu.add(labelDifficulty);
        difficultyOption1 = new JButton("Insane");
        difficultyOption1.addMouseListener(new MouseAdapter() {
            // Update setting if needed and change color to reflect the current setting
            public void mousePressed(MouseEvent e) {
                if(Config.getDifficultyConfig() != 3) {
                    Config.setDifficultyConfig(3);
                    difficultyOption1.setForeground(Color.BLACK);
                    difficultyOption2.setForeground(Color.GRAY);
                    difficultyOption3.setForeground(Color.GRAY);
                }
            }
        });
        settingsMenu.add(difficultyOption1);
        difficultyOption2 = new JButton("Hard");
        difficultyOption2.addMouseListener(new MouseAdapter() {
            // Update setting if needed and change color to reflect the current setting
            public void mousePressed(MouseEvent e) {
                if(Config.getDifficultyConfig() != 2) {
                    Config.setDifficultyConfig(2);
                    difficultyOption1.setForeground(Color.GRAY);
                    difficultyOption2.setForeground(Color.BLACK);
                    difficultyOption3.setForeground(Color.GRAY);
                }
            }
        });
        settingsMenu.add(difficultyOption2);
        difficultyOption3 = new JButton("Normal");
        difficultyOption3.addMouseListener(new MouseAdapter() {
            // Update setting if needed and change color to reflect the current setting
            public void mousePressed(MouseEvent e) {
                if(Config.getDifficultyConfig() != 1) {
                    Config.setDifficultyConfig(1);
                    difficultyOption1.setForeground(Color.GRAY);
                    difficultyOption2.setForeground(Color.GRAY);
                    difficultyOption3.setForeground(Color.BLACK);
                }
            }
        });
        settingsMenu.add(difficultyOption3);

        setDefaultValues();
    }

    @Override
    public void setDefaultValues() {
        open = false;

        // Menu UI
        settingsMenu.setLocation(X_SCREEN_POS, Y_SCREEN_POS);
        settingsMenu.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        settingsMenu.setBackground(Color.black);
        settingsMenu.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));
        settingsMenu.setFocusable(false); // Prevent the menu from taking focus from the panel
        settingsMenu.setVisible(false);

        // Label UI
        Font labelFont = new Font("TimesRoman", Font.PLAIN, 19);
        labelSpacer1.setFont(labelFont);
        labelSpacer2.setFont(labelFont);
        labelSpacer3.setFont(labelFont);
        labelSpacer4.setFont(labelFont);
        labelScreen.setFont(labelFont);
        labelScreen.setForeground(Color.ORANGE);
        labelScreen.setAlignmentX(Panel.CENTER_ALIGNMENT);
        labelScreen.setAlignmentY(Panel.CENTER_ALIGNMENT);
        labelBackgroundMusic.setFont(labelFont);
        labelBackgroundMusic.setForeground(Color.ORANGE);
        labelBackgroundMusic.setAlignmentX(Panel.CENTER_ALIGNMENT);
        labelBackgroundMusic.setAlignmentY(Panel.CENTER_ALIGNMENT);
        labelSoundEffect.setFont(labelFont);
        labelSoundEffect.setForeground(Color.ORANGE);
        labelSoundEffect.setAlignmentX(Panel.CENTER_ALIGNMENT);
        labelSoundEffect.setAlignmentY(Panel.CENTER_ALIGNMENT);
        labelDifficulty.setFont(labelFont);
        labelDifficulty.setForeground(Color.ORANGE);
        labelDifficulty.setAlignmentX(Panel.CENTER_ALIGNMENT);
        labelDifficulty.setAlignmentY(Panel.CENTER_ALIGNMENT);

        // Button general UI
        buttonBack.setAlignmentX(Panel.CENTER_ALIGNMENT);
        buttonScreenOption1.setAlignmentX(Panel.CENTER_ALIGNMENT);
        buttonScreenOption2.setAlignmentX(Panel.CENTER_ALIGNMENT);
        buttonBackgroundMusicOption1.setAlignmentX(Panel.CENTER_ALIGNMENT);
        buttonBackgroundMusicOption2.setAlignmentX(Panel.CENTER_ALIGNMENT);
        buttonSoundEffectOption1.setAlignmentX(Panel.CENTER_ALIGNMENT);
        buttonSoundEffectOption2.setAlignmentX(Panel.CENTER_ALIGNMENT);
        difficultyOption1.setAlignmentX(Panel.CENTER_ALIGNMENT);
        difficultyOption2.setAlignmentX(Panel.CENTER_ALIGNMENT);
        difficultyOption3.setAlignmentX(Panel.CENTER_ALIGNMENT);

        // Set Button colors to reflect the current config settings
        if(Config.getScreenWidthConfig() == Panel.MAX_SCREEN_COLS) {
            buttonScreenOption1.setForeground(Color.BLACK);
            buttonScreenOption2.setForeground(Color.GRAY);
        } else {
            buttonScreenOption1.setForeground(Color.GRAY);
            buttonScreenOption2.setForeground(Color.BLACK);
        }
        // Button text color for the Background config
        if(Config.getBackgroundMusicConfig() == true) {
            buttonBackgroundMusicOption1.setForeground(Color.BLACK);
            buttonBackgroundMusicOption2.setForeground(Color.GRAY);
        } else {
            buttonBackgroundMusicOption1.setForeground(Color.GRAY);
            buttonBackgroundMusicOption2.setForeground(Color.BLACK);
        }
        // Button text color for the Sound Effect config
        if(Config.getSoundEffectConfig() == true) {
            buttonSoundEffectOption1.setForeground(Color.BLACK);
            buttonSoundEffectOption2.setForeground(Color.GRAY);
        } else {
            buttonSoundEffectOption1.setForeground(Color.GRAY);
            buttonSoundEffectOption2.setForeground(Color.BLACK);
        }
        // Button text color for the Difficulty config
        if(Config.getDifficultyConfig() == 3) {
            difficultyOption1.setForeground(Color.BLACK);
            difficultyOption2.setForeground(Color.GRAY);
            difficultyOption3.setForeground(Color.GRAY);
        } else if (Config.getDifficultyConfig() == 2) {
            difficultyOption1.setForeground(Color.GRAY);
            difficultyOption2.setForeground(Color.BLACK);
            difficultyOption3.setForeground(Color.GRAY);
        } else {
            difficultyOption1.setForeground(Color.GRAY);
            difficultyOption2.setForeground(Color.GRAY);
            difficultyOption3.setForeground(Color.BLACK);
        }
    }

    @Override
    public void loadImages() {
        // Not currently used
    }

    @Override
    public void draw(Graphics g) {
        // No graphics to draw, just reveal the menu based on the game state
        if(open) {
            settingsMenu.setVisible(true);
        } else {
            settingsMenu.setVisible(false);
        }
    }
}

