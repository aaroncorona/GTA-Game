package main;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

// Class to map key inputs to public variables to inform other classes
public class KeyHandler implements KeyListener {

    // Variables to track when a key is pressed
    public boolean upPress, downPress, rightPress, leftPress,
                   spacePress, backSpacePress, enterPress, ePress, rPress;

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

