package com.imjustdoom.bettermessages.command.subcommand;

import com.imjustdoom.cmdinstruction.SubCommand;
import com.imjustdoom.bettermessages.BetterMessages;
import com.imjustdoom.bettermessages.config.Config;
import com.imjustdoom.bettermessages.message.Message;
import com.imjustdoom.bettermessages.util.MessageUtil;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class EditMessageCmd extends SubCommand {

    public EditMessageCmd() {
        List<String> list = new ArrayList<>();
        for (Message msg : Config.MESSAGES) {
            list.add(msg.getParent());
        }
        setTabCompletions(list);
    }

    /**
     * @param sender
     * @param args
     */
    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("You must be a player to use this command");
            return;
        }

        if (args.length < 3) {
            sender.sendMessage("You must specify a message to edit and what to set it as");
            return;
        }

        Player player = (Player) sender;

        for (Message msg : Config.MESSAGES) {
            if (!msg.getParent().equalsIgnoreCase(args[1])) continue;
            if (!player.hasPermission("bettermessages.editmsg." + msg.getParent())) {
                player.sendMessage("§cYou do not have permission to edit this message.");
                return;
            }

            StringBuilder message = new StringBuilder();
            for (int i = 2; i < args.length; i++) message.append(args[i]).append(" ");

            BetterMessages.getInstance().getStorage().updateMessage(player.getUniqueId(), "messages." + msg.getParent(), message.toString());
            player.sendMessage(MessageUtil.translatePlaceholders(Config.InternalMessages.CHANGED_MESSAGE, player).replace("{message}", args[1]));
        }
    }
}