package com.justdoom.bettermessages.events;

import com.justdoom.bettermessages.BetterMessages;
import com.justdoom.bettermessages.manager.PlayerManager;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;

import java.util.UUID;

public class PlayerPreLogin implements Listener {

    @EventHandler
    public void AsyncPlayerPreLoginEvent(AsyncPlayerPreLoginEvent event) {
        UUID uuid = event.getUniqueId();

        PlayerManager.addPlayer(uuid, BetterMessages.getInstance().getSqlite().getUuid(uuid));

        if (!BetterMessages.getInstance().getSqlite().getUuid(uuid)) {
            BetterMessages.getInstance().getSqlite().insert(uuid);
        } else {
            BetterMessages.getInstance().getSqlite().update(uuid);
        }
    }
}
