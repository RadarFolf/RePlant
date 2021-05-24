package de.jeff_media.replant;

import de.jeff_media.replant.commands.ReplantCommand;
import de.jeff_media.replant.config.Config;
import de.jeff_media.replant.config.ConfigUpdater;
import de.jeff_media.replant.config.Messages;
import de.jeff_media.replant.handlers.ParticleManager;
import de.jeff_media.replant.handlers.SaplingManager;
import de.jeff_media.replant.hooks.PluginHandler;
import de.jeff_media.replant.hooks.WorldGuardHandler;
import de.jeff_media.replant.listeners.CropListener;
import de.jeff_media.replant.listeners.SaplingListener;
import de.jeff_media.replant.nbt.PlayerManager;
import de.jeff_media.replant.utils.FileUtils;
import de.jeff_media.updatechecker.UpdateChecker;
import de.jeff_media.updatechecker.UserAgentBuilder;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

    private static Main instance;

    public static Main getInstance() {
        return instance;
    }
    private PluginHandler worldGuardHandler = new PluginHandler();

    private PlayerManager playerManager;
    private SaplingManager saplingManager;
    private ParticleManager saplingParticleManager, cropParticleManager;
    private static boolean debug = false;

    public ParticleManager getSaplingParticleManager() {
        return saplingParticleManager;
    }

    public ParticleManager getCropParticleManager() {
        return cropParticleManager;
    }

    public static void debug(String text) {
        if(debug) Main.getInstance().getLogger().warning("[DEBUG] " + text);
    }

    public PluginHandler getWorldGuardHandler() {
        return worldGuardHandler;
    }

    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();
        FileUtils.saveDefaultLangFiles();

        reload();

        new CropListener();
        new SaplingListener();
        new ReplantCommand();
        new Messages(getConfig().getString(Config.LANGUAGE));

        playerManager = new PlayerManager();
        saplingManager = new SaplingManager();
        saplingParticleManager = new ParticleManager("sapling");
        cropParticleManager = new ParticleManager("crop");

        initUpdateChecker();

        //playerManager = new PlayerManager();
    }

    private void initUpdateChecker() {
        UpdateChecker.init(this, "https://api.jeff-media.de/replant/latest-version.txt")
                .setUserAgent(UserAgentBuilder.getDefaultUserAgent().addSpigotUserId())
                .setDonationLink("https://paypal.me/mfnalex")
                .setDownloadLink("https://www.spigotmc.org/resources/authors/mfnalex.175238/");
        if(getConfig().getString(Config.CHECK_FOR_UPDATES).equalsIgnoreCase("true")) {
            UpdateChecker.getInstance().checkEveryXHours(getConfig().getDouble(Config.CHECK_FOR_UPDATES_INTERVAL))
                    .checkNow();
        } else if(getConfig().getString(Config.CHECK_FOR_UPDATES).equalsIgnoreCase("on-startup")) {
            UpdateChecker.getInstance().checkNow();
        }
    }

    public PlayerManager getPlayerManager() {
        return playerManager;
    }

    public SaplingManager getSaplingManager() {
        return saplingManager;
    }

    public void reload() {
        reloadConfig();
        ConfigUpdater.updateConfig();
        debug = getConfig().getBoolean(Config.DEBUG);

        if(getConfig().getBoolean(Config.USE_WORLDGUARD)) {
            if(Bukkit.getPluginManager().getPlugin("WorldGuard") != null) {
                try {
                    worldGuardHandler = new WorldGuardHandler();
                } catch(Exception e) {
                    getLogger().warning("Could not hook into WorldGuard although it seems to be installed.");
                    getLogger().warning("Detected WorldGuard version: " + Bukkit.getPluginManager().getPlugin("WorldGuard").getDescription().getVersion());
                    worldGuardHandler = new PluginHandler();
                }
            }
        }

    }
}
