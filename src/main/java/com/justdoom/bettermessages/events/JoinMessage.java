package com.justdoom.bettermessages.events;


import com.justdoom.bettermessages.BetterMessages;

import java.util.UUID;

import com.justdoom.bettermessages.sqlite.SQLite;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class JoinMessage implements Listener {
    private final BetterMessages plugin;

    public JoinMessage(BetterMessages plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void JoinEvent(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        String msg = this.plugin.handler.doMessage(player, "join", this.plugin);
        String firstmsg = this.plugin.handler.doMessage(player, "join.first-join", this.plugin);
        if (this.plugin.getConfig().getBoolean("join.enabled")) {
            event.setJoinMessage(msg);
        } else if (this.plugin.getConfig().getBoolean("join.first-join.enabled")) {
            event.setJoinMessage(firstmsg);
        }
        UUID uuid = player.getUniqueId();

        SQLite app = new SQLite(plugin);
        if(!app.getUuid(uuid)){
            app.insert(uuid);
            for(Player p: Bukkit.getOnlinePlayers()) {
                p.sendMessage("first join");
            }
        } else {
            app.update(uuid);
            for(Player p: Bukkit.getOnlinePlayers()) {
                p.sendMessage("not first join");
            }
        }
    }
}
