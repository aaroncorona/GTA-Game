package tile;

import main.Panel;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.NoSuchElementException;
import java.util.Scanner;

// The Tile Manager creates all Tile objects based on the World Map text file
public class TileManager {

    // Singleton instance tracking
    private static TileManager instance = null;

    // Tile creation
    public static final int numTiles = 13;
    public static Tile[] tiles;

    // Tile Map
    public static final int worldMapRows = 44; // manually added
    public static final int worldMapCols = 41; // manually added
    public static int[][] tileMap;
    public static Rectangle[][] tileMapCollisionArea;

    // Private Constructor - Singleton class
    private TileManager() {
        tiles = new Tile[numTiles];

        // Establish tile data
        createTiles();
        loadTileMap();
    }

    // Singleton constructor method to ensure there is only 1 Tile manager obj per game
    public static TileManager getInstance() {
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
            Tile water = new Tile("Water", waterImage, true, 0);
            tiles[0] = water;

            // Sand for each direction (4 new tiles)
            for(int i=0; i<4; i++) {
                char direction = 'R';
                switch(i) {
                    case 0:
                        direction = 'R';
                        break;
                    case 1:
                        direction = 'L';
                        break;
                    case 2:
                        direction = 'U';
                        break;
                    case 3:
                        direction = 'D';
                        break;
                }
                String filePath = "/Users/aaroncorona/eclipse-workspace/GTA/src/assets/images/tiles/sand_";
                filePath += direction + ".png";
                BufferedImage sandImage = ImageIO.read(new File(filePath));
                Tile sand = new Tile("Sand_" + direction, sandImage, false, 3);
                tiles[1+i] = sand;
            }

            // Sidewalk
            BufferedImage sidewalkImage = ImageIO.read(new File("/Users/aaroncorona/eclipse-workspace/GTA/src/assets/images/tiles/sidewalk.png"));
            Tile sidewalk = new Tile("Sidewalk", sidewalkImage, false, 3);
            tiles[5] = sidewalk;

            // Road for each direction (4 new tiles)
            for(int i=0; i<4; i++) {
                char direction = 'R';
                switch(i) {
                    case 0:
                        direction = 'R';
                        break;
                    case 1:
                        direction = 'L';
                        break;
                    case 2:
                        direction = 'U';
                        break;
                    case 3:
                        direction = 'D';
                        break;
                }
                String filePath = "/Users/aaroncorona/eclipse-workspace/GTA/src/assets/images/tiles/road_";
                filePath += direction + ".png";
                BufferedImage sandImage = ImageIO.read(new File(filePath));
                Tile road2 = new Tile("Road_" + direction, sandImage, false, 0);
                tiles[6+i] = road2;
            }

            // Grass
            BufferedImage grassImage = ImageIO.read(new File("/Users/aaroncorona/eclipse-workspace/GTA/src/assets/images/tiles/grass.png"));
            Tile grass = new Tile("Grass", grassImage, false, 3);
            tiles[10] = grass;

            // Tree
            BufferedImage treeImage = ImageIO.read(new File("/Users/aaroncorona/eclipse-workspace/GTA/src/assets/images/tiles/tree.png"));
            Tile tree = new Tile("Tree", treeImage, true, 0);
            tiles[11] = tree;

            // Hut
            BufferedImage hutImage = ImageIO.read(new File("/Users/aaroncorona/eclipse-workspace/GTA/src/assets/images/tiles/hut.png"));
            Tile hut = new Tile("Hut", hutImage, true, 0);
            tiles[12] = hut;

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Helper method to load the tile map
    private void loadTileMap() {
        // Instantiate map arrays
        tileMap = new int[worldMapRows][worldMapCols];
        tileMapCollisionArea = new Rectangle[worldMapRows][worldMapCols];
        // Use a scanner to load the text file into the array
        try {
            Scanner scan = new Scanner(new File("/Users/aaroncorona/eclipse-workspace/GTA/src/assets/maps/tile_map.txt"));
            // Fill the tile maps
            for (int i = 0; i < tileMap.length; i++) {
                for (int j = 0; j < tileMap[i].length; j++) {
                    // Fill the int tile map (nums represent array pos in the tiles array)
                    int next;
                    try {
                        next = Integer.parseInt(scan.next());
                    } catch (NumberFormatException | NoSuchElementException e) {
                        next = 0; // can't parse 00
                    }
                    tileMap[i][j] = next;

                    // Fill the collision tile map (rects to represent the collision area)
                    int collisionSize;
                    if(TileManager.tiles[TileManager.tileMap[i][j]].causeCollision == true) {
                        collisionSize = 30; // assume the same collision size
                    } else {
                        collisionSize = 0; // no collision
                    }
                    tileMapCollisionArea[i][j] = new Rectangle(j * Panel.UNIT_SIZE+10, i * Panel.UNIT_SIZE+15,
                                                               collisionSize, collisionSize);
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
     }

    // Method to draw the tiles
    public void draw(Graphics g) {
        // Draw the map to the screen according to the camera settings
        int xPosCurrent = Camera.translateXMapToScreenPos()[0];
        int yPosCurrent = Camera.translateYMapToScreenPos()[0];
        for (int i = 0; i < tileMap.length; i++) {
            for (int j = 0; j < tileMap[i].length; j++) {
                 g.drawImage(tiles[tileMap[i][j]].image, xPosCurrent, yPosCurrent,
                             Panel.UNIT_SIZE, Panel.UNIT_SIZE, null);
                xPosCurrent += Panel.UNIT_SIZE;
            }
            xPosCurrent = Camera.translateXMapToScreenPos()[0]; // next row
            yPosCurrent += Panel.UNIT_SIZE;
        }
    }
}
