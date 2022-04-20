package com.justdoom.bettermessages.commands;

import com.justdoom.bettermessages.BetterMessages;
import com.justdoom.bettermessages.config.Config;
import com.justdoom.bettermessages.message.Message;
import com.justdoom.bettermessages.util.MessageUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class BetterMessagesCommand implements CommandExecutor {

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(args.length > 1) {
            if(args[0].equalsIgnoreCase("editmsg")) {
                if(sender instanceof Player) {
                    Player player = (Player) sender;

                    for(Message msg : Config.MESSAGES) {
                        if(msg.getParent().equalsIgnoreCase(args[1])) {
                            if(player.hasPermission("bettermessages.editmsg." + msg.getParent())) {
                                StringBuilder message = new StringBuilder();
                                for (int i = 2; i < args.length; i++) {
                                    message.append(args[i]).append(" ");
                                }
                                BetterMessages.getInstance().getStorage().updateMessage(player.getUniqueId(),
                                        msg.getParent(), message.toString());
                            } else {
                                player.sendMessage("§cYou do not have permission to edit this message.");
                            }
                        }
                    }

                }
            }
        } else if(args.length == 1) {
            if(args[0].equalsIgnoreCase("editmsg")) {
                sender.sendMessage("Please specify a message to edit.");
            }
        }

        if (command.getName().equalsIgnoreCase("bettermessages") && sender.hasPermission("bettermessages"))
            if (args.length == 0) {
                sender.sendMessage(MessageUtil.translate(Config.InternalMessages.PREFIX + Config.InternalMessages.HELP_REDIRECT));
            } else if (args[0].equalsIgnoreCase("reload") && sender.hasPermission("bettermessages.reload")) {
                BetterMessages.getInstance().reloadConfig();
                Config.init();
                sender.sendMessage(MessageUtil.translate(Config.InternalMessages.PREFIX + Config.InternalMessages.RELOADED));
            } else if (args[0].equalsIgnoreCase("help") && sender.hasPermission("bettermessages.help")) {
                sender.sendMessage(MessageUtil.translate(Config.InternalMessages.PREFIX + Config.InternalMessages.HELP));
            }
        return false;
    }
}