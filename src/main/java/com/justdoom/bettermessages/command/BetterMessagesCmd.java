package com.justdoom.bettermessages.command;

import com.imjustdoom.cmdinstruction.Command;
import com.imjustdoom.cmdinstruction.SubCommand;
import com.justdoom.bettermessages.command.subcommand.EditMessageCmd;
import com.justdoom.bettermessages.command.subcommand.EditOtherMessageCmd;
import com.justdoom.bettermessages.command.subcommand.HelpCmd;
import com.justdoom.bettermessages.command.subcommand.ReloadCmd;
import com.justdoom.bettermessages.config.Config;
import com.justdoom.bettermessages.util.MessageUtil;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;

public class BetterMessagesCmd extends Command {

    public BetterMessagesCmd() {
        setSubcommands(
                new ReloadCmd().setName("reload").setPermission("bettermessages.reload"),
                new HelpCmd().setName("help").setPermission("bettermessages.help"),
                new EditMessageCmd().setName("editmsg").setPermission("bettermessages.editmsg").setRequiresArgs(true),
                new EditOtherMessageCmd().setName("editmsgother").setPermission("bettermessages.editmsgother").setRequiresArgs(true)
        );

        List<String> tabCompletions = new ArrayList<>();
        for (SubCommand subCommand : getSubcommands()) {
            tabCompletions.add(subCommand.getName());
        }
        setTabCompletions(tabCompletions);
    }

    /**
     * @param sender
     * @param strings
     */
    @Override
    public void execute(CommandSender sender, String[] strings) {
        sender.sendMessage(MessageUtil.translate(Config.InternalMessages.PREFIX + Config.InternalMessages.HELP_REDIRECT));
    }
}