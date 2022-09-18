package menu;

import main.Panel;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

// Class for a Menu that shows controls, which is just a Graphic and not a component
public class ControlMenu implements Menu {

    // Position tracking constant
    private static final int X_SCREEN_POS = TitleMenu.X_SCREEN_POS + 100; // align with TitleMenu
    private static final int Y_SCREEN_POS = 300;
    private static final int WIDTH = 300;
    private static final int HEIGHT = 320;

    public static boolean open;
    private static BufferedImage image;

    // Constructor - obj required for drawing
    public ControlMenu() {
        setDefaultValues();
    }

    @Override
    public void setDefaultValues() {
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
            g.drawImage(image, X_SCREEN_POS, Y_SCREEN_POS, WIDTH, HEIGHT, null);
        }
    }
}

