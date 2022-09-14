package tile;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TileManagerTest {

    // Test that the map is loaded properly, which should happen upon instantiation
    @Test
    public void whenCreated_MapArrayMatchesFileSize() {
        TileManager tileManager = TileManager.getInstance();
        // Check the size of the array that processes the world map
        int tileMapArrayLengthExpected = 44 * 41; // rows & cols in the world map
        int tileMapArrayLengthActual = tileManager.tileMap.length * tileManager.tileMap[0].length;
        assertEquals(tileMapArrayLengthExpected, tileMapArrayLengthActual);
        // QA all background tiles
        for(int i = 0; i < tileManager.tileMap.length; i++) {
            for(int j = 0; j < tileManager.tileMap[i].length; j++) {
                // There should be no missing images or the game background will not load properly
                assertNotEquals(tileManager.tiles[tileManager.tileMap[i][j]].image, null);
                // Every Tile should have a movement cost for the pathfinder
                assertNotEquals(tileManager.tiles[tileManager.tileMap[i][j]].movementCost, 0);
            }
        }
    }

    // Test that the Tile are not missing critical data
    @Test
    public void whenCreated_MapTilesAreFilledProperly() {
        TileManager tileManager = TileManager.getInstance();
        // QA all background tiles to make sure they have an image and path cost
        for(int i = 0; i < tileManager.tileMap.length; i++) {
            for(int j = 0; j < tileManager.tileMap[i].length; j++) {
                // There should be no missing images or the game background will not load properly
                assertNotEquals(tileManager.tiles[tileManager.tileMap[i][j]].image, null);
                // Every Tile should have a movement cost for the pathfinder
                assertNotEquals(tileManager.tiles[tileManager.tileMap[i][j]].movementCost, 0);
            }
        }
    }

    // Test that the method getClosestTile() returns the closest tile for a given location
    @Test
    public void whenOnRoad_ClosestTileReturnsRoad() {
        TileManager tileManager = TileManager.getInstance();
        String tileNameExpected = "Road_U";
        String tileNameActual = tileManager.getClosestTile(901, 951).name;
        assertEquals(tileNameExpected, tileNameActual);
    }
}