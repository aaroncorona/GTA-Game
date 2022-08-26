package entity;

import java.awt.image.BufferedImage;

public abstract class Car implements Entity {
    main.Panel panel; // @TODO determine if needed?
    main.KeyHandler key;
    public int xPos, yPos;
    public char direction;
    // Images  @TODO determine if this can be added here?
//    BufferedImage

    // Default method implementation for setting default position to the center of the screen
    @Override
    public final void setDefaultValues() {
        xPos = 100;
        yPos = 100;
        direction = 'R';
    }
}

// @TODO find other commonalities for the abstract class?