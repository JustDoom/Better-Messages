package com.imjustdoom.bettermessages.old.listener;

import com.imjustdoom.bettermessages.BetterMessages;
import com.imjustdoom.bettermessages.old.manager.PlayerManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;

import java.io.File;
import java.nio.file.Paths;
import java.util.UUID;

public class PlayerPreLoginListener implements Listener {

    @EventHandler
    public void asyncPlayerPreLoginEvent(AsyncPlayerPreLoginEvent event) {
        UUID uuid = event.getUniqueId();

        PlayerManager.addPlayer(uuid);

        if (!new File(Paths.get(BetterMessages.getInstance().getDataFolder() + "/data/" + uuid + ".yml").toString()).exists()) {
            BetterMessages.getInstance().getStorage().createPlayerData(uuid);
        }
    }
}