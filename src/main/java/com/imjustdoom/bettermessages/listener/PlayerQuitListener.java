package com.imjustdoom.bettermessages.listener;


import com.imjustdoom.bettermessages.BetterMessages;
import com.imjustdoom.bettermessages.config.Config;
import com.imjustdoom.bettermessages.manager.PlayerManager;
import com.imjustdoom.bettermessages.message.EventType;
import com.imjustdoom.bettermessages.message.Message;
import com.imjustdoom.bettermessages.util.MessageUtil;
import com.imjustdoom.bettermessages.util.VanishUtil;
import org.bukkit.Bukkit;
import org.bukkit.Statistic;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerQuitListener implements Listener {

    @EventHandler(priority = EventPriority.LOWEST)
    public void quitEvent(PlayerQuitEvent event) {

        Player player = event.getPlayer();

        PlayerManager.removePlayer(player.getUniqueId());

        if (VanishUtil.isVanished(player) || player.hasPermission("bettermessages.silent-quit")) return;

        Message pMessage = null;

        for (Message msg : Config.MESSAGES.get(EventType.QUIT)) {

            if (!msg.canRun(player, event)) continue;

            BetterMessages.getInstance().getStorage().update(player.getUniqueId(), msg.getParent());

            if (msg.getPriority() != -1) {
                if (pMessage == null) {
                    pMessage = msg;
                    continue;
                }
                if (msg.getPriority() < pMessage.getPriority())
                    pMessage = msg;
                continue;
            }

            event.setQuitMessage(null);
            msg.sendMessage(player);
        }
        if (pMessage != null) {
            event.setQuitMessage(null);
            pMessage.sendMessage(player);
        }
    }
}

