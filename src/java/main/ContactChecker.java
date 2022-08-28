package main;

import entity.car.SuperCar;
import tile.Tile;
import tile.TileManager;

import java.awt.geom.Rectangle2D;

public class ContactChecker {

    // Private constructor - Noninstantiable class
    private ContactChecker() {}

    // Method to check for overlap between the entity and tile collision area
    public static boolean checkTileCollision(SuperCar car) {
        boolean collision = false;
        for (int i = 0; i < TileManager.tileMapCollisionArea.length; i++) {
            for (int j = 0; j < TileManager.tileMapCollisionArea[i].length; j++) {
                Rectangle2D overlapArea = car.collisionArea.createIntersection(TileManager.tileMapCollisionArea[i][j]);
                if(overlapArea.getWidth() > 0 && overlapArea.getHeight() > 0) {
                    collision = true;
                }
            }
        }
        return collision;
    }

    // Method to check for a car colliding with another car
    public static boolean checkCarCollision(SuperCar car1, SuperCar car2) {
        Rectangle2D overlapArea = car1.collisionArea.createIntersection(car2.collisionArea);
        if(overlapArea.getWidth() > 0 && overlapArea.getHeight() > 0) {
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
