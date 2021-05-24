package de.jeff_media.replant.listeners;

import de.jeff_media.replant.Main;
import de.jeff_media.replant.config.Config;
import de.jeff_media.replant.config.Permissions;
import de.jeff_media.replant.handlers.SeedManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.data.Ageable;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockDropItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Iterator;
import java.util.List;

import static de.jeff_media.replant.config.Permissions.isAllowed;

public class CropListener implements Listener {

    private final Main main = Main.getInstance();

    public CropListener() {
        Bukkit.getServer().getPluginManager().registerEvents(this, main);
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onCropHarvest(BlockDropItemEvent event) {

        Player player = event.getPlayer();
        if(!isAllowed(player, Permissions.PERMISSION_USE)) return;
        if(!main.getPlayerManager().hasCropsEnabled(player)) return;
        Block block = event.getBlock();
        BlockState blockState = event.getBlockState();
        BlockData blockData = blockState.getBlockData();
        if(!(blockData instanceof Ageable)) return;

        Ageable ageable = (Ageable) blockData;
        if(ageable.getAge() < ageable.getMaximumAge()) return;

        Material crop = blockState.getType();
        Material seed = SeedManager.getSeedFromCrop(crop);

        if(main.getConfig().getBoolean(Config.CROP_REPLANT_COSTS) && !removeSeed(seed, event.getItems(),player)) {
            return;
        }

        new BukkitRunnable() {
            @Override
            public void run() {
                if(block.getType().isAir()) {
                    block.setType(crop);

                    main.getCropParticleManager().spawnParticles(block);

                } else {
                    World world = block.getWorld();
                    Location location = block.getLocation();
                    ItemStack itemStack = new ItemStack(seed,1);
                    world.dropItemNaturally(location, itemStack);
                }
            }
        }.runTaskLater(main, Config.getCropReplantDelayInTicks());

    }

    private static boolean removeSeed(Material seed, List<Item> drops, Player player) {

        // Try to remove seed from drop
        Iterator<Item> iterator = drops.iterator();
        while(iterator.hasNext()) {
            Item item = iterator.next();
            ItemStack itemStack = item.getItemStack();
            if(itemStack.getType() != seed) continue;
            if(itemStack.getAmount()>1) {
                itemStack.setAmount(itemStack.getAmount() - 1);
            } else {
                iterator.remove();
            }
            return true;
        }

        // Maybe player has a seed in their inventory
        PlayerInventory inventory = player.getInventory();
        if(inventory.contains(seed)) {
            inventory.remove(new ItemStack(seed,1));
        }

        return false;
    }

}
