package menu;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

// Class for a Title Menu that displays an image and provides starting instructions
public class TitleMenu implements Menu {

    public boolean open;
    int xScreenPos, yScreenPos;
    int width, height;
    BufferedImage image;

    // Constructor
    public TitleMenu() {
        setDefaultValues();
    }

    @Override
    public void setDefaultValues() {
        // Placement vars
        xScreenPos = 250;
        yScreenPos = 20;
        width = 550;
        height = 700;
        open = false;
    }

    @Override
    public void loadImages() {
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
            // Draw the title screen image
            loadImages();
            g.drawImage(image, xScreenPos, yScreenPos, width, height, null);
            // Draw rect background for text
            int textBoxY = height-160;
            g.setColor(Color.LIGHT_GRAY);
            g.fillRect(xScreenPos + 40, textBoxY, width - 70, 130);
            // Draw text
            g.setColor(Color.YELLOW);
            g.setFont(new Font("Serif", Font.ITALIC, 50));
            g.drawString("Press ENTER to Begin",xScreenPos + 50,textBoxY + 50);
            g.drawString(" Press C for Controls",xScreenPos + 50,textBoxY + 110);
        }
    }
}

