package main;

import entity.Car;
import tile.Tile;
import tile.TileManager;

public class ContactChecker {

    // Private constructor - Noninstantiable class
    private ContactChecker() {}

    // Method to check for a car colliding with a tile
    public static boolean checkTileCollision(Car car) {
        Tile currentTile = getTileTouched(car);
        boolean collision = ContactChecker.getTileTouched(car).collision;
        return collision;
    }

    // Mthod to get the background tile in the entity's collision area
    public static Tile getTileTouched(Car car) {
        Tile currentTile = TileManager.tiles[TileManager.tileMap[car.yPos/Panel.UNIT_SIZE][car.xPos/Panel.UNIT_SIZE]];
        return currentTile;
    }

    // Method to check for a car colliding with another car
    public static boolean checkCarCollision(Car car1, Car car2) {
        if(car1.xPos == car2.xPos
           && car1.yPos == car2.yPos) {
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
