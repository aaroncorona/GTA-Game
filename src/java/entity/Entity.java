package entity;

import java.awt.*;

public interface Entity {
    // Methods that every Entity should use
    public void setDefaultValues();
    public void loadImages();
    public void updateData();
    public void draw(Graphics g);
}

// @TODO find other commonalities for the interface to require?
