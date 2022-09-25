package entity.item;

import entity.car.SuperEntity;

import java.awt.image.BufferedImage;

// This super class provides a foundation for creating item classes
public abstract class SuperItem extends SuperEntity {
    // Item settings
    public static int itemType;
    BufferedImage imageItem;
}
