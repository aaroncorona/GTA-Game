package entity;

import javax.imageio.ImageIO;
import java.awt.*;
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
        nitro = false;

//        // Reset if the Cop spawns off the road
//        if(panel.backgroundGrid[xPos][yPos] != 2) {
//            setDefaultValues();
//        }
    }

    @Override
    public void update() {
    }

    @Override
    public void getImage() {
        String filePath = "/Users/aaroncorona/eclipse-workspace/GTA/src/assets/images/cop/cop_car.png";
        try {
            image = ImageIO.read(new File(filePath));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void draw(Graphics g) {
        getImage();
        g.drawImage(image, xPos, yPos, panel.UNIT_SIZE, panel.UNIT_SIZE, null);
//        g.drawOval(xPos, yPos, 10, 10);
    }
}
