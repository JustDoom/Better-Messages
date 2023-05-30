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
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.io.File;

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

        Message pMessage = null;

        for (Message msg : Config.MESSAGES.get(EventType.JOIN.getClazz())) {

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

