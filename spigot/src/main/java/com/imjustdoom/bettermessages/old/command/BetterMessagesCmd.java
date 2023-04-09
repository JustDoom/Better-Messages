package com.imjustdoom.bettermessages.old.command;

import com.imjustdoom.cmdinstruction.Command;
import com.imjustdoom.cmdinstruction.SubCommand;
import com.imjustdoom.bettermessages.old.command.subcommand.EditMessageCmd;
import com.imjustdoom.bettermessages.old.command.subcommand.EditOtherMessageCmd;
import com.imjustdoom.bettermessages.old.command.subcommand.HelpCmd;
import com.imjustdoom.bettermessages.old.command.subcommand.ReloadCmd;
import com.imjustdoom.bettermessages.old.config.Config;
import com.imjustdoom.bettermessages.old.util.MessageUtil;
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