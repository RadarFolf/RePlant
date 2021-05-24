package de.jeff_media.replant.commands;

import de.jeff_media.replant.Main;
import de.jeff_media.replant.config.Messages;
import de.jeff_media.replant.config.Permissions;
import de.jeff_media.replant.utils.CommandUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import static de.jeff_media.replant.config.Messages.*;

public class ReplantCommand implements CommandExecutor, TabCompleter {

    private static final Main main = Main.getInstance();

    public ReplantCommand() {
        main.getCommand("replant").setExecutor(this);
        main.getCommand("replant").setTabCompleter(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String alias, String[] args) {

        if(!Permissions.isAllowed(sender, Permissions.PERMISSION_USE)) {
            sender.sendMessage(command.getPermissionMessage());
            return true;
        }

        if(args.length==0) {
            usage(sender, null);
            return true;
        }

        /*
        Console commands
         */
        if(args.length==1) {
            switch(args[0].toLowerCase(Locale.ROOT)) {
                case "reload":
                    if(!Permissions.isAllowed(sender, Permissions.PERMISSION_RELOAD)) {
                        sender.sendMessage(command.getPermissionMessage());
                        return true;
                    }
                    main.reload();
                    sendMessage(sender, CONFIG_RELOADED);
                    return true;
            }
        }

        if(!(sender instanceof Player)) {
            sendMessage(sender, COMMAND_PLAYERS_ONLY);
            return true;
        }

        Player player = (Player) sender;

        /*
        Player commands
         */
        if(args.length==1) {
            switch (args[0].toLowerCase(Locale.ROOT)) {
                case "crops":
                    main.getPlayerManager().toggleCrops(player);
                    return true;
            }
        }

        usage(sender, null);
        return true;
    }

    private void usage(CommandSender sender, String arg) {
        HashMap<String,String> commands = new HashMap<>();
        if(arg==null) {
            commands.put("crops","Toggles auto-replanting crops");
            commands.put("reload","Reloads config file");
            //commands.put("trees","Toggle auto-replanting trees");
            CommandUtils.printUsage(sender,"/replant",commands);
        }
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if(args.length<2) return Arrays.asList(new String[] {"crops","reload"});
        return null;
    }
}
