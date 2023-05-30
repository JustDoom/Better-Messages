package com.imjustdoom.bettermessages.listener;


import com.imjustdoom.bettermessages.BetterMessages;
import com.imjustdoom.bettermessages.config.Config;
import com.imjustdoom.bettermessages.listener.event.ServerSwitchEvent;
import com.imjustdoom.bettermessages.manager.PlayerManager;
import com.imjustdoom.bettermessages.message.EventType;
import com.imjustdoom.bettermessages.message.Message;
import com.imjustdoom.bettermessages.util.VanishUtil;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class ServerSwitchListener implements Listener {

    // TODO: make all listeners just call a method in a manager class or something to run this code

    @EventHandler
    public void serverSwitch(ServerSwitchEvent event) {

        Player player = event.getPlayer();

        PlayerManager.addWaitingPlayer(player.getUniqueId(), player.getWorld());
        PlayerManager.removePlayer(player.getUniqueId());

        if (VanishUtil.isVanished(player) || player.hasPermission("bettermessages.silent-join")) {
            return;
        }

        Message pMessage = null;

        if (event.getFrom().equals("")) {
            for (Message msg : Config.MESSAGES.get(EventType.JOIN.getClazz())) {

                // TODO: readd this
                if (!msg.canRun(player, null)) {
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

                msg.sendMessage(player);
            }
            if (pMessage != null) {
                pMessage.sendMessage(player);
            }
            return;
        }

        for (Message msg : Config.MESSAGES.get(EventType.SERVER_SWITCH.getClazz())) {

            // TODO: readd this
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

            msg.sendMessage(player);
        }
        if (pMessage != null) {
            pMessage.sendMessage(player);
        }
    }
}

