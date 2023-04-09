package com.imjustdoom.bettermessages.old.command.subcommand;

import com.imjustdoom.cmdinstruction.SubCommand;
import com.imjustdoom.bettermessages.BetterMessages;
import com.imjustdoom.bettermessages.old.config.Config;
import com.imjustdoom.bettermessages.old.message.MessageImpl;
import com.imjustdoom.bettermessages.old.util.MessageUtil;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class EditMessageCmd extends SubCommand {

    public EditMessageCmd() {
        List<String> list = new ArrayList<>();
        for (List<MessageImpl> msgList : Config.MESSAGES.values()) {
            for (MessageImpl msg : msgList) {
                list.add(msg.getParent());
            }
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

        for (List<MessageImpl> msgList : Config.MESSAGES.values()) {
            for (MessageImpl msg : msgList) {
                if (!msg.getParent().equalsIgnoreCase(args[1])) {
                    continue;
                }
                if (!player.hasPermission("bettermessages.editmsg." + msg.getParent())) {
                    player.sendMessage("§cYou do not have permission to edit this message.");
                    return;
                }

                StringBuilder message = new StringBuilder();
                for (int i = 2; i < args.length; i++) {
                    message.append(args[i]).append(" ");
                }

                BetterMessages.getInstance().getStorage().updateMessage(player.getUniqueId(), "messages." + msg.getParent(), message.toString());
                player.sendMessage(MessageUtil.translate(Config.InternalMessages.CHANGED_MESSAGE).replace("{message}", args[1]));
            }
        }
    }
}