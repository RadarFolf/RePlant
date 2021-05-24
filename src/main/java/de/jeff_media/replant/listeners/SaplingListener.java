package de.jeff_media.replant.listeners;

import de.jeff_media.replant.Main;
import de.jeff_media.replant.config.Config;
import de.jeff_media.replant.utils.SaplingUtils;
import org.bukkit.Bukkit;
import org.bukkit.Tag;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockDropItemEvent;
import org.bukkit.event.entity.ItemSpawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import javax.annotation.Nullable;
import java.util.UUID;

import static de.jeff_media.replant.utils.SaplingUtils.isSapling;

public class SaplingListener implements Listener {

    private final Main main = Main.getInstance();

    public SaplingListener() {
        Bukkit.getPluginManager().registerEvents(this, main);
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onSaplingSpawn(ItemSpawnEvent event) {

        Item item = event.getEntity();

        if(!isSapling(item)) return;

        @Nullable UUID throwerUUID = item.getThrower();
        @Nullable Entity thrower = throwerUUID == null ? null : Bukkit.getEntity(throwerUUID);
        if(thrower instanceof Player && !main.getConfig().getBoolean(Config.SAPLING_REPLANT_THROWN_BY_PLAYER)) return;

        main.getSaplingManager().register(item);
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onSaplingSpawn(BlockDropItemEvent event) {
        for(Item item : event.getItems()) {
            if(!isSapling(item)) continue;

            // Do not replant saplings that have just been removed from the world
            if(isSapling(event.getBlockState().getType())) continue;

            main.getSaplingManager().register(item);
        }
    }


}
