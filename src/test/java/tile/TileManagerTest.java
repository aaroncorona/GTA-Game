package tile;

import main.Panel;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TileManagerTest {

    // Test that the map is loaded properly, which should happen upon instantiation
    @Test
    public void whenCreated_MapArrayMatchesFileSize() {
        new Panel(); // dummy panel to initiate game state to mimic the conditions during production
        // Check the size of the array that processes the world map
        int tileMapArrayLengthExpected = 44 * 41; // rows & cols in the world map
        int tileMapArrayLengthActual = TileManager.tileMap.length * TileManager.tileMap[0].length;
        assertEquals(tileMapArrayLengthExpected, tileMapArrayLengthActual);
        // QA all background tiles
        for(int i = 0; i < TileManager.tileMap.length; i++) {
            for(int j = 0; j < TileManager.tileMap[i].length; j++) {
                // There should be no missing images or the game background will not load properly
                assertNotEquals(TileManager.tiles[TileManager.tileMap[i][j]].IMAGE, null);
                // Every Tile should have a movement cost for the pathfinder
                assertNotEquals(TileManager.tiles[TileManager.tileMap[i][j]].TRAVEL_COST, 0);
            }
        }
    }

    // Test that the Tile are not missing critical data
    @Test
    public void whenCreated_MapTilesAreFilledProperly() {
        new Panel();
        // QA all background tiles to make sure they have an image and path cost
        for(int i = 0; i < TileManager.tileMap.length; i++) {
            for(int j = 0; j < TileManager.tileMap[i].length; j++) {
                // There should be no missing images or the game background will not load properly
                assertNotEquals(TileManager.tiles[TileManager.tileMap[i][j]].IMAGE, null);
                // Every Tile should have a movement cost for the pathfinder
                assertNotEquals(TileManager.tiles[TileManager.tileMap[i][j]].TRAVEL_COST, 0);
            }
        }
    }

    // Test that the method getClosestTile() returns the closest tile for a given location
    @Test
    public void whenOnRoad_ClosestTileReturnsRoad() {
        new Panel();
        String tileNameExpected = "Road_U";
        String tileNameActual = TileManager.getClosestTile(901, 951).NAME;
        assertEquals(tileNameExpected, tileNameActual);
    }
}