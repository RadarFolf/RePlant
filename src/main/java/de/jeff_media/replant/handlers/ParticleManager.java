package de.jeff_media.replant.handlers;

import com.google.common.base.Enums;
import de.jeff_media.replant.Main;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.util.Vector;

import java.util.Locale;

public class ParticleManager {

    private final Main main = Main.getInstance();
    private final String node;

    public ParticleManager(String node) {
        this.node = node;
    }

    public boolean isEnabled() {
        return main.getConfig().getBoolean(node + "-particles-enabled");
    }

    public Particle getParticleType() {
        return Enums.getIfPresent(Particle.class,main.getConfig().getString(node + "-particles-type").toUpperCase(Locale.ROOT)).or(Particle.VILLAGER_HAPPY);
    }

    public int getParticleCount() {
        System.out.println(main.getConfig().getInt(node + "-particles-count"));
        return main.getConfig().getInt(node + "-particles-count");
    }

    public void spawnParticles(Block block) {
        if(!isEnabled()) return;
        Location location = block.getLocation().add(new Vector(0.5,0.1,0.5));

        Particle type = getParticleType();
        int count = getParticleCount();
        location.getWorld().spawnParticle(type, location, getParticleCount(), 0.5, 0.1, 0.5);

    }

}
