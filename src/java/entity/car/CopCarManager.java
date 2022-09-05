package entity.car;

import entity.Entity;
import entity.item.ItemManager;
import main.Panel;
import main.Sound;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

// The Item Manager runs the necessary methods for all Cop objects and adds cops based on a Wanted level
public class CopCarManager implements Entity {

    // Singleton instance tracking
    private static CopCarManager instance = null;

    // Total Cop tracking
    public static ArrayList<CopCar> cops;
    public static int wantedLevel;
    private BufferedImage imageBadge;
    private BufferedImage imageStars;

    // Private Constructor - Singleton class
    private CopCarManager() {
        setDefaultValues();
    }

    // Singleton constructor method to ensure there is only 1 Tile manager obj per game
    public static CopCarManager getInstance() {
        if(instance == null) {
            instance = new CopCarManager();
            return instance;
        } else {
            return instance;
        }
    }

    // Delete all cops from being tracked except 1 and set Wanted to 0
    @Override
    public void setDefaultValues() {
        cops = new ArrayList<>();
        cops.add(new CopCar());
        wantedLevel = 0;
    }

    // Run update on all Items
    @Override
    public void update() {
        // Individual item updates
        for (int i = 0; i < cops.size(); i++) {
            cops.get(i).update();
        }
        // Overall item management
        deleteDeadCops();
        handleWantedLevel();
    }

    // Helper Method to update the Wanted level and cop creation
    private static void handleWantedLevel() {
        // Set the Wanted level based on money (which implies the num of cops killed)
        if(ItemManager.moneyValueTotal > 200) {
            wantedLevel = 5;
        } else if (ItemManager.moneyValueTotal > 150) {
            wantedLevel = 4;
        } else if (ItemManager.moneyValueTotal > 100) {
            wantedLevel = 3;
        } else if (ItemManager.moneyValueTotal > 50) {
            wantedLevel = 2;
        } else if (ItemManager.moneyValueTotal > 0) {
            wantedLevel = 1;
        }

        // Set the num of cops based on the Wanted level
        if(cops.size()-1 < wantedLevel) {
            createCop();
            Sound.playSiren();
        }
    }

    // Method to create a Cop obj
    public static void createCop() {
        CopCar cop = new CopCar();
        cops.add(cop);
    }

    // Helper Method to remove unused ("dead") items from the list to improve item manager performance
    private static void deleteDeadCops() {
        for (int i = 0; i < cops.size(); i++) {
            if(cops.get(i).health == 0) {
                cops.remove(i);
            }
        }
    }

    @Override
    public void loadImages() {
        // Get the badge image
        String filePath = "/Users/aaroncorona/eclipse-workspace/GTA/src/assets/images/entities/cop_car/badge.png";
        try {
            imageBadge = ImageIO.read(new File(filePath));
        } catch (IOException e) {
            e.printStackTrace();
        }
        // Get the wanted stars image based on the wanted level
        filePath = "/Users/aaroncorona/eclipse-workspace/GTA/src/assets/images/entities/cop_car/wanted_stars_";
        filePath += wantedLevel;
        filePath += ".png";
        try {
            imageStars = ImageIO.read(new File(filePath));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Method to call the draw method for every Item as well as the Wanted level
    @Override
    public void draw(Graphics g) {
        if(Panel.titleState == false) {
            // Draw all Cop objects
            for (int i = 0; i < cops.size(); i++) {
                cops.get(i).draw(g);
            }
            // Draw the Badge and Wanted level at the top next to the Bank account
            loadImages();
            g.drawImage(imageBadge, 630, 1, Panel.UNIT_SIZE, Panel.UNIT_SIZE-2, null);
            g.drawImage(imageStars, 675, 2, Panel.UNIT_SIZE*4+10, Panel.UNIT_SIZE-2, null);
        }
    }
}
