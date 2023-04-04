package com.imjustdoom.bettermessages.command.subcommand;

import com.imjustdoom.cmdinstruction.SubCommand;
import com.imjustdoom.bettermessages.BetterMessages;
import com.imjustdoom.bettermessages.config.Config;
import com.imjustdoom.bettermessages.util.MessageUtil;
import org.bukkit.command.CommandSender;

public class ReloadCmd extends SubCommand {

    /**
     * @param sender
     * @param strings
     */
    @Override
    public void execute(CommandSender sender, String[] strings) {
        BetterMessages.getInstance().reloadConfig();
        Config.init();
        sender.sendMessage(MessageUtil.translate(Config.InternalMessages.PREFIX + Config.InternalMessages.RELOADED));
    }
}
