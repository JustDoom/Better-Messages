package com.justdoom.bettermessages.events;


import com.justdoom.bettermessages.BetterMessages;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class QuitMessage implements Listener {
    private BetterMessages plugin;

    public QuitMessage(BetterMessages plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void QuitEvent(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        String msg = plugin.handler.doMessage(player, "quit", plugin);
        if (plugin.getConfig().getBoolean("quit.enabled")) {
            event.setQuitMessage(null);
            for(Player p: Bukkit.getOnlinePlayers()) {
                plugin.handler.messageType(p, msg, plugin, "quit");
            }
        }
    }
}

