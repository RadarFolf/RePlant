package de.jeff_media.replant.hooks;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.util.Location;
import com.sk89q.worldedit.world.World;
import com.sk89q.worldguard.LocalPlayer;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.internal.platform.WorldGuardPlatform;
import com.sk89q.worldguard.protection.flags.Flags;
import com.sk89q.worldguard.protection.regions.RegionQuery;
import de.jeff_media.replant.Main;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public class WorldGuardHandler extends PluginHandler {

    private static final WorldGuardPlatform platform;

    static {
        WorldGuardPlatform pf;
        try {
            pf = WorldGuard.getInstance().getPlatform();
        } catch(Exception e) {
            pf = null;
        }
        platform = pf;
    }

    @Override
    public boolean canBuild(Player player, Block block) {
        if(platform == null) return true;
        RegionQuery query = platform.getRegionContainer().createQuery();
        Location location = BukkitAdapter.adapt(block.getLocation());
        World world = BukkitAdapter.adapt(block.getWorld());
        LocalPlayer localPlayer = WorldGuardPlugin.inst().wrapPlayer(player);
        if(!platform.getSessionManager().hasBypass(localPlayer, world)) {
            return query.testState(location, localPlayer, Flags.BUILD);
        }
        return true;
    }

}
