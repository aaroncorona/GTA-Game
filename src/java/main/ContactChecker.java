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
        // @TODO loop through all tiles touched to check for a collision
        boolean collision = ContactChecker.getTileTouched(car).collision;
        return collision;
    }

    // Method to get the background tile in the entity's collision area
    public static Tile getTileTouched(Car car) {
        // @TODO determine what tiles the collision area is on, change to Tile[] getTilesTouched()
        Tile currentTile = TileManager.tiles[TileManager.tileMap[car.yPos/Panel.UNIT_SIZE][car.xPos/Panel.UNIT_SIZE]];
        return currentTile;
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
