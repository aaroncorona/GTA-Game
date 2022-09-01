package entity.item;

import entity.Entity;
import main.Panel;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

// The Item Manager runs the Entity methods for all objects and provides functions to add more Item objects
public class ItemManager implements Entity {

    // Singleton instance tracking
    private static ItemManager instance = null;

    // Total Item tracking
    public static ArrayList<SuperItem> items;
    public static int moneyValueTotal;
    BufferedImage image;

    // Private Constructor - Singleton class
    private ItemManager() {
        setDefaultValues();
    }

    // Singleton constructor method to ensure there is only 1 Tile manager obj per game
    public static ItemManager getInstance() {
        if(instance == null) {
            instance = new ItemManager();
            return instance;
        } else {
            return instance;
        }
    }

    // Delete all items from being tracked
    @Override
    public void setDefaultValues() {
        items = new ArrayList<>();
        moneyValueTotal = 0;
    }

    // Run update on all Items
    @Override
    public void update() {
        // Overall item management
        deleteDeadItems();
        // Individual item updates
        for (int i = 0; i < items.size(); i++) {
            items.get(i).update();
        }
    }

    // Method for other classes to create a Money Item object
    public static void createMoney(int xPos, int yPos) {
        Money money = new Money(xPos,yPos);
        items.add(money);
    }

    // Method for other classes to create a Bullet Item object
    public static void createBullet(int xPos, int yPos, char direction) {
        Bullet bullet1 = new Bullet(xPos, yPos, direction);
        items.add(bullet1);
    }

    // Helper Method to remove unused ("dead") items from the list to improve item manager performance
    private static void deleteDeadItems() {
        for (int i = 0; i < items.size(); i++) {
            if(items.get(i).dead == true) {
                items.remove(i);
            }
        }
    }

    @Override
    public void loadImage() {
        // Bank icon to display money total
        String filePath = "/Users/aaroncorona/eclipse-workspace/GTA/src/assets/images/menus/bank.png";
        try {
            image = ImageIO.read(new File(filePath));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Method to call the draw method for every Item
    @Override
    public void draw(Graphics g) {
        for (int i = 0; i < items.size(); i++) {
            items.get(i).draw(g);
        }
        // Draw the money total while the game is running
        if(Panel.titleState == false) {
            // Draw bank
            loadImage();
            g.drawImage(image, 50, 1, main.Panel.UNIT_SIZE, Panel.UNIT_SIZE, null);
            // Draw the current score
            g.setColor(Color.GREEN.darker());
            g.setFont(new Font("Serif", Font.ITALIC, 30));
            g.drawString("$" + ItemManager.moneyValueTotal,108,38);
        }
    }
}
