package menu;

import main.Panel;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

// Class for a Title Menu that displays an image and provides starting instructions
public class TitleMenu implements Menu {

    public boolean open;
    protected static final int X_SCREEN_POS = Panel.SCREEN_WIDTH/3 - 50; // manually center
    protected static final int Y_SCREEN_POS = 20;
    private static final int WIDTH = 550;
    private static final int HEIGHT = 700;
    private BufferedImage image;

    // Constructor
    public TitleMenu() {
        setDefaultValues();
    }

    @Override
    public void setDefaultValues() {
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
            g.drawImage(image, X_SCREEN_POS, Y_SCREEN_POS, WIDTH, HEIGHT, null);
            // Draw rect background for text
            int textBoxY = HEIGHT - 160;
            g.setColor(Color.LIGHT_GRAY);
            g.fillRect(X_SCREEN_POS + 40, textBoxY, WIDTH - 70, 130);
            // Draw text
            g.setColor(Color.YELLOW);
            g.setFont(new Font("Serif", Font.ITALIC, 50));
            g.drawString("Press ENTER to Begin",X_SCREEN_POS + 50,textBoxY + 50);
            g.drawString(" Press C for Controls",X_SCREEN_POS + 50,textBoxY + 110);
        }
    }
}

