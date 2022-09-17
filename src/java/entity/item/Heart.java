package entity.item;

import main.Panel;
import tile.Camera;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;

import static entity.physics.CollisionChecker.checkEntityCollision;

public class Heart extends SuperItem {

    // Protected Constructor to create a single Health item. Only the TileManager should use this method
    protected Heart(int xPos, int yPos) {
        this.xMapPos = xPos;
        this.yMapPos = yPos;

        setDefaultValues();
    }

    // Default method implementation for setting the reset position
    @Override
    public void setDefaultValues() {
        itemType = 2;
        dead = false;
        direction = 'R'; // direction does not apply
        speed = 0; // speed does not apply
        collisionArea = new Rectangle(xMapPos, yMapPos, Panel.UNIT_SIZE, Panel.UNIT_SIZE);
    }

    @Override
    public void update() {
        // Handle events
        handleCollection();
    }

    // Helper method to check if the given Health object is contacted by the player, whereby
    // a "collection" should occur and increment the player's health
    private void handleCollection() {
        if(checkEntityCollision(Panel.playerCar, this) == true) {
            if(Panel.playerCar.health < 3) {
                Panel.playerCar.health++;
            }
            dead = true;
            ItemManager.createHeart(); // create replacement obj
        }
    }

    @Override
    public void loadImages() {
        String filePath = "/Users/aaroncorona/eclipse-workspace/GTA/src/assets/images/items/heart.png";
        try {
            imageItem = ImageIO.read(new File(filePath));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void draw(Graphics g) {
        loadImages();
        int xScreenPos = Camera.translateXMapToScreenPos()[xMapPos];
        int yScreenPos = Camera.translateYMapToScreenPos()[yMapPos];
        g.drawImage(imageItem, xScreenPos, yScreenPos, Panel.UNIT_SIZE, Panel.UNIT_SIZE, null);
    }
}
