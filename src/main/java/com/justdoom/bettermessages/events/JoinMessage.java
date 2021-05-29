package com.justdoom.bettermessages.events;


import com.justdoom.bettermessages.BetterMessages;

import java.util.UUID;

import com.justdoom.bettermessages.util.VanishUtil;
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

        if(VanishUtil.isVanished(player)){
            return;
        }

        UUID uuid = player.getUniqueId();
        String msg = plugin.handler.doMessage(player, "join", plugin);
        String firstmsg = plugin.handler.doMessage(player, "join.first-join", plugin);

        if(plugin.getConfig().getBoolean("join.enabled")){
            event.setJoinMessage(null);
            if(plugin.getConfig().getBoolean("join.first-join.enabled") && !plugin.sqlite.getUuid(uuid)){
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

        if(!plugin.sqlite.getUuid(uuid)){
            plugin.sqlite.insert(uuid);
        } else {
            plugin.sqlite.update(uuid);
        }
    }
}
