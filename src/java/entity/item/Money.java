package entity.item;

import main.Panel;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.Random;

import static main.CollisionChecker.checkEntityCollision;

public class Money extends SuperItem {

    public int value;

    // Constructor to create a single Money item. Only the TileManager should use this method
    protected Money(int xPos, int yPos) {
        this.xPos = xPos;
        this.yPos = yPos;

        setDefaultValues();
    }

    // Default method implementation for setting the reset position
    @Override
    public void setDefaultValues() {
        direction = 'R'; // direction does not apply
        speed = 1; // speed does not apply
        dead = false;
        collisionArea = new Rectangle(xPos, yPos, Panel.UNIT_SIZE, Panel.UNIT_SIZE);
        value = new Random().nextInt(20) + 1;
    }

    @Override
    public void update() {
        // Handle events
        handleMoneyCollection();
    }

    // Helper method to check if the money object is contacted by the player, whereby
    // a "collection" should occur and increment the overal money score
    private void handleMoneyCollection() {
        if(checkEntityCollision(Panel.playerCar, this) == true) {
            ItemManager.moneyValueTotal += value;
            dead = true;
            System.out.println("Money collected! New bank account balance: $" + ItemManager.moneyValueTotal);
        }
    }

    @Override
    public void loadImage() {
        String filePath = "/Users/aaroncorona/eclipse-workspace/GTA/src/assets/images/items/money.png";
        try {
            image = ImageIO.read(new File(filePath));
        } catch (IOException e) {
            e.printStackTrace();
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
