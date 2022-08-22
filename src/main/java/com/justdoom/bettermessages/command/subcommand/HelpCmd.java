package com.justdoom.bettermessages.command.subcommand;

import com.imjustdoom.cmdinstruction.SubCommand;
import com.justdoom.bettermessages.BetterMessages;
import com.justdoom.bettermessages.config.Config;
import com.justdoom.bettermessages.util.MessageUtil;
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
