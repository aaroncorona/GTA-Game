package tile;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Tile {
    // Tile settings
    public String name;
    public BufferedImage image;
    public boolean causeCollision;

    // Constructor to create a type of background
    public Tile(String name, BufferedImage image, boolean causeCollision) {
        this.name = name;
        this.image = image;
        this.causeCollision = causeCollision;
    }
}
