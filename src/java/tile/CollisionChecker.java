package tile;

import entity.car.SuperCar;
import entity.item.SuperItem;

import java.awt.geom.Rectangle2D;

// The Contact Checker checks for overlap in collision area between objects
public class CollisionChecker {

    // Private constructor - Noninstantiable class
    private CollisionChecker() {}

    // Method to check for overlap between a Car and tile collision area
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

    // Method to check for overlap between an Item and tile collision area
    public static boolean checkTileCollision(SuperItem item) {
        boolean collision = false;
        for (int i = 0; i < TileManager.tileMapCollisionArea.length; i++) {
            for (int j = 0; j < TileManager.tileMapCollisionArea[i].length; j++) {
                Rectangle2D overlapArea = item.collisionArea.createIntersection(TileManager.tileMapCollisionArea[i][j]);
                if(overlapArea.getWidth() > 0 && overlapArea.getHeight() > 0) {
                    collision = true;
                }
            }
        }
        return collision;
    }

    // Method to see if a Node's closest tile causes a collision. This is more conservative than above for the pathfinder
    public static boolean checkTileCollision(PathFinder.Node node) {
        Tile currentTile = TileManager.getClosestTile(node.xMapPos, node.yMapPos);
        return currentTile.causeCollision;
    }

    // Method to check for a car colliding with another car
    public static boolean checkEntityCollision(SuperCar car1, SuperCar car2) {
        Rectangle2D overlapArea = car1.collisionArea.createIntersection(car2.collisionArea);
        if(overlapArea.getWidth() > 0 && overlapArea.getHeight() > 0) {
            return true;
        } else {
            return false;
        }
    }

    // Method to check for a car colliding with an item
    public static boolean checkEntityCollision(SuperCar car, SuperItem item) {
        Rectangle2D overlapArea = car.collisionArea.createIntersection(item.collisionArea);
        if(overlapArea.getWidth() > 0 && overlapArea.getHeight() > 0) {
            return true;
        } else {
            return false;
        }
    }

    // Method to check for an item colliding with an item
    public static boolean checkItemCollision(SuperItem item1, SuperItem item2) {
        Rectangle2D overlapArea = item1.collisionArea.createIntersection(item2.collisionArea);
        if(overlapArea.getWidth() > 0 && overlapArea.getHeight() > 0) {
            return true;
        } else {
            return false;
        }
    }
}
