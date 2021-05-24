package de.jeff_media.replant.config;

import de.jeff_media.replant.Main;
import de.jeff_media.replant.utils.FileUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.InputStreamReader;

public class Messages {

    private static final Main main = Main.getInstance();
    private static final File langDir = new File(main.getDataFolder(),"lang");

    private static YamlConfiguration yaml;

    private static String PREFIX;

    public static String COMMAND_PLAYERS_ONLY;
    public static String CONFIG_RELOADED;
    public static String REPLANT_CROPS_ENABLED, REPLANT_CROPS_DISABLED;

    public Messages(String lang) {
        File file = getLangFile(lang);
        if(!file.exists()) {
            main.getLogger().warning(String.format("Language '%s' not found in %s - falling back to default English (en) translation.",lang,langDir.getAbsolutePath()));
            yaml = YamlConfiguration.loadConfiguration(new InputStreamReader(FileUtils.getFileFromResourceAsStream("lang/en.yml")));
        } else {
            main.getLogger().info(String.format("Using language '%s'",lang));
            yaml = YamlConfiguration.loadConfiguration(file);
        }

        PREFIX = main.getConfig().getString("prefix","");

        COMMAND_PLAYERS_ONLY = yaml.getString("command-players-only");
        CONFIG_RELOADED = yaml.getString("config-reloaded");

        REPLANT_CROPS_ENABLED = yaml.getString("replant-crops-enabled");
        REPLANT_CROPS_DISABLED = yaml.getString("replant-crops-disabled");
    }

    private File getLangFile(String lang) {
        langDir.mkdirs();
        return new File(langDir,lang + ".yml");
    }

    public static void sendMessage(CommandSender receiver, String message) {
        if(message == null || message.length()==0) return;
        receiver.sendMessage(ChatColor.translateAlternateColorCodes('&',PREFIX+message));
    }

}
