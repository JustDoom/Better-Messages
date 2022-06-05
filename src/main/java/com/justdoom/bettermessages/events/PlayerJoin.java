package com.justdoom.bettermessages.events;


import com.justdoom.bettermessages.BetterMessages;

import com.justdoom.bettermessages.config.Config;
import com.justdoom.bettermessages.manager.PlayerManager;
import com.justdoom.bettermessages.message.Message;
import com.justdoom.bettermessages.util.MessageUtil;
import com.justdoom.bettermessages.util.VanishUtil;
import org.bukkit.Bukkit;
import org.bukkit.Statistic;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;

public class PlayerJoin implements Listener {

    @EventHandler(priority = EventPriority.HIGH)
    public void JoinEvent(PlayerJoinEvent event) {

        Player player = event.getPlayer();
        PlayerManager.addWaitingPlayer(player.getUniqueId(), event.getPlayer().getWorld());

        PlayerManager.removePlayer(player.getUniqueId());

        if (VanishUtil.isVanished(player)
                || player.hasPermission("bettermessages.silent-join")) return;

        for (Message msg : Config.MESSAGES) {

            if (!msg.getActivation().contains("join") || !msg.isEnabled()) continue;

            BetterMessages.getInstance().getStorage().update(player.getUniqueId(), msg.getParent());

            if (msg.isPermission() && !player.hasPermission(msg.getPermissionString())) continue;

            int count = msg.getStorageType().equals("default")
                    ? BetterMessages.getInstance().getStorage().getCount(player.getUniqueId(), msg.getParent())
                    : player.getStatistic(Statistic.LEAVE_GAME) + 1;

            if ((!msg.getCount().contains(count)
                    && !msg.getCount().contains(-1))) {
                continue;
            }

            event.setJoinMessage(null);

            Bukkit.getScheduler().scheduleAsyncDelayedTask(BetterMessages.getInstance(), () -> {

                if(BetterMessages.getInstance().getStorage().getMessage(player.getUniqueId(), msg.getParent()) == null) {
                    File file = new File(BetterMessages.getInstance().getDataFolder() + "/data", player.getUniqueId() + ".yml");
                    FileConfiguration config = YamlConfiguration.loadConfiguration(file);

                    config.set("messages." + msg.getParent(), "");
                }

                String tempMsg = BetterMessages.getInstance().getStorage().getMessage(player.getUniqueId(), msg.getParent()).equals("")
                        ? msg.getMessage() : BetterMessages.getInstance().getStorage().getMessage(player.getUniqueId(), msg.getParent());
                String message = MessageUtil.translatePlaceholders(tempMsg, player);

                for (String command : msg.getCommands()) {
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), MessageUtil.translatePlaceholders(command, player));
                }

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
            }, msg.getDelay());
        }
    }
}