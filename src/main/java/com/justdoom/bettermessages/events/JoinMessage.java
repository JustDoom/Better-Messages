package com.justdoom.bettermessages.events;


import com.justdoom.bettermessages.BetterMessages;

import java.util.UUID;

import com.justdoom.bettermessages.sqlite.SQLite;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
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
                            plugin.handler.messageType(p, msg, plugin, "join");
                        } else {
                            plugin.handler.messageType(p, firstmsg, plugin, "join.first-join");
                        }
                    }
                } else {
                    for (Player p : Bukkit.getOnlinePlayers()) {
                        plugin.handler.messageType(p, firstmsg, plugin, "join.first-join");
                    }
                }
            } else {
                for (Player p : Bukkit.getOnlinePlayers()) {
                    plugin.handler.messageType(p, msg, plugin, "join");
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
