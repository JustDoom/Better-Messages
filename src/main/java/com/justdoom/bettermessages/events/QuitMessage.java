package com.justdoom.bettermessages.events;


import com.justdoom.bettermessages.BetterMessages;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class QuitMessage implements Listener {
    private BetterMessages plugin;

    public QuitMessage(BetterMessages plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void QuitEvent(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        String msg = plugin.handler.doMessage(player, "quit", plugin);
        if (plugin.getConfig().getBoolean("quit.enabled"))
            event.setQuitMessage(msg);
    }
}

