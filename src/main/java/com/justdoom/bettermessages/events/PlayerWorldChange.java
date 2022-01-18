package com.justdoom.bettermessages.events;

import com.justdoom.bettermessages.BetterMessages;
import com.justdoom.bettermessages.config.Config;
import com.justdoom.bettermessages.manager.PlayerManager;
import com.justdoom.bettermessages.message.Message;
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

        PlayerManager.removePlayer(player.getUniqueId());

        if (VanishUtil.isVanished(player)
                || player.hasPermission("bettermessages.silent-world-change")) return;

        MESSAGES:
        for (Message msg : Config.MESSAGES) {

            if(!msg.isEnabled()) continue;

            String from = "", to = "";

            for(String activation : msg.getActivation()) {
                if(!activation.startsWith("world-change")) {
                    continue MESSAGES;
                } else {
                    from = activation.split("/")[1];
                    to = activation.split("/")[2];
                }
            }

            if(!from.equalsIgnoreCase(event.getFrom().getName()) || !player.getWorld().getName().equalsIgnoreCase(to)) continue;

            BetterMessages.getInstance().getStorage().update(player.getUniqueId(), msg.getParent());

            if (!msg.getPermission().equals("none") && !player.hasPermission(msg.getPermission())) continue;

            if (!msg.getCount().contains(BetterMessages.getInstance().getStorage().getCount(player.getUniqueId(), msg.getParent().replace("-", "_"))) && !msg.getCount().contains(-1)) {
                continue;
            }

            String message = MessageUtil.translate(msg.getMessage(), player);

            switch (msg.getAudience()) {
                case "server":
                    for (Player p : Bukkit.getOnlinePlayers()) {
                        p.sendMessage(message);
                    }
                    break;
                case "world":
                    for (Player p : player.getWorld().getPlayers()) {
                        p.sendMessage(message);
                    }
                    break;
                case "user":
                    player.sendMessage(message);
                    break;
                default:
                    if (!msg.getAudience().startsWith("world/")) {
                        break;
                    }

                    for (Player p : Bukkit.getWorld(msg.getAudience().replace("world/", "")).getPlayers()) {
                        p.sendMessage(message);
                    }
            }
        }

        /**
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
        }**/
    }
}