package com.imjustdoom.bettermessages.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {

    @EventHandler(priority = EventPriority.LOWEST)
    public void playerJoinListener(PlayerJoinEvent event) {
        event.setJoinMessage(null);


    }
}