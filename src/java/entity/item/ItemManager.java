package entity.item;

import entity.Entity;

import java.awt.*;
import java.util.ArrayList;

// The Item Manager runs the Item methods for all objects and gives methods to add or delete items
public class ItemManager implements Entity {

    // Singleton instance tracking
    private static ItemManager instance = null;

    // Total Item tracking
    public static ArrayList<SuperItem> items = new ArrayList<>();
    public static int moneyValueTotal = 0;

    // Private Constructor - Singleton class
    private ItemManager() {}

    // Singleton constructor method to ensure there is only 1 Tile manager obj per game
    public static ItemManager getInstance() {
        if(instance == null) {
            instance = new ItemManager();
            return instance;
        } else {
            return instance;
        }
    }

    // Delete all item objects
    @Override
    public void setDefaultValues() {
        items = new ArrayList<>();
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
        // Not currently used, each item obj loads their own image
    }

    // Method to call the draw method for every Item
    @Override
    public void draw(Graphics g) {
        for (int i = 0; i < items.size(); i++) {
            items.get(i).draw(g);
        }
    }
}