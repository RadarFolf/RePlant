package de.jeff_media.replant.utils;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class CommandUtils {

    public static void printUsage(CommandSender sender, String commandPrefix, HashMap<String,String> commands) {
        for(Map.Entry<String,String> command : commands.entrySet()) {
            sender.sendMessage("§6" + (commandPrefix == null ? "" : commandPrefix + " ") + command.getKey() + " §7- §f" + command.getValue());
        }
    }

}
