package physics;

import entity.car.SuperEntity;
import tile.Tile;
import tile.TileManager;

import java.awt.geom.Rectangle2D;

// The Contact Checker checks for overlap in collision area between objects
public class CollisionChecker {

    // Private constructor - Noninstantiable class
    private CollisionChecker() {}

    // Method to check for overlap between an Entity (Car or Item) and the Tile collision area
    public static <T extends SuperEntity> boolean checkTileCollision(T t) {
        boolean collision = false;
        for (int i = 0; i < TileManager.tileMapCollisionArea.length; i++) {
            for (int j = 0; j < TileManager.tileMapCollisionArea[i].length; j++) {
                Rectangle2D overlapArea = t.collisionArea.createIntersection(TileManager.tileMapCollisionArea[i][j]);
                if(overlapArea.getWidth() > 0 && overlapArea.getHeight() > 0) {
                    collision = true;
                }
            }
        }
        return collision;
    }

    // Method to see if a Node's closest tile causes a collision. This is more conservative than above for the pathfinder
    public static boolean checkTileCollision(PathFinder.Node node) {
        Tile currentTile = TileManager.getClosestTile(node.X_MAP_POS, node.Y_MAP_POS);
        return currentTile.COLLISION;
    }

    // Method to check for 2 entities colliding (i.e. car on car, car on item, or item on item)
    public static <T extends SuperEntity> boolean checkEntityCollision(T t1, T t2) {
        Rectangle2D overlapArea = t1.collisionArea.createIntersection(t2.collisionArea);
        if(overlapArea.getWidth() > 0 && overlapArea.getHeight() > 0) {
            return true;
        } else {
            return false;
        }
    }
}
