package de.jeff_media.replant.handlers;

import de.jeff_media.replant.Main;
import de.jeff_media.replant.config.Config;
import de.jeff_media.replant.utils.SaplingUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.UUID;

public class SaplingManager {

    private final Main main = Main.getInstance();
    private final ArrayList<UUID> saplings = new ArrayList<>();

    public SaplingManager() {
        new BukkitRunnable() {
            @Override
            public void run() {
                if(!main.getConfig().getBoolean(Config.SAPLING_REPLANT)) return;
                int delay = Config.getSaplingReplantDelayInTicks();
                for(Item item : getAll()) {
                    if(item.getTicksLived() >= delay) {
                        Main.debug("Item " + item + " is old enough to be planted.");
                        replantSapling(item);
                    }
                }
            }
        }.runTaskTimer(main, 20, 20);
    }

    private void replantSapling(Item item) {
        /*if(item.getTicksLived() < Config.getSaplingReplantDelayInTicks()) {
            Main.debug(item + " is not old enough yet, waiting...");
            return;
        }*/
        if(!item.isOnGround()) {
            Main.debug(item + " is not on the ground yet, waiting...");
            return;
        }

        Block block = item.getLocation().getBlock();

        // Soulsand is too small, fckn son of a bitch
        if(block.getType() == Material.SOUL_SAND) {
            block = block.getRelative(BlockFace.UP);
        }
        if(!block.getType().isAir()) {
            Block occupiedBlock = block;
            //System.out.println("\n\n\n\n\n\n\nOriginal Block: " + block);
            if(main.getConfig().getBoolean(Config.SAPLING_REPLANT_SEARCH_NEARBY)) {
                outerloop:
                for (int x = -1; x <= 1; x++) {
                    for (int y = -1; y <= 1; y++) {
                        for (int z = -1; z <= 1; z++) {
                            //System.out.println("\nChecking " + block);
                            if (block.getRelative(x, y, z).getType().isAir()) {
                               // System.out.println("-> YES");
                                block = block.getRelative(x, y, z);
                                break outerloop;
                            }
                        }
                    }
                }
            }
            if(occupiedBlock == block) {
                Main.debug("Could not find a free spot ");
                unregister(item);
                return;
            }
        }
        if(!SaplingUtils.getValidGroundTypes(item.getItemStack().getType()).contains(block.getRelative(BlockFace.DOWN).getType())) {
            unregister(item);
            return;
        }

        if(main.getConfig().getBoolean(Config.CALL_BLOCK_PLACE_EVENT)) {
            UUID thrower = item.getThrower();
            if (thrower == null) return;
            if (!(Bukkit.getEntity(thrower) instanceof Player)) return;
            Player throwingPlayer = Bukkit.getPlayer(thrower);
            BlockPlaceEvent blockPlaceEvent = new BlockPlaceEvent(block, block.getState(), block.getRelative(BlockFace.DOWN), item.getItemStack(), throwingPlayer, true, EquipmentSlot.HAND);
            Bukkit.getPluginManager().callEvent(blockPlaceEvent);
            if(blockPlaceEvent.isCancelled()) {
                main.debug("BlockPlaceEvent cancelled.");
                return;
            }
        }
        remove(item);
        main.getSaplingParticleManager().spawnParticles(block);
        block.setType(item.getItemStack().getType());
    }

    public void unregister(Item item) {
        saplings.remove(item.getUniqueId());
    }

    public void register(Item item) {
        Main.debug("Registered sapling: " + item.getItemStack());
        saplings.add(item.getUniqueId());
    }

    public void remove(Item item) {
        ItemStack itemStack = item.getItemStack();
        int amount = itemStack.getAmount();
        if(amount == 1) {
            unregister(item);
            item.remove();
        } else {
            itemStack.setAmount(amount - 1);
            item.setItemStack(itemStack);
        }
    }

    public boolean isRegistered(Item item) {
        return saplings.contains(item.getUniqueId());
    }

    public ArrayList<Item> getAll() {
        ArrayList<Item> all = new ArrayList<>();
        Iterator<UUID> iterator = saplings.iterator();
        while(iterator.hasNext()) {
            UUID uuid = iterator.next();
            Entity entity = Bukkit.getEntity(uuid);
            if(entity == null || entity.isDead() || entity.getType() != EntityType.DROPPED_ITEM) {
                iterator.remove();
                continue;
            }
            all.add((Item) entity);
        }
        return all;
    }

}
