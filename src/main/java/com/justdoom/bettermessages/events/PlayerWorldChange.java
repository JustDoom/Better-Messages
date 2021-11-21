package com.justdoom.bettermessages.events;

import com.justdoom.bettermessages.BetterMessages;
import com.justdoom.bettermessages.util.MessageUtil;
import com.justdoom.bettermessages.util.VanishUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class PlayerWorldChange implements Listener {
    @EventHandler
    public void WorldChangeEvent(PlayerChangedWorldEvent event) {
        Player player = event.getPlayer();

        // vanished or has silent-world-change permission
        if(VanishUtil.isVanished(player) || player.hasPermission("bettermessages.silent-world-change")) return;

        for (String key : BetterMessages.getInstance().getConfig().getConfigurationSection("world-change").getKeys(false)) {
            String msg = MessageUtil.doMessage(player, "world-change." + key, BetterMessages.getInstance());
            String to = player.getWorld().getName();
            String from = event.getFrom().getName();
            if (BetterMessages.getInstance().getConfig().getBoolean("world-change." + key + ".replace-underscore")) {
                to = to.replace("_", " ");
                from = from.replace("_", " ");
            }
            msg = msg.replace("{to}", to);
            msg = msg.replace("{from}", from);
            if (BetterMessages.getInstance().getConfig().getBoolean("world-change." + key + ".enabled")
                    && BetterMessages.getInstance().getConfig().getString("world-change." + key + ".to").equals(player.getWorld().getName())
                    && BetterMessages.getInstance().getConfig().getString("world-change." + key + ".from").equals(event.getFrom().getName())) {
                if (BetterMessages.getInstance().getConfig().getBoolean("world-change." + key + ".only-to-player")) {
                    if(BetterMessages.getInstance().getConfig().getString("world-change." + key + ".permission").equalsIgnoreCase("none")
                            || player.hasPermission(BetterMessages.getInstance().getConfig().getString("world-change." + key + ".permission")))
                        MessageUtil.messageType(player, msg, BetterMessages.getInstance(), "world-change." + key);
                } else {
                    for (Player p : Bukkit.getOnlinePlayers()) {
                        if(BetterMessages.getInstance().getConfig().getString("world-change." + key + ".permission").equalsIgnoreCase("none")
                                || p.hasPermission(BetterMessages.getInstance().getConfig().getString("world-change." + key + ".permission")))
                            MessageUtil.messageType(p, msg, BetterMessages.getInstance(), "world-change." + key);
                    }
                    return;
                }
            }
        }
    }
}