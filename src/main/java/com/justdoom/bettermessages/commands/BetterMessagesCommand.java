package com.justdoom.bettermessages.commands;

import com.justdoom.bettermessages.BetterMessages;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class BetterMessagesCommand implements CommandExecutor {
    private final BetterMessages plugin;

    public BetterMessagesCommand(BetterMessages plugin) {
        this.plugin = plugin;
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("bettermessages") && sender.hasPermission(""))
            if (args.length == 0) {
                sender.sendMessage("Type \"/bm help\" for help on Better Messages!");
            } else if (args[0].equalsIgnoreCase("reload") && sender.hasPermission("bettermessages.reload")) {
                this.plugin.reloadConfig();
                sender.sendMessage("BetterMessages config has been reloaded.");
            } else if (args[0].equalsIgnoreCase("help") && sender.hasPermission("bettermessages.help")) {
                sender.sendMessage("Better Messages Help\nCommands\n- /bettermessages help: Brings you here!\n- /bettermessages reload: Reloads the config!");
            }
        return false;
    }
}