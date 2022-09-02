package menu;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

// Class for a Menu that shows controls, which is just a Graphic and not a component
public class ControlMenu implements Menu {

    public boolean open;
    int xPos, yPos;
    int width, height;
    BufferedImage image;

    // Constructor
    public ControlMenu() {
        setDefaultValues();
    }

    @Override
    public void setDefaultValues() {
        // Placement vars
        xPos = 380;
        yPos = 300;
        width = 300;
        height = 320;
        open = false;
    }

    @Override
    public void loadImages() {
        String filePath = "/Users/aaroncorona/eclipse-workspace/GTA/src/assets/images/menus/control_menu.png";
        try {
            image = ImageIO.read(new File(filePath));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void draw(Graphics g) {
        if(open) {
            // Draw the control image
            loadImages();
            g.drawImage(image, xPos, yPos, width, height, null);
        }
    }
}

