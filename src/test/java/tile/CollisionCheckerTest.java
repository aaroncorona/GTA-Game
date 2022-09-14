package tile;

import entity.car.PlayerCar;
import main.Panel;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CollisionCheckerTest {

    // Check that 2 cars colliding correctly returns true from the Collision Checker
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
        boolean collisionResultExpected = true;
        boolean collisionResultActual = CollisionChecker.checkEntityCollision(car1, car2);
        assertEquals(collisionResultExpected, collisionResultActual);
    }
}