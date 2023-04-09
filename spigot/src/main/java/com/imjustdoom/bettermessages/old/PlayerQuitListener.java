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
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerQuitListener implements Listener {

    @EventHandler(priority = EventPriority.LOWEST)
    public void quitEvent(PlayerQuitEvent event) {

        Player player = event.getPlayer();

        PlayerManager.removePlayer(player.getUniqueId());

        if (VanishUtil.isVanished(player) || player.hasPermission("bettermessages.silent-quit")) {
            return; // TODO: try to add something so certain roles can still see a message even if the user is vanished
        }

        MessageImpl pMessage = null;

        for (MessageImpl msg : Config.MESSAGES.get(EventType.QUIT.getClazz())) {

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

            event.setQuitMessage(null);
            msg.sendMessage(player);
        }
        if (pMessage != null) {
            event.setQuitMessage(null);
            pMessage.sendMessage(player);
        }
    }
}

