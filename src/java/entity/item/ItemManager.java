package entity.item;

import entity.Entity;
import main.CollisionChecker;
import main.Panel;
import tile.TileManager;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

// The Item Manager runs the Entity methods for all objects and provides functions to add more Item objects
public class ItemManager implements Entity {

    // Singleton instance tracking
    private static ItemManager instance = null;

    // Total Item tracking
    public static ArrayList<SuperItem> items;
    public static int moneyValueTotal;
    BufferedImage imageBank;

    // Private Constructor - Singleton class
    private ItemManager() {
        // Delete objects
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
        // Always have 1 heart
        createHeart();
    }

    // Run update on all Items
    @Override
    public void update() {
        // Individual item updates
        for (int i = 0; i < items.size(); i++) {
            items.get(i).update();
        }
        // Overall item management
        deleteDeadItems();
    }

    // Method for other classes to create a Money Item object
    public static void createMoney(int xMapPos, int yMapPos) {
        Money money = new Money(xMapPos, yMapPos);
        items.add(money);
    }

    // Method for other classes to create a Bullet Item object
    public static void createBullet(int xMapPos, int yMapPos, char direction) {
        Bullet bullet1 = new Bullet(xMapPos, yMapPos, direction);
        items.add(bullet1);
    }

    // Method for other classes to create a Heart Item object in a random location
    public static void createHeart() {
        int xMapPos = new Random().nextInt(TileManager.worldMapCols * Panel.UNIT_SIZE);
        int yMapPos = new Random().nextInt(TileManager.worldMapRows * Panel.UNIT_SIZE);
        Heart heart = new Heart(xMapPos, yMapPos);
        // Reset if the Heart spawns on a tile that would cause an instant collision
        if(CollisionChecker.checkTileCollision(heart) == true) {
            heart.dead = true;
            createHeart();
        }
        items.add(heart);
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
    public void loadImages() {
        // Bank icon to display money total
        String filePath = "/Users/aaroncorona/eclipse-workspace/GTA/src/assets/images/menus/bank.png";
        try {
            imageBank = ImageIO.read(new File(filePath));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Method to call the draw method for every Item as well as the bank account
    @Override
    public void draw(Graphics g) {
        if(Panel.playState) {
            // Draw bank
            loadImages();
            g.drawImage(imageBank, 180, 1, main.Panel.UNIT_SIZE, Panel.UNIT_SIZE, null);
            // Draw the current bank account total
            g.setColor(Color.GREEN.darker());
            g.setFont(new Font("Serif", Font.ITALIC, 30));
            g.drawString("$" + ItemManager.moneyValueTotal, 235, 38);
            // Draw each Item object
            for (int i = 0; i < items.size(); i++) {
                items.get(i).draw(g);
            }
        }
    }
}
