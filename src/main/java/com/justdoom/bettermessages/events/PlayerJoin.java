package com.justdoom.bettermessages.events;


import com.justdoom.bettermessages.BetterMessages;

import java.util.UUID;

import com.justdoom.bettermessages.manager.PlayerManager;
import com.justdoom.bettermessages.util.MessageUtil;
import com.justdoom.bettermessages.util.VanishUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoin implements Listener {

    @EventHandler
    public void JoinEvent(PlayerJoinEvent event) {

        Player player = event.getPlayer();

        UUID uuid = player.getUniqueId();
        String msg = MessageUtil.doMessage(player, "join", BetterMessages.getInstance());
        String firstmsg = MessageUtil.doMessage(player, "join.first-join", BetterMessages.getInstance());

        if (!BetterMessages.getInstance().getConfig().getBoolean("join.enabled")) return;

        event.setJoinMessage(null);

        // checks need to be after setting join message to null, to work properly
        if (VanishUtil.isVanished(player) || player.hasPermission("bettermessages.silent-join") || PlayerManager.getPlayer(uuid) == null) return;

        if (BetterMessages.getInstance().getConfig().getBoolean("join.first-join.enabled") && !PlayerManager.getPlayer(uuid)) {
            PlayerManager.removePlayer(uuid);
            if (BetterMessages.getInstance().getConfig().getBoolean("join.first-join.only-to-player")) {
                for (Player p : Bukkit.getOnlinePlayers()) {
                    if (p != player) {
                        if(BetterMessages.getInstance().getConfig().getString("join.permission").equalsIgnoreCase("none")
                                || p.hasPermission(BetterMessages.getInstance().getConfig().getString("join.permission")))
                            MessageUtil.messageType(p, msg, BetterMessages.getInstance(), "join");
                    } else {
                        if(BetterMessages.getInstance().getConfig().getString("join.first-join.permission").equalsIgnoreCase("none")
                                || p.hasPermission(BetterMessages.getInstance().getConfig().getString("join.first-join.permission")))
                            MessageUtil.messageType(p, firstmsg, BetterMessages.getInstance(), "join.first-join");
                    }
                }
            } else {
                for (Player p : Bukkit.getOnlinePlayers()) {
                    if(BetterMessages.getInstance().getConfig().getString("join.first-join.permission").equalsIgnoreCase("none")
                            || p.hasPermission(BetterMessages.getInstance().getConfig().getString("join.first-join.permission")))
                        MessageUtil.messageType(p, firstmsg, BetterMessages.getInstance(), "join.first-join");
                }
            }
        } else {
            for (Player p : Bukkit.getOnlinePlayers()) {
                if(BetterMessages.getInstance().getConfig().getString("join.permission").equalsIgnoreCase("none")
                        || p.hasPermission(BetterMessages.getInstance().getConfig().getString("join.permission")))
                    MessageUtil.messageType(p, msg, BetterMessages.getInstance(), "join");
            }
        }
    }
}
