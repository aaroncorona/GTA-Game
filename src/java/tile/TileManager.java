package tile;

import main.Panel;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Scanner;

public class TileManager {

    private static TileManager instance = null;
    public static main.Panel panel;
    public static Tile[] tiles;
    public static int[][] tileMap;

    // Private Constructor
    private TileManager() {
        tiles = new Tile[10];

        // Establish tile data
        createTiles();
        createTileMap();

//        // Print to confirm
//        for (int i = 0; i < tileMap.length; i++) {
//            for (int j = 0; j < tileMap[i].length; j++) {
//                System.out.print(tileMap[i][j] + " ");
//            }
//            System.out.println();
//        }
    }

    // Singleton constructor method to ensure there is only 1 Tile manager obj per game
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

            // Sidewalk
            BufferedImage sidewalkImage = ImageIO.read(new File("/Users/aaroncorona/eclipse-workspace/GTA/src/assets/images/tiles/sidewalk.png"));
            Tile sidewalk = new Tile(sidewalkImage, false);
            tiles[1] = sidewalk;

            // Road
            BufferedImage roadImage = ImageIO.read(new File("/Users/aaroncorona/eclipse-workspace/GTA/src/assets/images/tiles/road.png"));
            Tile road = new Tile(roadImage, false);
            tiles[2] = road;

            // Grass
            BufferedImage grassImage = ImageIO.read(new File("/Users/aaroncorona/eclipse-workspace/GTA/src/assets/images/tiles/grass.png"));
            Tile grass = new Tile(grassImage, false);
            tiles[3] = grass;

            // Tree
            BufferedImage treeImage = ImageIO.read(new File("/Users/aaroncorona/eclipse-workspace/GTA/src/assets/images/tiles/tree.png"));
            Tile tree = new Tile(treeImage, true);
            tiles[4] = tree;

            // Hut
            BufferedImage hutImage = ImageIO.read(new File("/Users/aaroncorona/eclipse-workspace/GTA/src/assets/images/tiles/hut.png"));
            Tile hut = new Tile(hutImage, true);
            tiles[5] = hut;


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Helper method to load the tile map
    private void createTileMap() {
        // Use a scanner to load the text file into the array
        tileMap = new int[panel.SCREEN_ROWS][panel.SCREEN_COLS];
        try {
            Scanner scan = new Scanner(new File("/Users/aaroncorona/eclipse-workspace/GTA/src/assets/maps/tile_map.txt"));
            for (int i = 0; i < tileMap.length; i++) {
                for (int j = 0; j < tileMap[i].length; j++) {
                    try {
                        tileMap[i][j] = Integer.parseInt(scan.next());
                    } catch (NumberFormatException e) {
                        tileMap[i][j] = 0; // can't parse 00
                    }
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
     }

    // Method to draw the tiles
    public void draw(Graphics g) {
        // Use the map data to draw each tile
        for (int i = 0; i < tileMap.length; i++) {
            for (int j = 0; j < tileMap[i].length; j++) {
                int tileNum = tileMap[i][j];
                g.drawImage(tiles[tileNum].image, j * panel.UNIT_SIZE, i * panel.UNIT_SIZE, panel.UNIT_SIZE, panel.UNIT_SIZE, null);
            }
        }
    }
}
