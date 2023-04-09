package com.imjustdoom.bettermessages.old.listener;


import com.imjustdoom.bettermessages.BetterMessages;
import com.imjustdoom.bettermessages.old.config.Config;
import com.imjustdoom.bettermessages.old.listener.event.ServerSwitchEvent;
import com.imjustdoom.bettermessages.old.manager.PlayerManager;
import com.imjustdoom.bettermessages.old.message.EventType;
import com.imjustdoom.bettermessages.old.message.MessageImpl;
import com.imjustdoom.bettermessages.old.util.VanishUtil;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

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

        MessageImpl pMessage = null;

        if (event.getFrom().equals("")) {
            for (MessageImpl msg : Config.MESSAGES.get(EventType.JOIN.getClazz())) {

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

        for (MessageImpl msg : Config.MESSAGES.get(EventType.SERVER_SWITCH.getClazz())) {

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

