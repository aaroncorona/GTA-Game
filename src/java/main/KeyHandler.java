package main;

import tile.Tile;
import tile.TileManager;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

// Class to map key inputs to public variables to inform other classes
public class KeyHandler implements KeyListener {

    // Singleton instance tracking
    private static KeyHandler instance = null;

    // Variables to track when a key is pressed
    public static boolean upPress, downPress, rightPress, leftPress,
                          spacePress, backSpacePress, enterPress,
                          cPress, ePress, rPress;

    // Private Constructor - Singleton class
    private KeyHandler() {}

    // Singleton constructor method to ensure there is only 1 Tile manager obj per game
    public static KeyHandler getInstance() {
        if(instance == null) {
            instance = new KeyHandler();
            return instance;
        } else {
            return instance;
        }
    }

    public static void setDefaultValues() {
        upPress = false;
        downPress = false;
        rightPress = false;
        leftPress = false;
        spacePress = false;
        backSpacePress = false;
        enterPress = false;
        cPress = false;
        ePress = false;
        rPress = false;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_UP) {
             upPress = true;
        }
        if(e.getKeyCode() == KeyEvent.VK_DOWN) {
            downPress = true;
        }
        if(e.getKeyCode() == KeyEvent.VK_RIGHT) {
            rightPress = true;
        }
        if(e.getKeyCode() == KeyEvent.VK_LEFT) {
            leftPress = true;
        }
        if(e.getKeyCode() == KeyEvent.VK_SPACE) {
            spacePress = true;
        }
        if(e.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
            backSpacePress = true;
        }
        if(e.getKeyCode() == KeyEvent.VK_ENTER) {
            enterPress = true;
        }
        if(e.getKeyCode() == KeyEvent.VK_C) {
            cPress = true;
        }
        if(e.getKeyCode() == KeyEvent.VK_E) {
            ePress = true;
        }
        if(e.getKeyCode() == KeyEvent.VK_R) {
            rPress = true;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        // Not currently used
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // Not currently used
    }
}

