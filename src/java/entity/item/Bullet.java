package entity.item;

import main.Panel;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class Bullet extends SuperItem {

    // Constructor to create a single Money
    public Bullet(int xPos, int yPos, char direction) {
        this.xPos = xPos;
        this.yPos = yPos;
        this.direction = direction;

        setDefaultValues();
    }


    // Default method implementation for setting the reset position
    @Override
    public void setDefaultValues() {
        speed = 20;
        collisionArea = new Rectangle(xPos, yPos + Panel.UNIT_SIZE/4,
                                      Panel.UNIT_SIZE, Panel.UNIT_SIZE/2);
    }

    @Override
    public void update() {
        // Update location
        updateLocation();
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

    @Override
    public void loadImage() {
        String filePath = "/Users/aaroncorona/eclipse-workspace/GTA/src/assets/images/items/bullet/bullet_";
        filePath += direction;
        filePath += ".png";
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
        g.drawRect(collisionArea.x, collisionArea.y, collisionArea.width, collisionArea.height);
//        g.drawRect(xPos, yPos, 5, 5);
    }
}
