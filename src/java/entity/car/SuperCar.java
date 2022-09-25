package entity.car;

import main.Panel;
import entity.car.SuperEntity;

import java.awt.*;
import java.awt.image.BufferedImage;

// This super class provides a foundation for creating car classes
public abstract class SuperCar extends SuperEntity {
    // Car specific settings
    public final int DEFAULT_SPEED = 5; // pixels to move per frame
    public boolean nitro;
    BufferedImage imageCar;
    BufferedImage imageHealth;

    // Default method implementation for setting the reset position
    @Override
    public void setDefaultValues() {
        xMapPos = 900;
        yMapPos = 950;
        direction = 'R';
        speed = DEFAULT_SPEED;
        nitro = false;
        hitTaken = false;
        health = 3;
        collisionArea = new Rectangle(xMapPos, yMapPos + Panel.UNIT_SIZE/4,
                                      Panel.UNIT_SIZE, Panel.UNIT_SIZE/2);
    }
}
