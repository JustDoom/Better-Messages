package com.justdoom.bettermessages.events;

import com.justdoom.bettermessages.BetterMessages;
import com.justdoom.bettermessages.util.PlayerJoinUtil;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;

import java.util.UUID;

public class PlayerPreLogin implements Listener {
    private final BetterMessages plugin;

    public PlayerPreLogin(BetterMessages plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void AsyncPlayerPreLoginEvent(AsyncPlayerPreLoginEvent event) {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {

            UUID uuid = event.getUniqueId();

            PlayerJoinUtil.addPlayer(uuid, plugin.sqlite.getUuid(uuid));

            if(!plugin.sqlite.getUuid(uuid)){
                plugin.sqlite.insert(uuid);
            } else {
                plugin.sqlite.update(uuid);
            }
        });
    }
}
