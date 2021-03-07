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
        UUID uuid = player.getUniqueId();
        String msg = plugin.handler.doMessage(player, "join", plugin);
        String firstmsg = plugin.handler.doMessage(player, "join.first-join", plugin);
        /**if (plugin.getConfig().getBoolean("join.enabled")) {
            event.setJoinMessage(msg);
        }**/

        SQLite sqlite = new SQLite(plugin);

        if(plugin.getConfig().getBoolean("join.enabled")){
            event.setJoinMessage(null);
            if(plugin.getConfig().getBoolean("join.first-join.enabled") && !sqlite.getUuid(uuid)){
                if(plugin.getConfig().getBoolean("join.first-join.only-to-player")){
                    for(Player p:Bukkit.getOnlinePlayers()){
                        if(p != player){
                            p.sendMessage(msg);
                        } else {
                            p.sendMessage(firstmsg);
                        }
                    }
                } else {
                    for (Player p : Bukkit.getOnlinePlayers()) {
                        p.sendMessage(firstmsg);
                    }
                }
            } else {
                for (Player p : Bukkit.getOnlinePlayers()) {
                    p.sendMessage(msg);
                }
            }
        }

        if(!sqlite.getUuid(uuid)){
            sqlite.insert(uuid);
        } else {
            sqlite.update(uuid);
        }

        /**if (plugin.getConfig().getBoolean("join.first-join.enabled") && sqlite.getUuid(uuid)) {
            if(plugin.getConfig().getBoolean("join.first-join.only-to-player")){
                for (Player p : Bukkit.getOnlinePlayers()) {
                    if (p != player) {
                        p.sendMessage(msg);
                    } else {
                        p.sendMessage(firstmsg);
                    }
                }
            } else {
                for (Player p : Bukkit.getOnlinePlayers()) {
                    p.sendMessage(firstmsg);
                }
            }
        } else if (plugin.getConfig().getBoolean("join.enabled")) {
            for (Player p : Bukkit.getOnlinePlayers()) {
                p.sendMessage(msg);
            }
        }**/
    }
}
