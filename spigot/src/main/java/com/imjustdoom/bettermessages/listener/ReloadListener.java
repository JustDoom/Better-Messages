package com.imjustdoom.bettermessages.listener;

import better.reload.api.ReloadEvent;
import com.imjustdoom.bettermessages.BetterMessages;
import com.imjustdoom.bettermessages.config.Config;
import com.imjustdoom.bettermessages.util.MessageUtil;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class ReloadListener implements Listener {

    @EventHandler
    public void reloadEvent(ReloadEvent event) {
        BetterMessages.getInstance().reloadConfig();
        Config.init();
        event.getCommandSender().sendMessage(MessageUtil.translate(Config.InternalMessages.PREFIX + Config.InternalMessages.RELOADED));
    }
}
