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

    // Constructor to create Cop NPC
    public Money(main.Panel panel) {
        this.panel = panel;
        setDefaultValues();
    }

    // Default method implementation for setting the reset position
    @Override
    public void setDefaultValues() {
        xPos = 400;
        yPos = 400;
        direction = 'R';
        speed = 5;
        collisionArea = new Rectangle(xPos, yPos + Panel.UNIT_SIZE/4,
                Panel.UNIT_SIZE, Panel.UNIT_SIZE/2);
        collected = false;
        value = new Random().nextInt(20) + 1;
    }

    @Override
    public void update() {
        // Not currently used - money does not move
    }


    @Override
    public BufferedImage getImage() {
        BufferedImage image = null;
        String filePath = "/Users/aaroncorona/eclipse-workspace/GTA/src/assets/images/items/money.png";
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
