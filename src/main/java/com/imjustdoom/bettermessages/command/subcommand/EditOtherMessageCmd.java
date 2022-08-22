package com.imjustdoom.bettermessages.command.subcommand;

import com.imjustdoom.cmdinstruction.SubCommand;
import com.imjustdoom.bettermessages.BetterMessages;
import com.imjustdoom.bettermessages.config.Config;
import com.imjustdoom.bettermessages.message.Message;
import com.imjustdoom.bettermessages.util.MessageUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class EditOtherMessageCmd extends SubCommand {

    public EditOtherMessageCmd() {
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

        Player player = Bukkit.getPlayer(args[1]);
        if (!sender.hasPermission("bettermessages.editmsg.other")) {
            sender.sendMessage("You don't have permission for this command");
            return;
        }

        if (args.length < 4) {
            sender.sendMessage("You must specify a message to edit and what to set it as");
            return;
        }

        if (!Objects.requireNonNull(Bukkit.getPlayer(args[1])).isOnline()) {
            sender.sendMessage("Can't find player");
            return;
        }

        for (Message msg : Config.MESSAGES) {
            if (!msg.getParent().equalsIgnoreCase(args[2])) continue;
            if (player.hasPermission("bettermessages.editmsg." + msg.getParent())) {
                player.sendMessage("§cYou do not have permission to edit this message.");
                return;
            }

            StringBuilder message = new StringBuilder();
            for (int i = 3; i < args.length; i++) message.append(args[i]).append(" ");

            BetterMessages.getInstance().getStorage().updateMessage(player.getUniqueId(), "messages." + msg.getParent(), message.toString());
            player.sendMessage(MessageUtil.translatePlaceholders(Config.InternalMessages.CHANGED_MESSAGE, player).replace("{message}", args[2]));
        }
    }
}
