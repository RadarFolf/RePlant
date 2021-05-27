package de.jeff_media.replant.config;

import de.jeff_media.replant.Main;
import de.jeff_media.replant.utils.TimeUtils;

public class Config {

    public static final String CONFIG_PLUGIN_VERSION = "plugin-version";
    public static final String CONFIG_VERSION = "config-version";
    public static final String USE_PERMISSIONS = "use-permissions";
    public static final String LANGUAGE = "language";
    public static final String CROP_REPLANT_DELAY = "crop-replant-delay";
    public static final String CROP_REPLANT_COSTS = "crop-replant-costs";
    public static final String CROP_REPLANT_ENABLED_BY_DEFAULT = "crop-replant-enabled-by-default";
    public static final String SAPLING_REPLANT = "plant-fallen-saplings";
    public static final String SAPLING_REPLANT_DELAY = "plant-fallen-saplings-delay";
    public static final String SAPLING_REPLANT_THROWN_BY_PLAYER = "plant-fallen-saplings-thrown-by-player";
    public static final String SAPLING_REPLANT_SEARCH_NEARBY = "search-nearby-when-block-is-occupied";
    public static final String CHECK_FOR_UPDATES = "check-for-updates";
    public static final String CHECK_FOR_UPDATES_INTERVAL = "check-for-updates-interval";
    public static final String CALL_BLOCK_PLACE_EVENT = "call-block-place-event";
    public static final String USE_WORLDGUARD = "use-worldguard";
    public static final String DEBUG = "debug";

    public static void init() {
        addDefault(USE_PERMISSIONS, false);
        addDefault(LANGUAGE, "en");
        addDefault(CROP_REPLANT_DELAY, 1.0D);
        addDefault(CROP_REPLANT_COSTS, true);
        addDefault(CROP_REPLANT_ENABLED_BY_DEFAULT, false);
        addDefault(SAPLING_REPLANT, true);
        addDefault(SAPLING_REPLANT_DELAY, 120.0D);
        addDefault(SAPLING_REPLANT_THROWN_BY_PLAYER, true);
        addDefault(SAPLING_REPLANT_SEARCH_NEARBY, true);
        addDefault(CHECK_FOR_UPDATES, true);
        addDefault(CHECK_FOR_UPDATES_INTERVAL, 4);
        addDefault(CALL_BLOCK_PLACE_EVENT, true);
        addDefault(USE_WORLDGUARD, true);
        addDefault(DEBUG, false);
    }

    private static void addDefault(String node, Object value) {
        Main.getInstance().getConfig().addDefault(node, value);
    }

    public static int getCropReplantDelayInTicks() {
        double delayInSeconds = Main.getInstance().getConfig().getDouble(Config.CROP_REPLANT_DELAY);
        return TimeUtils.secondsToTicks(delayInSeconds);
    }

    public static int getSaplingReplantDelayInTicks() {
        double delayInSeconds = Main.getInstance().getConfig().getDouble(Config.SAPLING_REPLANT_DELAY);
        delayInSeconds = Math.min(delayInSeconds,300); // 5 Minutes
        return TimeUtils.secondsToTicks(delayInSeconds);
    }
}
