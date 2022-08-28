package entity.car;

import main.ContactChecker;
import tile.TileManager;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;

public class CopCar extends SuperCar {

    // Constructor to create Cop NPC
    public CopCar(main.Panel panel) {
        this.panel = panel;

        setDefaultValues();
    }

    // Cop should spawn in a random location
    @Override
    public void setDefaultValues() {
        xPos = new Random().nextInt((int) (panel.SCREEN_WIDTH/panel.UNIT_SIZE)) * panel.UNIT_SIZE;
        yPos = new Random().nextInt((int) (panel.SCREEN_HEIGHT/panel.UNIT_SIZE)) * panel.UNIT_SIZE;
        direction = 'R';
        speed = panel.UNIT_SIZE; // default speed is moving 1 full position
        dead = false;

        collisionArea = new Rectangle(xPos, yPos + panel.UNIT_SIZE/4,
                                      panel.UNIT_SIZE, panel.UNIT_SIZE/2);

        // Reset if the Cop spawn on a tile that would cause an instant collision
        if(TileManager.tiles[TileManager.tileMap[yPos / panel.UNIT_SIZE][xPos / panel.UNIT_SIZE]].collision == true) {
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
        if(ContactChecker.checkCarCollision(this, panel.playerCar) == true) {
            dead = true;
        }
        // @TODO update after the bullet class is built
        // Check for a deadly collision with a bullet
//        if(ContactChecker.checkBulletCollision(this, panel.bullet) == true) {
//            death = true;
//            panel.running = false;
//            System.out.println("deadly collision - bullet");
//        }
    }

    @Override
    public BufferedImage getImage() {
        BufferedImage image = null;

        // Return the explosion image if the car has died
        if(dead == true) {
            String filePath = "/Users/aaroncorona/eclipse-workspace/GTA/src/assets/images/collisions/explosion.png";
            try {
                image = ImageIO.read(new File(filePath));
            } catch (IOException e) {
                e.printStackTrace();
            }
            return image;
        }

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
        return image;
    }

    @Override
    public void draw(Graphics g) {
        g.drawImage(getImage(), xPos, yPos, panel.UNIT_SIZE, panel.UNIT_SIZE, null);

        // ad hoc check of the collision area
//        g.setColor(Color.BLACK);
//        g.drawRect(collisionArea.x, collisionArea.y, collisionArea.width, collisionArea.height);
//        g.drawRect(xPos, yPos, 5, 5);
    }
}
