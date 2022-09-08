package tile;

import entity.car.SuperCar;
import entity.item.SuperItem;
import main.Panel;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.util.HashMap;

// The Contact Checker checks for overlap in collision area between objects
public class CollisionChecker {

    // Create a cache of Node collision results given each Node will not change positions
    private static HashMap<PathFinder.Node, Boolean> nodeCollisionResults = new HashMap<>();

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

    // Method to see if a node lands on any tile that may cause a collision (for the pathfinder)
    public static boolean checkTileCollision(PathFinder.Node node) {
        boolean collision = false;
        // First, check if we have already processed this node.
        if(nodeCollisionResults.containsKey(node)) {
            collision = nodeCollisionResults.get(node);
            return collision;
        }
        // Otherwise, Check if the Node's collision area intersects with any tile collision area
        // Create a collision area for the Node that spans an entire tile, then loop through the map
        Rectangle2D nodeCollisionArea = new Rectangle(node.xMapPos, node.yMapPos, Panel.UNIT_SIZE, Panel.UNIT_SIZE);
        for (int i = 0; i < TileManager.tileMapCollisionArea.length; i++) {
            for (int j = 0; j < TileManager.tileMapCollisionArea[i].length; j++) {
                Rectangle2D overlapArea = nodeCollisionArea.createIntersection(TileManager.tileMapCollisionArea[i][j]);
                if(overlapArea.getWidth() > 0 && overlapArea.getHeight() > 0) {
                    collision = true;
                }
            }
        }
        nodeCollisionResults.put(node, collision);
        return collision;
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
