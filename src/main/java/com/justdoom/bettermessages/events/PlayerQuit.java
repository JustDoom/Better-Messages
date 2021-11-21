package com.justdoom.bettermessages.events;


import com.justdoom.bettermessages.BetterMessages;
import com.justdoom.bettermessages.util.MessageUtil;
import com.justdoom.bettermessages.util.VanishUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerQuit implements Listener {

    @EventHandler
    public void QuitEvent(org.bukkit.event.player.PlayerQuitEvent event) {
        Player player = event.getPlayer();

        String msg = MessageUtil.doMessage(player, "quit", BetterMessages.getInstance());
        if (BetterMessages.getInstance().getConfig().getBoolean("quit.enabled")) {
            event.setQuitMessage(null);

            // check after quit message null
            if(VanishUtil.isVanished(player) || player.hasPermission("bettermessages.silent-quit")){
                return;
            }

            for(Player p: Bukkit.getOnlinePlayers()) {
                if(BetterMessages.getInstance().getConfig().getString("quit.permission").equalsIgnoreCase("none")
                        || p.hasPermission(BetterMessages.getInstance().getConfig().getString("quit.permission")))
                    MessageUtil.messageType(p, msg, BetterMessages.getInstance(), "quit");
            }
        }
    }
}

