package de.jeff_media.replant.nbt;

import de.jeff_media.replant.Main;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class PlayerSettings {

    private static final Main main = Main.getInstance();

    public static final NamespacedKey CROP_REPLANTING = new NamespacedKey(main,"replant_crops");
    public static final NamespacedKey TREE_REPLANTING = new NamespacedKey(main,"replant_trees");

    public static boolean hasEnabled(Player player, NamespacedKey setting) {
        PersistentDataContainer pdc = player.getPersistentDataContainer();
        return pdc.getOrDefault(setting, PersistentDataType.BYTE, (byte) 0) == (byte) 1 ? true : false;
    }

    public static void setEnabled(Player player, NamespacedKey setting, boolean enabled) {
        PersistentDataContainer pdc = player.getPersistentDataContainer();
        pdc.set(setting, PersistentDataType.BYTE, enabled ? (byte) 1 : (byte) 0);
    }
}
