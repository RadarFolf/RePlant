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
                if (!main.getConfig().getBoolean(Config.SAPLING_REPLANT)) return;
                int delay = Config.getSaplingReplantDelayInTicks();
                for (Item item : getAll()) {
                    if (item.getTicksLived() >= delay) {
                        //Main.debug("Item " + item.getItemStack() + " is old enough to be planted.");
                        replantSapling(item);
                    }
                }
            }
        }.runTaskTimer(main, 20, 20);
    }

    public ArrayList<Item> getAll() {
        ArrayList<Item> all = new ArrayList<>();
        Iterator<UUID> iterator = saplings.iterator();
        while (iterator.hasNext()) {
            UUID uuid = iterator.next();
            Entity entity = Bukkit.getEntity(uuid);
            if (entity == null || entity.isDead() || entity.getType() != EntityType.DROPPED_ITEM) {
                iterator.remove();
                continue;
            }
            all.add((Item) entity);
        }
        return all;
    }

    public boolean isRegistered(Item item) {
        return saplings.contains(item.getUniqueId());
    }

    public void register(Item item) {
        //Main.debug("Registered sapling: " + item.getItemStack());
        saplings.add(item.getUniqueId());
    }

    public void remove(Item item) {
        ItemStack itemStack = item.getItemStack();
        int amount = itemStack.getAmount();
        if (amount == 1) {
            unregister(item);
            item.remove();
        } else {
            itemStack.setAmount(amount - 1);
            item.setItemStack(itemStack);
        }
    }

    private void replantSapling(Item item) {
        /*if(item.getTicksLived() < Config.getSaplingReplantDelayInTicks()) {
            Main.debug(item + " is not old enough yet, waiting...");
            return;
        }*/
        if (!item.isOnGround()) {
            //Main.debug(item + " is not on the ground yet, waiting...");
            return;
        }

        Block saplingBlock = item.getLocation().getBlock();

        // Soulsand is too small, fckn son of a bitch
        if (saplingBlock.getType() == Material.SOUL_SAND) {
            saplingBlock = saplingBlock.getRelative(BlockFace.UP);
        }

        saplingBlock = SaplingUtils.findValidSpot(saplingBlock, item);

        if(saplingBlock == null) {
            Main.debug("Could not find valid spot");
            unregister(item);
            return;
        }

        UUID thrower = item.getThrower();
        Player throwingPlayer = null;
        if (thrower != null) {
            if (Bukkit.getEntity(thrower) instanceof Player) {
                throwingPlayer = Bukkit.getPlayer(thrower);
            }
        }

        if (main.getConfig().getBoolean(Config.USE_WORLDGUARD) && throwingPlayer != null) {
            if (!main.getWorldGuardHandler().canBuild(throwingPlayer, saplingBlock)) {
                main.debug("Sapling inside protected WorldGuard region.");
                unregister(item);
                return;
            }
        }

        if (main.getConfig().getBoolean(Config.CALL_BLOCK_PLACE_EVENT) && throwingPlayer != null) {
            BlockPlaceEvent blockPlaceEvent = new BlockPlaceEvent(saplingBlock, saplingBlock.getState(), saplingBlock.getRelative(BlockFace.DOWN), item.getItemStack(), throwingPlayer, true, EquipmentSlot.HAND);
            Bukkit.getPluginManager().callEvent(blockPlaceEvent);
            if (blockPlaceEvent.isCancelled()) {
                main.debug("BlockPlaceEvent cancelled.");
                unregister(item);
                return;
            }
        }

        remove(item);
        main.getSaplingParticleManager().spawnParticles(saplingBlock);
        saplingBlock.setType(item.getItemStack().getType());
    }

    public void unregister(Item item) {
        saplings.remove(item.getUniqueId());
    }

}
