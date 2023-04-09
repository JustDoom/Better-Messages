package com.imjustdoom.bettermessages.old.command.subcommand;

import com.imjustdoom.cmdinstruction.SubCommand;
import com.imjustdoom.bettermessages.old.config.Config;
import com.imjustdoom.bettermessages.old.util.MessageUtil;
import org.bukkit.command.CommandSender;

public class HelpCmd extends SubCommand {

    /**
     * @param sender
     * @param strings
     */
    @Override
    public void execute(CommandSender sender, String[] strings) {
        sender.sendMessage(MessageUtil.translate(Config.InternalMessages.PREFIX + Config.InternalMessages.HELP));
    }
}
