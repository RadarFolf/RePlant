package de.jeff_media.replant.utils;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CommandUtils {

    public static void printUsage(CommandSender sender, String commandPrefix, HashMap<String,String> commands) {

        ArrayList<String> keys = new ArrayList<>(commands.keySet());
        keys.sort(String::compareTo);

        for(String key : keys) {
            String value = commands.get(key);
            sender.sendMessage("§6" + (commandPrefix == null ? "" : commandPrefix + " ") + key + " §7- §f" + value);
        }
    }

}
