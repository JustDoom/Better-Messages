package com.imjustdoom.bettermessages.old.listener;


import com.imjustdoom.bettermessages.BetterMessages;

import com.imjustdoom.bettermessages.old.PlayerManager;
import com.imjustdoom.bettermessages.old.config.Config;
import com.imjustdoom.bettermessages.old.manager.PlayerManager;
import com.imjustdoom.bettermessages.old.message.EventType;
import com.imjustdoom.bettermessages.old.message.MessageImpl;
import com.imjustdoom.bettermessages.old.util.VanishUtil;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {

    @EventHandler(priority = EventPriority.LOWEST)
    public void joinEvent(PlayerJoinEvent event) {

        if (Config.BUNGEECORD_MODE) {
            event.setJoinMessage(null);
            return;
        }

        Player player = event.getPlayer();

        PlayerManager.addWaitingPlayer(player.getUniqueId(), event.getPlayer().getWorld());
        PlayerManager.removePlayer(player.getUniqueId());

        if (VanishUtil.isVanished(player) || player.hasPermission("bettermessages.silent-join")) {
            return;
        }

        MessageImpl pMessage = null;

        for (MessageImpl msg : Config.MESSAGES.get(EventType.JOIN.getClazz())) {

            if (!msg.canRun(player, event)) {
                continue;
            }

            BetterMessages.getInstance().getStorage().update(player.getUniqueId(), msg.getParent());

            if (msg.getPriority() != -1) {
                if (pMessage == null) {
                    pMessage = msg;
                    continue;
                }
                if (msg.getPriority() < pMessage.getPriority()) {
                    pMessage = msg;
                }
                continue;
            }

            event.setJoinMessage(null);
            msg.sendMessage(player);
        }
        if (pMessage != null) {
            event.setJoinMessage(null);
            pMessage.sendMessage(player);
        }
    }
}

