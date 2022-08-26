package entity;

import java.awt.*;

public class PlayerCar extends Car {

    public PlayerCar(main.Panel panel, main.KeyHandler key) {
        this.panel = panel;
        this.key = key;

        setDefaultValues();
        loadImages();
    }

    @Override
    public void loadImages() {

    }

    @Override
    public void updateData() {

    }

    @Override
    public void draw(Graphics g) {

    }
}
