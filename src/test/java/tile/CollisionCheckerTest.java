package tile;

import entity.physics.CollisionChecker;
import entity.car.PlayerCar;
import entity.item.ItemManager;
import entity.item.SuperItem;
import main.Panel;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CollisionCheckerTest {

    // Check that 2 cars colliding returns true from the Collision Checker
    @Test
    void whenCarCollision_ReturnTrue() {
        new Panel();
        // Car 1
        PlayerCar car1 = new PlayerCar();
        car1.xMapPos = 900;
        car1.yMapPos = 950;
        // Car 2
        PlayerCar car2 = new PlayerCar();
        car2.xMapPos = 910; // within collision area
        car2.yMapPos = 960;
        // Assert
        boolean collisionResultExpected = true;
        boolean collisionResultActual = CollisionChecker.checkEntityCollision(car1, car2);
        assertEquals(collisionResultExpected, collisionResultActual);
    }

    // Check that a bullet on a bullet returns true from the Collision Checker
    @Test
    void whenItemCollision_ReturnTrue() {
        new Panel();
        // Bullet 1
        ItemManager.createBullet(900, 950, 'R');
        SuperItem item1 = ItemManager.items.get(ItemManager.items.size()-1);
        // Bullet 2
        ItemManager.createBullet(901, 950, 'L');
        SuperItem item2 = ItemManager.items.get(ItemManager.items.size()-1);
        // Assert
        boolean collisionResultExpected = true;
        boolean collisionResultActual = CollisionChecker.checkItemCollision(item1, item2);
        assertEquals(collisionResultExpected, collisionResultActual);
    }

    // Check that a car on a tree returns true from the Collision Checker
    @Test
    void whenCarOnTree_ReturnTrue() {
        new Panel();
        // Car
        PlayerCar car = new PlayerCar();
        car.xMapPos = 900;
        car.yMapPos = 900;
        car.update();
        // Assert
        boolean collisionResultExpected = true;
        boolean collisionResultActual = CollisionChecker.checkTileCollision(car);
        assertEquals(collisionResultExpected, collisionResultActual);
    }

    // Check that a bullet on a hut returns true from the Collision Checker
    @Test
    void whenItemOnWater_ReturnTrue() {
        new Panel();
        // Bullet
        ItemManager.createBullet(100, 100, 'R');
        SuperItem item = ItemManager.items.get(ItemManager.items.size()-1);
        // Assert
        boolean collisionResultExpected = true;
        boolean collisionResultActual = CollisionChecker.checkTileCollision(item);
        assertEquals(collisionResultExpected, collisionResultActual);
    }
}