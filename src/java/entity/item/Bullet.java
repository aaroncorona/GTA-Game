package entity.item;

import main.CollisionChecker;
import main.Panel;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class Bullet extends SuperItem {

    // Constructor to create a single Bullet item. Only the TileManager should use this method
    protected Bullet(int xPos, int yPos, char direction) {
        this.xPos = xPos;
        this.yPos = yPos;
        this.direction = direction;

        setDefaultValues();
    }


    // Default method implementation for setting the reset position
    @Override
    public void setDefaultValues() {
        speed = 25;
        dead = false;
        collisionArea = new Rectangle(xPos, yPos + Panel.UNIT_SIZE/4,
                                      Panel.UNIT_SIZE, Panel.UNIT_SIZE/2);
    }

    @Override
    public void update() {
        // Update location
        updateLocation();
        // Handle events
        handleDeadlyCollision();
    }

    // Helper method to update the bullet coordinates
    private void updateLocation() {
        switch(direction) {
            case 'R':
                xPos = xPos + speed;
                collisionArea = new Rectangle(xPos, yPos + Panel.UNIT_SIZE/4,
                                              Panel.UNIT_SIZE, Panel.UNIT_SIZE/2);
                break;
            case 'L':
                xPos = xPos - speed;
                collisionArea = new Rectangle(xPos, yPos + Panel.UNIT_SIZE/4,
                                              Panel.UNIT_SIZE, Panel.UNIT_SIZE/2);
                break;
            case 'U':
                yPos = yPos - speed;
                collisionArea = new Rectangle(xPos+ Panel.UNIT_SIZE/4, yPos,
                                        Panel.UNIT_SIZE/2, Panel.UNIT_SIZE);
                break;
            case 'D':
                yPos = yPos + speed;
                collisionArea = new Rectangle(xPos+ Panel.UNIT_SIZE/4, yPos,
                                        Panel.UNIT_SIZE/2, Panel.UNIT_SIZE);
                break;
        }
    }

    // Helper method to respond to the bullet hitting a tile
    private void handleDeadlyCollision() {
        // Check for a tile collision
        if(CollisionChecker.checkTileCollision(this) == true) {
            dead = true;
        }
        // Check for a player collision
        if(CollisionChecker.checkEntityCollision(Panel.playerCar, this) == true) {
            dead = true;
            Panel.playerCar.dead = true;
            System.out.println("Deadly Collision - Bullet");
        }
        // Check for a cop collision
        if(CollisionChecker.checkEntityCollision(Panel.copCar, this) == true) {
            dead = true;
            Panel.copCar.dead = true;
        }
    }

    @Override
    public void loadImage() {
        // Check if the bullet should explode
        if(dead == true) {
            String filePath = "/Users/aaroncorona/eclipse-workspace/GTA/src/assets/images/collisions/explosion.png";
            try {
                image = ImageIO.read(new File(filePath));
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            // Otherwise, return the correct sprite
            String filePath = "/Users/aaroncorona/eclipse-workspace/GTA/src/assets/images/items/bullet/bullet_";
            filePath += direction;
            filePath += ".png";
            try {
                image = ImageIO.read(new File(filePath));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void draw(Graphics g) {
        loadImage();
        g.drawImage(image, xPos, yPos, Panel.UNIT_SIZE, Panel.UNIT_SIZE, null);

        // ad hoc check of the collision area
//        g.setColor(Color.BLACK);
//        g.drawRect(collisionArea.x, collisionArea.y, collisionArea.width, collisionArea.height);
//        g.drawRect(xPos, yPos, 5, 5);
    }
}
