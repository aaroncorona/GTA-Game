package entity.car;

import entity.physics.CollisionChecker;
import main.KeyHandler;
import main.Panel;
import entity.item.ItemManager;
import main.Sound;
import tile.Tile;
import tile.TileManager;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class PlayerCar extends SuperCar {

    // Player should always be at the center of the screen
    public final int X_SCREEN_POS = Panel.SCREEN_WIDTH/2 - Panel.UNIT_SIZE/2;
    public final int Y_SCREEN_POS = Panel.SCREEN_HEIGHT/2 - Panel.UNIT_SIZE/2;

    public boolean hitTaken = false;

    // Constructor to create Player Car
    public PlayerCar() {
        setDefaultValues();
    }

    @Override
    public void update() {
        // Update location
        updateDir();
        updateLocation();
        // Manage game events
        handleSpeed();
        handleCollision();
        handleShooting();
        handleHealth();
    }

    // Helper Method to update the Car's direction based on key input
    private void updateDir() {
        // Key mappings for the player's direction
        if(KeyHandler.upPress) {
            direction = 'U';
            KeyHandler.upPress = false; // movement processed
        }
        if(KeyHandler.downPress) {
            direction = 'D';
            KeyHandler.downPress = false;
        }
        if(KeyHandler.rightPress) {
            direction = 'R';
            KeyHandler.rightPress = false;
        }
        if(KeyHandler.leftPress) {
            direction = 'L';
            KeyHandler.leftPress = false;
        }
    }

    // Helper method to update the player's coordinates and collision area based on the direction and speed
    private void updateLocation() {
        switch(direction) {
            case 'R':
                xMapPos = xMapPos + speed;
                collisionArea = new Rectangle(xMapPos, yMapPos + Panel.UNIT_SIZE/4,
                                              Panel.UNIT_SIZE, Panel.UNIT_SIZE/2);
                break;
            case 'L':
                xMapPos = xMapPos - speed;
                collisionArea = new Rectangle(xMapPos, yMapPos + Panel.UNIT_SIZE/4,
                                              Panel.UNIT_SIZE, Panel.UNIT_SIZE/2);
                break;
            case 'U':
                yMapPos = yMapPos - speed;
                collisionArea = new Rectangle(xMapPos + Panel.UNIT_SIZE/4, yMapPos,
                                        Panel.UNIT_SIZE/2, Panel.UNIT_SIZE);
                break;
            case 'D':
                yMapPos = yMapPos + speed;
                collisionArea = new Rectangle(xMapPos + Panel.UNIT_SIZE/4, yMapPos,
                                        Panel.UNIT_SIZE/2, Panel.UNIT_SIZE);
                break;
        }
    }

    // Helper Method to update the speed based on Tile traversal cost and Nitro
    private void handleSpeed() {
        // Handle nitro
        int nitroBoost = 10;
        // Check for the R key to toggle nitro
        if(KeyHandler.rPress && !nitro) {
            nitro = true;
            speed = speed + nitroBoost; // nitro on
            KeyHandler.rPress = false;
        }
        if(KeyHandler.rPress && nitro) {
            nitro = false;
            speed = speed - nitroBoost; // nitro off
            KeyHandler.rPress = false;
        }
        // Check if the car is on a Tile with a traversal cost
        Tile currentTile = TileManager.getClosestTile(xMapPos, yMapPos);
        // Update speed based on the tile traversal cost
        if(nitro) {
            if(currentTile.TRAVEL_COST == 0) { // reset
                speed = DEFAULT_SPEED + nitroBoost;
            } else if(currentTile.TRAVEL_COST >= 1 // initial slowdown
                    && speed == DEFAULT_SPEED + nitroBoost) {
                speed = speed - currentTile.TRAVEL_COST;
            }
        }
        if(!nitro) {
            if(currentTile.TRAVEL_COST == 0) { // reset
                speed = DEFAULT_SPEED;
            } else if(currentTile.TRAVEL_COST >= 1 // initial slowdown
                    && speed == DEFAULT_SPEED) {
                speed = speed - currentTile.TRAVEL_COST;
            }
        }
    }

    // Helper method to respond to collision events that should reduce health
    private void handleCollision() {
        // First, check for a collision with a tile
        if(CollisionChecker.checkTileCollision(this) == true) {
            // Reduce health
            hitTaken = true;
            // Move away from impact
            switch(direction) {
                case 'R':
                    direction = 'L';
                    break;
                case 'L':
                    direction = 'R';
                    break;
                case 'U':
                    direction = 'D';
                    break;
                case 'D':
                    direction = 'U';
                    break;
            }
            updateLocation();
        }
        // Second, check for a collision with a cop car
        for(int i = 0; i < CopCarManager.cops.size(); i++) {
            if(CollisionChecker.checkEntityCollision(this, CopCarManager.cops.get(i)) == true) {
                // Reduce health
                hitTaken = true;
                CopCarManager.cops.get(i).hitTaken = true;
                // Move away from impact
                switch(direction) {
                    case 'R':
                        direction = 'L';
                        yMapPos = yMapPos - speed; // avoid getting stuck on a tile
                        break;
                    case 'L':
                        direction = 'R';
                        yMapPos = yMapPos + speed;
                        break;
                    case 'U':
                        direction = 'D';
                        xMapPos = xMapPos - speed;
                        break;
                    case 'D':
                        direction = 'U';
                        xMapPos = xMapPos + speed;
                        break;
                }
                updateLocation();
            }
        }
    }

    // Helper Method to create a bullet based on key input
    private void handleShooting() {
        // E key to shoot bullet
        if(KeyHandler.ePress) {
            // Spawn the bullet a safe distance from the player to avoid instant death
            switch(direction) {
                case 'R':
                    ItemManager.createBullet(xMapPos + Panel.UNIT_SIZE, yMapPos, direction);
                    break;
                case 'L':
                    ItemManager.createBullet(xMapPos - Panel.UNIT_SIZE, yMapPos, direction);
                    break;
                case 'U':
                    ItemManager.createBullet(xMapPos, yMapPos - Panel.UNIT_SIZE, direction);
                    break;
                case 'D':
                    ItemManager.createBullet(xMapPos, yMapPos + Panel.UNIT_SIZE, direction);
                    break;
            }
            KeyHandler.ePress = false;
            Sound.playShot();
        }
    }

    // Helper method to update player health based on hits taken
    private void handleHealth() {
        // First, check for a collision with a tile
        if(hitTaken) {
            health--;
        }
    }

    @Override
    public void loadImages() {
        // Get the explosion image if the car has died
        if(hitTaken) {
            String filePath = "/Users/aaroncorona/eclipse-workspace/GTA/src/assets/images/collisions/explosion.png";
            try {
                imageCar = ImageIO.read(new File(filePath));
            } catch (IOException e) {
                e.printStackTrace();
            }
            hitTaken = false; // reset state
        } else {
            // Otherwise, return the correct sprite
            String filePath = "/Users/aaroncorona/eclipse-workspace/GTA/src/assets/images/entities/player_car/player_car_";
            // Update the file path for the direction
            filePath += direction;
            // Update the file path for the nitro status
            if(nitro) {
                filePath += "_nitro";
            }
            filePath += ".png";
            try {
                imageCar = ImageIO.read(new File(filePath));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        // Get the correct health bar image
        String filePath = "/Users/aaroncorona/eclipse-workspace/GTA/src/assets/images/entities/player_car/health_";
        filePath += health;
        filePath += ".png";
        try {
            imageHealth = ImageIO.read(new File(filePath));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void draw(Graphics g) {
        if(Panel.titleState == false) {
            loadImages();
            // Draw player in center
            g.drawImage(imageCar, X_SCREEN_POS, Y_SCREEN_POS, Panel.UNIT_SIZE, Panel.UNIT_SIZE, null);
            // Draw health
            g.drawImage(imageHealth, 350, 3, Panel.UNIT_SIZE*4, Panel.UNIT_SIZE-5, null);
        }
    }
}
