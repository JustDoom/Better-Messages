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

        if(VanishUtil.isVanished(player)){
            return;
        }

        for (String key : plugin.getConfig().getConfigurationSection("world-change").getKeys(false)) {
            String msg = plugin.handler.doMessage(player, "world-change." + key, plugin);
            msg = msg.replace("{to}", player.getWorld().getName());
            msg = msg.replace("{from}", event.getFrom().getName());
            if (plugin.getConfig().getBoolean("world-change." + key + ".replace-underscore")) {
                msg = msg.replace("_", " ");
            }
            if (plugin.getConfig().getBoolean("world-change." + key + ".enabled") && plugin.getConfig().getString("world-change." + key + ".to").equals(player.getWorld().getName()) && plugin.getConfig().getString("world-change." + key + ".from").equals(event.getFrom().getName())) {
                for (Player p : Bukkit.getOnlinePlayers()) {
                    plugin.handler.messageType(p, msg, plugin, "world-change." + key);
                }
                return;
            }
        }
    }
}