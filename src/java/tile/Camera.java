package tile;

import main.Panel;

// The Camera determines how tiles should be painted on the screen
public class Camera {

    // Private constructor - Noninstantiable class
    private Camera() {}

    // Get the x screen locations for each map location
    public static int[] translateXMapToScreenPos() {
        // This is where the furthest left map tile should be drawn on the screen (usually negative/not drawn)
        int xMapStartScreenPos = Panel.playerCar.X_SCREEN_POS - Panel.playerCar.xMapPos;
        // Fill position translation array. This is the Rosetta Stone for drawing the map on the screen horizontally
        int xMapScreenPos[] = new int[TileManager.worldMapCols * Panel.UNIT_SIZE];
        int xCurr = xMapStartScreenPos;
        for (int i = 0; i < xMapScreenPos.length; i++) {
            xMapScreenPos[i] = xCurr;
            xCurr++;
        }
        return xMapScreenPos;
    }

    // Get the y screen locations for each map location
    public static int[] translateYMapToScreenPos() {
        // This is where the furthest up map tile should be drawn on the screen
        int yMapStartScreenPos = Panel.playerCar.Y_SCREEN_POS - Panel.playerCar.yMapPos;
        // Fill position translation array. This is the Rosetta Stone for drawing the map on the screen vertically
        int yMapScreenPos[] = new int[TileManager.worldMapRows * Panel.UNIT_SIZE];
        int yCurr = yMapStartScreenPos;
        for (int i = 0; i < yMapScreenPos.length; i++) {
            yMapScreenPos[i] = yCurr;
            yCurr++;
        }
        return yMapScreenPos;
    }

}
