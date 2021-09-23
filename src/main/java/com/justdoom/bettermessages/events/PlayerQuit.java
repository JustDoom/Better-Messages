package com.justdoom.bettermessages.events;


import com.justdoom.bettermessages.BetterMessages;
import com.justdoom.bettermessages.util.VanishUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerQuit implements Listener {
    private BetterMessages plugin;

    public PlayerQuit(BetterMessages plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void QuitEvent(org.bukkit.event.player.PlayerQuitEvent event) {
        Player player = event.getPlayer();

        String msg = plugin.handler.doMessage(player, "quit", plugin);
        if (plugin.getConfig().getBoolean("quit.enabled")) {
            event.setQuitMessage(null);

            // check after quit message null
            if(VanishUtil.isVanished(player) || player.hasPermission("bettermessages.silent-quit")){
                return;
            }

            for(Player p: Bukkit.getOnlinePlayers()) {
                if(plugin.getConfig().getString("quit.permission").equalsIgnoreCase("none")
                        || p.hasPermission(plugin.getConfig().getString("quit.permission")))
                    plugin.handler.messageType(p, msg, plugin, "quit");
            }
        }
    }
}

