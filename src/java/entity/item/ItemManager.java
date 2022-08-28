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

    // Run reset on all Items
    @Override
    public void setDefaultValues() {
        for (int i = 0; i < items.size(); i++) {
            items.get(i).setDefaultValues();
        }
    }

    // Run update on all Items
    @Override
    public void update() {
        for (int i = 0; i < items.size(); i++) {
            items.get(i).update();
        }
    }

    // Method to create a Money Item object as needed
    public static void createMoney(int xPos, int yPos) {
        Money money = new Money(xPos,yPos);
        items.add(money);
    }

    // Method to increment the score and delete the Money Item
    public static void collectMoney(Money money) {
        // Add to money score
        moneyValueTotal += money.value;

        // Remove that money object from the array so it disappears from the screen
        for (int i = 0; i < items.size(); i++) {
            if(items.get(i).equals(money)) {
                items.remove(i);
            }
        }
    }

    // Method to create a Bullet Item object as needed
    public static void createBullet(int xPos, int yPos, char direction) {
        Bullet bullet1 = new Bullet(xPos, yPos, direction);
        items.add(bullet1);
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
