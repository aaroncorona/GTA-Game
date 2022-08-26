package entity;

public abstract class Car implements Entity {
    main.Panel panel;
    main.KeyHandler key;
    public int xPos, yPos;
    public char direction;
    public boolean nitro = false;
    // Images  @TODO determine if this can be added here?
//    BufferedImage

    // Default method implementation for setting default position to the center of the screen
    @Override
    public final void setDefaultValues() {
        xPos = 200;
        yPos = 200;
        direction = 'R';
        nitro = false;
    }
}

// @TODO find other commonalities for the abstract class?