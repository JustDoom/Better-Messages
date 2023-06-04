package com.imjustdoom.bettermessages.listener;

import com.imjustdoom.bettermessages.config.Configuration;
import com.imjustdoom.bettermessages.message.Message;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void playerJoinListener(PlayerJoinEvent event) {
        event.setJoinMessage(null);

        for (Message message : Configuration.MESSAGES) {
            for (Player player : Bukkit.getOnlinePlayers()) {
                player.sendMessage(message.getMessage());
            }
        }
    }
}