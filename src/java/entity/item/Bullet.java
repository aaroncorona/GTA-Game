package entity.item;

import entity.car.CopCarManager;
import main.CollisionChecker;
import main.Panel;
import tile.TileManager;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class Bullet extends SuperItem {

    // Constructor to create a single Bullet item. Only the TileManager should use this method
    protected Bullet(int xMapPos, int yMapPos, char direction) {
        this.xMapPos = xMapPos;
        this.yMapPos = yMapPos;
        this.direction = direction;

        setDefaultValues();
    }


    // Default method implementation for setting the reset position
    @Override
    public void setDefaultValues() {
        type = 0;
        dead = false;
        speed = 25;
        collisionArea = new Rectangle(xMapPos, yMapPos + Panel.UNIT_SIZE/4,
                                      Panel.UNIT_SIZE, Panel.UNIT_SIZE/2);
    }

    @Override
    public void update() {
        // Update location
        updateLocation();
        // Handle events
        handleCollision();
    }

    // Helper method to update the bullet coordinates
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

    // Helper method to respond to the bullet hitting a tile
    private void handleCollision() {
        // Check for a tile collision
        if(CollisionChecker.checkTileCollision(this) == true) {
            // Bullet blows up only
            dead = true;
        }
        // Check for a player collision
        if(CollisionChecker.checkEntityCollision(Panel.playerCar, this) == true) {
            // Bullet blows up
            dead = true;
            // Reduce player health
            Panel.playerCar.health--;
        }
        // Check for a cop collision
        for(int i = 0; i < CopCarManager.cops.size(); i++) {
            if(CollisionChecker.checkEntityCollision(CopCarManager.cops.get(i), this) == true) {
                // Bullet blows up
                dead = true;
                // Car blows up
                CopCarManager.cops.get(i).health = 0;
            }
        }
        // Check for a collision with any other item
        for (int i = 0; i < ItemManager.items.size(); i++) {
            SuperItem item = ItemManager.items.get(i);
            if(!item.equals(this)
               && CollisionChecker.checkEntityCollision(item, this) == true) {
                // 2 Bullets blows up, but not money
                dead = true;
                if(item.type == 1) {
                    item.dead = true;
                }
            }
        }
    }

    @Override
    public void loadImages() {
        // Check if the bullet should explode
        if(dead == true) {
            String filePath = "/Users/aaroncorona/eclipse-workspace/GTA/src/assets/images/collisions/explosion.png";
            try {
                imageItem = ImageIO.read(new File(filePath));
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            // Otherwise, return the correct sprite
            String filePath = "/Users/aaroncorona/eclipse-workspace/GTA/src/assets/images/items/bullet/bullet_";
            filePath += direction;
            filePath += ".png";
            try {
                imageItem = ImageIO.read(new File(filePath));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void draw(Graphics g) {
        loadImages();
        int xScreenPos = TileManager.tileMapScreenXPos[xMapPos/Panel.UNIT_SIZE];
        int yScreenPos = TileManager.tileMapScreenYPos[yMapPos/Panel.UNIT_SIZE];
        g.drawImage(imageItem, xScreenPos * Panel.UNIT_SIZE, yScreenPos * Panel.UNIT_SIZE, Panel.UNIT_SIZE, Panel.UNIT_SIZE, null);

        // ad hoc check of the collision area
//        g.setColor(Color.BLACK);
//        g.drawRect(collisionArea.x, collisionArea.y, collisionArea.width, collisionArea.height);
//        g.drawRect(xPos, yPos, 5, 5);
    }
}
