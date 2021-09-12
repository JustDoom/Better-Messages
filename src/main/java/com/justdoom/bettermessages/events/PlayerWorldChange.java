package com.justdoom.bettermessages.events;

import com.justdoom.bettermessages.BetterMessages;
import com.justdoom.bettermessages.util.VanishUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class PlayerWorldChange implements Listener {
    private BetterMessages plugin;

    public PlayerWorldChange(BetterMessages plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void WorldChangeEvent(PlayerChangedWorldEvent event) {
        Player player = event.getPlayer();

        // vanished or has silent-world-change permission
        if(VanishUtil.isVanished(player) || player.hasPermission("bettermessages.silent-world-change")){
            return;
        }

        for (String key : plugin.getConfig().getConfigurationSection("world-change").getKeys(false)) {
            String msg = plugin.handler.doMessage(player, "world-change." + key, plugin);
            String to = player.getWorld().getName();
            String from = event.getFrom().getName();
            if (plugin.getConfig().getBoolean("world-change." + key + ".replace-underscore")) {
                to = to.replace("_", " ");
                from = from.replace("_", " ");
            }
            msg = msg.replace("{to}", to);
            msg = msg.replace("{from}", from);
            if (plugin.getConfig().getBoolean("world-change." + key + ".enabled")
                    && plugin.getConfig().getString("world-change." + key + ".to").equals(player.getWorld().getName())
                    && plugin.getConfig().getString("world-change." + key + ".from").equals(event.getFrom().getName())) {
                if (plugin.getConfig().getBoolean("world-change." + key + ".only-to-player")) {
                    plugin.handler.messageType(player, msg, plugin, "world-change." + key);
                } else {
                    for (Player p : Bukkit.getOnlinePlayers()) {
                        plugin.handler.messageType(p, msg, plugin, "world-change." + key);
                    }
                    return;
                }
            }
        }
    }
}