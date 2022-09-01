package menu;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

// Class for a Title Menu that displays an image and provides starting instructions
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
            // Draw the title screen image
            loadImage();
            g.drawImage(image, xPos, yPos, width, height, null);
            // Draw rect background for text
            int textBoxY = height-160;
            g.setColor(Color.LIGHT_GRAY);
            g.fillRect(xPos + 40, textBoxY, width - 70, 130);
            // Draw text
            g.setColor(Color.YELLOW);
            g.setFont(new Font("Serif", Font.ITALIC, 50));
            g.drawString("Press ENTER to Begin",xPos + 50,textBoxY + 50);
            g.drawString(" Press C for Controls",xPos + 50,textBoxY + 110);
        }
    }
}

