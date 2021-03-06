package com.justdoom.bettermessages.events;


import com.justdoom.bettermessages.BetterMessages;
import java.sql.SQLException;
import java.util.UUID;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class JoinMessage implements Listener {
    private final BetterMessages plugin;

    public JoinMessage(BetterMessages plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void JoinEvent(PlayerJoinEvent event) throws SQLException {
        Player player = event.getPlayer();
        String msg = this.plugin.handler.doMessage(player, "join", (JavaPlugin)this.plugin);
        String firstmsg = this.plugin.handler.doMessage(player, "join.first-join", (JavaPlugin)this.plugin);
        if (this.plugin.getConfig().getBoolean("join.enabled")) {
            event.setJoinMessage(msg);
        } else if (this.plugin.getConfig().getBoolean("join.first-join.enabled")) {
            event.setJoinMessage(firstmsg);
        }
        UUID uuid2 = player.getUniqueId();
    }
}
