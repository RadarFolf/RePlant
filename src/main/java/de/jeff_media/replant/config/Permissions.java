package de.jeff_media.replant.config;

import de.jeff_media.replant.Main;
import org.bukkit.permissions.Permissible;

public class Permissions {

    public static String PERMISSION_USE = "replant.use";

    public static boolean isAllowed(Permissible permissible, String permission) {
        return !Main.getInstance().getConfig().getBoolean(Config.USE_PERMISSIONS)
                || permissible.hasPermission(permission);
    }

}
