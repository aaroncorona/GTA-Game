package entity;

import tile.Tile;
import tile.TileManager;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;

public class CopCar extends Car {

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

        collisionArea = new Rectangle(xPos, yPos + panel.UNIT_SIZE/4,
                                      panel.UNIT_SIZE, panel.UNIT_SIZE/2);

        // Reset if the Cop spawn on a tile that would cause an instant collision
        if(TileManager.tiles[TileManager.tileMap[yPos / panel.UNIT_SIZE][xPos / panel.UNIT_SIZE]].collision == true) {
            setDefaultValues();
        }
    }

    @Override
    public void update() {
        // Not currently used, the cop doesn't move or take key inputs
    }

    @Override
    public BufferedImage getImage() {
        BufferedImage image = null;
        String filePath = "/Users/aaroncorona/eclipse-workspace/GTA/src/assets/images/entities/cop_car/cop_car_";
        // Determine the image direction and nitro status
        filePath += direction;
        // Determine if it should be a nitro image
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
    }
}
