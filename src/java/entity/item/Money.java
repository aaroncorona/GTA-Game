package entity.item;

import main.Panel;
import main.Sound;
import tile.Camera;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.Random;

import static main.CollisionChecker.checkEntityCollision;

public class Money extends SuperItem {

    public int value;

    // Protected Constructor to create a single Money item. Only the TileManager should use this method
    protected Money(int xPos, int yPos) {
        this.xMapPos = xPos;
        this.yMapPos = yPos;

        setDefaultValues();
    }

    // Default method implementation for setting the reset position
    @Override
    public void setDefaultValues() {
        itemType = 1;
        dead = false;
        direction = 'R'; // direction does not apply
        speed = 0; // speed does not apply
        collisionArea = new Rectangle(xMapPos, yMapPos, Panel.UNIT_SIZE, Panel.UNIT_SIZE);
        value = new Random().nextInt(20) + 10;
    }

    @Override
    public void update() {
        // Handle events
        handleCollection();
    }

    // Helper method to check if the given money object is contacted by the player, whereby
    // a "collection" should occur and increment the overall money score
    private void handleCollection() {
        if(checkEntityCollision(Panel.playerCar, this) == true) {
            ItemManager.moneyValueTotal += value;
            dead = true;
            Sound.playCoins();
        }
    }

    @Override
    public void loadImages() {
        String filePath = "/Users/aaroncorona/eclipse-workspace/GTA/src/assets/images/items/money.png";
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
