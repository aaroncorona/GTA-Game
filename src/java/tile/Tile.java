package tile;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Tile {
    // Tile settings
    public String name;
    public BufferedImage image;
    public boolean causeCollision;
    public int movementCost;

    // Constructor to create a type of background
    public Tile(String name, BufferedImage image,
                boolean causeCollision, int movementCost) {
        this.name = name;
        this.image = image;
        this.causeCollision = causeCollision;
        this.movementCost = movementCost;
    }

    @Override
    public String toString() {
        return name;
    }
}
