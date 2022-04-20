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
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public class PlayerWorldChange implements Listener {

    @EventHandler(priority = EventPriority.LOWEST)
    public void WorldChangeEvent(PlayerChangedWorldEvent event) {
        Player player = event.getPlayer();

        PlayerManager.removePlayer(player.getUniqueId());

        if (VanishUtil.isVanished(player)
                || player.hasPermission("bettermessages.silent-world-change")) return;

        for (Message msg : Config.MESSAGES) {

            if (!msg.isEnabled()) continue;

            Bukkit.getScheduler().scheduleAsyncDelayedTask(BetterMessages.getInstance(), () -> {

                if(msg.getDontRunIf().equalsIgnoreCase(PlayerManager.waiting.get(player.getUniqueId()).getName())) {
                    PlayerManager.waiting.remove(player.getUniqueId());
                    return;
                }

                String from = "", to = "";

                for (String activation : msg.getActivation()) {
                    if (!activation.startsWith("world-change")) {
                        return;
                    } else {
                        from = activation.split("/")[1];
                        to = activation.split("/")[2];
                    }
                }

                if (!from.equalsIgnoreCase(event.getFrom().getName()) || !player.getWorld().getName().equalsIgnoreCase(to))
                    return;

                BetterMessages.getInstance().getStorage().update(player.getUniqueId(), msg.getParent());

                if (msg.isPermission() && !player.hasPermission(msg.getPermissionString())) return;

                if (!msg.getCount().contains(BetterMessages.getInstance().getStorage().getCount(player.getUniqueId(),
                        msg.getParent().replace("-", "_")))
                        && !msg.getCount().contains(-1)) {
                    return;
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