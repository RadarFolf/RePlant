package de.jeff_media.replant.handlers;

import org.bukkit.Material;

public class SeedManager {

    public static Material getSeedFromCrop(Material crop) {
        switch(crop) {
            case WHEAT:
                return Material.WHEAT_SEEDS;
            case BEETROOTS:
                return Material.BEETROOT_SEEDS;
            case CARROTS:
                return Material.CARROT;
            case POTATOES:
                return Material.POTATO;
            default:
                return crop;
        }
    }
}
