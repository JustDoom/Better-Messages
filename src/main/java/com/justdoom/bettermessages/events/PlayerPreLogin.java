package com.justdoom.bettermessages.events;

import com.justdoom.bettermessages.BetterMessages;
import com.justdoom.bettermessages.manager.PlayerManager;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;

import java.io.File;
import java.nio.file.Paths;
import java.util.UUID;

public class PlayerPreLogin implements Listener {

    @EventHandler
    public void AsyncPlayerPreLoginEvent(AsyncPlayerPreLoginEvent event) {
        UUID uuid = event.getUniqueId();

        PlayerManager.addPlayer(uuid);

        if (!new File(Paths.get(BetterMessages.getInstance().getDataFolder() + "/data/" + uuid + ".yml").toString()).exists()) {
            BetterMessages.getInstance().getStorage().createPlayerData(uuid);
        }
    }
}