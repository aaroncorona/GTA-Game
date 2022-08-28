package main;

import entity.Car;
import tile.Tile;
import tile.TileManager;

import java.awt.geom.Rectangle2D;

public class ContactChecker {

    // Private constructor - Noninstantiable class
    private ContactChecker() {}

    // Method to check for a car colliding with a tile
    public static boolean checkTileCollision(Car car) {
        Tile[] tilesTouched = getTilesTouched(car);
        for(int i=0; i < tilesTouched.length; i++) {
            if(tilesTouched[i].collision == true) {
                return true;
            }
        }
        return false;
    }

    // Method to get the background tile in the entity's collision area
    public static Tile[] getTilesTouched(Car car) {
        // Get the tiles that the 4 corners of the collision rectangle are on
        int carXPos1 = car.collisionArea.x;
        int carYPos1 = car.collisionArea.y;
        int carXPos2 = car.collisionArea.x + car.collisionArea.width;
        int carYPos2 = car.collisionArea.y;
        int carXPos3 = car.collisionArea.x;
        int carYPos3 = car.collisionArea.y + car.collisionArea.height;
        int carXPos4 = car.collisionArea.x + car.collisionArea.width;
        int carYPos4 = car.collisionArea.y + car.collisionArea.height;

        // Get the corresponding 4 Tiles and add them to the array
        Tile[] tilesTouched = new Tile[4];
        Tile tile1 = TileManager.tiles[TileManager.tileMap[carYPos1/Panel.UNIT_SIZE][carXPos1/Panel.UNIT_SIZE]];
        tilesTouched[0] = tile1;
        Tile tile2 = TileManager.tiles[TileManager.tileMap[carYPos2/Panel.UNIT_SIZE][carXPos2/Panel.UNIT_SIZE]];
        tilesTouched[1] = tile2;
        Tile tile3 = TileManager.tiles[TileManager.tileMap[carYPos3/Panel.UNIT_SIZE][carXPos3/Panel.UNIT_SIZE]];
        tilesTouched[2] = tile3;
        Tile tile4 = TileManager.tiles[TileManager.tileMap[carYPos4/Panel.UNIT_SIZE][carXPos4/Panel.UNIT_SIZE]];
        tilesTouched[3] = tile4;

        return tilesTouched;
    }

    // Method to check for a car colliding with another car
    public static boolean checkCarCollision(Car car1, Car car2) {
        Rectangle2D overlapArea = car1.collisionArea.createIntersection(car2.collisionArea);
        if(overlapArea.getWidth() >= 0 && overlapArea.getHeight() >= 0) {
            return true;
        } else {
            return false;
        }
    }

    // @TODO update after the money class is built
    // Method to check for a car colliding with money
//    public static boolean checkMoneyCollision(Car entity) {
//        return null;
//    }

    // @TODO update after the bullet class is built
    // Method to check for a car colliding with money
//    public static boolean checkBulletCollision(Car entity) {
//        return null;
//    }
}
