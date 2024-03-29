package com.imjustdoom.bettermessages.listener;

import com.imjustdoom.bettermessages.BetterMessages;
import com.imjustdoom.bettermessages.config.Config;
import com.imjustdoom.bettermessages.manager.PlayerManager;
import com.imjustdoom.bettermessages.message.EventType;
import com.imjustdoom.bettermessages.message.Message;
import com.imjustdoom.bettermessages.util.VanishUtil;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;

public class PlayerWorldChangeListener implements Listener {

    @EventHandler(priority = EventPriority.LOWEST)
    public void worldChangeEvent(PlayerChangedWorldEvent event) {
        Player player = event.getPlayer();

        PlayerManager.removePlayer(player.getUniqueId());

        if (VanishUtil.isVanished(player) || player.hasPermission("bettermessages.silent-world-change")) {
            return;
        }

        Message pMessage = null;

        for (Message msg : Config.MESSAGES.get(EventType.WORLD_CHANGE.getClazz())) {

            if (msg.getExtraInfo() != null) {
                String from = msg.getExtraInfo().split("/")[0];
                String to = msg.getExtraInfo().split("/")[1];

                if (!from.equalsIgnoreCase(event.getFrom().getName()) || !player.getWorld().getName().equalsIgnoreCase(to)) {
                    continue;
                }
            }

            BetterMessages.getInstance().getStorage().update(player.getUniqueId(), msg.getParent());

            if (!msg.canRun(player, event)) {
                continue;
            }

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

            msg.sendMessage(player);
        }
        if (pMessage != null) {
            pMessage.sendMessage(player);
        }
    }
}