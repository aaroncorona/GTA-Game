package entity.car;

import main.CollisionChecker;
import main.Panel;
import tile.TileManager;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.Random;

public class CopCar extends SuperCar {

    // Constructor to create Cop NPC
    public CopCar() {
        setDefaultValues();
    }

    // Cop should spawn in a random location
    @Override
    public void setDefaultValues() {
        xPos = new Random().nextInt((int) (Panel.SCREEN_WIDTH/Panel.UNIT_SIZE)) * Panel.UNIT_SIZE;
        yPos = new Random().nextInt((int) (Panel.SCREEN_HEIGHT/Panel.UNIT_SIZE)) * Panel.UNIT_SIZE;
        direction = 'R';
        speed = Panel.UNIT_SIZE; // default speed is moving 1 full position
        dead = false;

        collisionArea = new Rectangle(xPos, yPos + Panel.UNIT_SIZE/4,
                                      Panel.UNIT_SIZE, Panel.UNIT_SIZE/2);

        // Reset if the Cop spawn on a tile that would cause an instant collision
        if(TileManager.tiles[TileManager.tileMap[yPos / Panel.UNIT_SIZE][xPos / Panel.UNIT_SIZE]].collision == true) {
            setDefaultValues();
        }
    }

    @Override
    public void update() {
        // Manage events
        handleDeadlyCollision();
    }

    // Helper method to respond to collision events that should end the game
    private void handleDeadlyCollision() {
        // Check for a deadly collision with the player
        if(CollisionChecker.checkEntityCollision(this, Panel.playerCar) == true) {
            dead = true;
        }
        // @TODO update after the bullet class is built
        // Check for a deadly collision with a bullet, which creates money
//        if(ContactChecker.checkBulletCollision(this, panel.bullet) == true) {
//            death = true;
//        ItemManager.createMoney(xPos, yPos);

//        }
    }

    @Override
    public void loadImage() {
        // Return the explosion image if the car has died
        if(dead == true) {
            String filePath = "/Users/aaroncorona/eclipse-workspace/GTA/src/assets/images/collisions/explosion.png";
            try {
                image = ImageIO.read(new File(filePath));
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            // Otherwise, return the correct sprite
            String filePath = "/Users/aaroncorona/eclipse-workspace/GTA/src/assets/images/entities/cop_car/cop_car_";
            // Update the file path for the direction
            filePath += direction;
            // Update the file path for the nitro status
            if(nitro == true) {
                filePath += "_nitro";
            }
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
