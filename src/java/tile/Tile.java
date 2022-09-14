package tile;

import java.awt.image.BufferedImage;

public class Tile {
    // Tile settings
    public final String NAME;
    public final boolean COLLISION;
    public final int TRAVEL_COST;
    public final BufferedImage IMAGE;

    // Constructor to create a type of background
    public Tile(String name, BufferedImage image,
                boolean collision, int travelCost) {
        this.NAME = name;
        this.COLLISION = collision;
        this.TRAVEL_COST = travelCost;
        this.IMAGE = image;
    }

    @Override
    public String toString() {
        return NAME;
    }
}
