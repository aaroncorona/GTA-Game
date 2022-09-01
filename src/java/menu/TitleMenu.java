package menu;

import main.Panel;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

// Class for a Menu that shows controls, which is just a Graphic and not a component
public class TitleMenu implements Menu {

    public boolean open;
    int xPos, yPos;
    int width, height;
    BufferedImage image;

    // Constructor
    public TitleMenu() {
        setDefaultValues();
    }

    @Override
    public void setDefaultValues() {
        // Placement vars
        xPos = 250;
        yPos = 20;
        width = 550;
        height = 700;
        open = false;
    }

    @Override
    public void loadImage() {
        String filePath = "/Users/aaroncorona/eclipse-workspace/GTA/src/assets/images/menus/start_menu.png";
        try {
            image = ImageIO.read(new File(filePath));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void draw(Graphics g) {
        if(open) {
            // Draw the title screen
            loadImage();
            g.drawImage(image, xPos, yPos, width, height, null);
            // Draw text
            g.setColor(Color.YELLOW);
            g.setFont(new Font("Serif", Font.ITALIC, 50));
            g.drawString("Press ENTER to Play",xPos + 50,550);
        }
    }
}

