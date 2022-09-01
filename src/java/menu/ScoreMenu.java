package menu;

import entity.item.ItemManager;
import main.Panel;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

// Class for a Menu that shows a Running score
public class ScoreMenu implements Menu {

    public boolean open;
    int xPos, yPos;
    BufferedImage image;

    // Constructor
    public ScoreMenu() {
        setDefaultValues();
    }

    @Override
    public void setDefaultValues() {
        // Placement vars
        xPos = 50;
        yPos = 1;
        // Always display the score
        open = true;
    }

    @Override
    public void loadImage() {
        String filePath = "/Users/aaroncorona/eclipse-workspace/GTA/src/assets/images/menus/bank.png";
        try {
            image = ImageIO.read(new File(filePath));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void draw(Graphics g) {
        if(open) {
            // Draw bank
            loadImage();
            g.drawImage(image, xPos, yPos, main.Panel.UNIT_SIZE, Panel.UNIT_SIZE, null);
            // Draw the current score
            g.setColor(Color.GREEN.darker());
            g.setFont(new Font("Serif", Font.ITALIC, 30));
            g.drawString("$" + ItemManager.moneyValueTotal,xPos + 58,yPos + 37);
        }
    }
}

