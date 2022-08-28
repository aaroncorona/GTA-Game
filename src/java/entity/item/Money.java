package entity.item;

import main.Panel;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;

public class Money extends SuperItem {

    public int value;
    public boolean collected;

    // Constructor to create a single Money
    public Money(int xPos, int yPos) {
        this.xPos = xPos;
        this.yPos = yPos;

        setDefaultValues();
    }

    // Default method implementation for setting the reset position
    @Override
    public void setDefaultValues() {
        direction = 'R'; // direction does not apply
        speed = 1; // speed does not apply
        collisionArea = new Rectangle(xPos, yPos + Panel.UNIT_SIZE/4,
                Panel.UNIT_SIZE, Panel.UNIT_SIZE/2);
        collected = false;
        value = new Random().nextInt(20) + 1;
    }

    @Override
    public void update() {

    }


    @Override
    public BufferedImage getImage() {
        BufferedImage image = null;
        String filePath = "/Users/aaroncorona/eclipse-workspace/GTA/src/assets/images/items/money.png";
        try {
            image = ImageIO.read(new File(filePath));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return image;
    }

    @Override
    public void draw(Graphics g) {
        g.drawImage(getImage(), xPos, yPos, Panel.UNIT_SIZE, Panel.UNIT_SIZE, null);

        // ad hoc check of the collision area
//        g.setColor(Color.BLACK);
//        g.drawRect(collisionArea.x, collisionArea.y, collisionArea.width, collisionArea.height);
//        g.drawRect(xPos, yPos, 5, 5);
    }
}
