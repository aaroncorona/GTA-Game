package tile;

import main.Panel;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class TileManager {

    public static main.Panel panel;
    public static Tile[] tiles;
    private static TileManager instance = null;

    // Private Constructor
    private TileManager() {
        tiles = new Tile[10];
    }

    // Use the Singleton design pattern to ensure there is only 1 Tile manager per game
    public static TileManager getInstance(Panel panel) {
        if(instance == null) {
            instance = new TileManager();
            return instance;
        } else {
            return instance;
        }
    }

    // Helper method to create Tile objects and put them in the array
    private void createTiles() {
        try {
            // Water
            BufferedImage waterImage = ImageIO.read(new File("/Users/aaroncorona/eclipse-workspace/GTA/src/assets/images/tiles/water.png"));
            Tile water = new Tile(waterImage, true);
            tiles[0] = water;

            // Road
            BufferedImage roadImage = ImageIO.read(new File("/Users/aaroncorona/eclipse-workspace/GTA/src/assets/images/tiles/road.png"));
            Tile road = new Tile(roadImage, true);
            tiles[1] = road;

            // Tree
            BufferedImage treeImage = ImageIO.read(new File("/Users/aaroncorona/eclipse-workspace/GTA/src/assets/images/tiles/tree.png"));
            Tile tree = new Tile(treeImage, true);
            tiles[2] = tree;
            // @TODO create building sprite

        } catch (IOException e) {}
    }

    // Method to draw the tiles
    public void draw(Graphics g) {
        createTiles();

        // @TODO create mapping text file
        g.drawImage(tiles[0].image, 150, 150, panel.UNIT_SIZE, panel.UNIT_SIZE, null);
        g.drawImage(tiles[1].image, 200, 200, panel.UNIT_SIZE, panel.UNIT_SIZE, null);
        g.drawImage(tiles[2].image, 250, 250, panel.UNIT_SIZE, panel.UNIT_SIZE, null);


    }
}
