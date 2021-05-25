package de.jeff_media.replant.utils;

import de.jeff_media.replant.Main;
import de.jeff_media.replant.config.Config;
import org.bukkit.Material;
import org.bukkit.Tag;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class SaplingUtils {

    private static final HashSet<Material> saplings = new HashSet<>(Arrays.asList(
            Material.ACACIA_SAPLING,
            Material.BIRCH_SAPLING,
            Material.DARK_OAK_SAPLING,
            Material.JUNGLE_SAPLING,
            Material.OAK_SAPLING,
            Material.SPRUCE_SAPLING,
            Material.CRIMSON_FUNGUS,
            Material.WARPED_FUNGUS,
            Material.NETHER_WART
    ));

    private static final HashSet<Material> overworldGroundTypes = new HashSet<>(Arrays.asList(
            Material.DIRT,
            Material.COARSE_DIRT,
            Material.PODZOL,
            Material.GRASS_BLOCK,
            Material.FARMLAND));

    private static final HashSet<Material> crimsonGroundTypes = new HashSet<>(Collections.singleton(Material.CRIMSON_NYLIUM));

    private static final HashSet<Material> warpedGroundTypes = new HashSet<>(Collections.singleton(Material.WARPED_NYLIUM));

    private static final HashSet<Material> netherwartGroundTypes = new HashSet<>(Collections.singleton(Material.SOUL_SAND));

    public static @Nullable Block findValidSpot(Block saplingBlock, Item item) {
        Block below = saplingBlock.getRelative(BlockFace.DOWN);
        if(saplingBlock.getType().isAir() && isValidGround(below, item)) return saplingBlock;

        if(!Main.getInstance().getConfig().getBoolean(Config.SAPLING_REPLANT_SEARCH_NEARBY)) return null;

        //Main.debug("Block " + saplingBlock + " is not appropriate, looking nearby...");

        for(int x = -1; x <= 1; x++) {
            for (int y = -1; y <= 1; y++) {
                for(int z = -1; z <= 1; z++) {
                    Block candidate = saplingBlock.getRelative(x,y,z);
                    //Main.debug("Checking " + candidate);
                    below = candidate.getRelative(BlockFace.DOWN);
                    if(candidate.getType().isAir() && isValidGround(below, item)) {
                        return candidate;
                    }
                }
            }
        }
        return null;
    }

    public static boolean isSapling(Item item) {
        return isSapling(item.getItemStack().getType());
    }

    public static boolean isSapling(Material material) {
        if(saplings.contains(material)) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean isValidGround(Block block, Item sapling) {
        Material type = sapling.getItemStack().getType();
        return getValidGroundTypes(type).contains(block.getType());
    }

    public static Set<Material> getValidGroundTypes(Material sapling) {
        Set<Material> groundTypes = new HashSet<>();

        switch(sapling) {
            case ACACIA_SAPLING:
            case BIRCH_SAPLING:
            case DARK_OAK_SAPLING:
            case JUNGLE_SAPLING:
            case OAK_SAPLING:
            case SPRUCE_SAPLING:
                return overworldGroundTypes;
            case CRIMSON_FUNGUS:
                return crimsonGroundTypes;
            case WARPED_FUNGUS:
                return warpedGroundTypes;
            case NETHER_WART:
                return netherwartGroundTypes;
            default:
                throw new IllegalArgumentException();
        }
    }
}
