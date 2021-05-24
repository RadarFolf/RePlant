package de.jeff_media.replant.nbt;

import de.jeff_media.replant.Main;
import de.jeff_media.replant.config.Messages;
import org.bukkit.entity.Player;

import static de.jeff_media.replant.config.Messages.sendMessage;

public class PlayerManager {

    private final Main main = Main.getInstance();

    public boolean hasTreesEnabled(Player player) {
        return PlayerSettings.hasEnabled(player, PlayerSettings.TREE_REPLANTING);
    }

    public boolean hasCropsEnabled(Player player) {
        return PlayerSettings.hasEnabled(player,PlayerSettings.CROP_REPLANTING);
    }

    public boolean toggleCrops(Player player) {
        boolean enabled = !hasCropsEnabled(player);

        PlayerSettings.setEnabled(player, PlayerSettings.CROP_REPLANTING, enabled);

        if(enabled) {
            sendMessage(player, Messages.REPLANT_CROPS_ENABLED);
        } else {
            sendMessage(player, Messages.REPLANT_CROPS_DISABLED);
        }

        return enabled;
    }
}
