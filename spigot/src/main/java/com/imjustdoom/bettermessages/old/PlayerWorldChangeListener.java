package com.imjustdoom.bettermessages.old.listener;

import com.imjustdoom.bettermessages.BetterMessages;
import com.imjustdoom.bettermessages.old.config.Config;
import com.imjustdoom.bettermessages.old.manager.PlayerManager;
import com.imjustdoom.bettermessages.old.message.EventType;
import com.imjustdoom.bettermessages.old.message.MessageImpl;
import com.imjustdoom.bettermessages.old.util.VanishUtil;
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

        for (MessageImpl msg : Config.MESSAGES.get(EventType.WORLD_CHANGE.getClazz())) {

            if (!msg.canRun(player, event)) {
                continue;
            }

            BetterMessages.getInstance().getStorage().update(player.getUniqueId(), msg.getParent());

            msg.sendMessage(player);
        }
    }
}