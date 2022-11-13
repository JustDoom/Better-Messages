package com.imjustdoom.bettermessages.listener;

import com.imjustdoom.bettermessages.BetterMessages;
import com.imjustdoom.bettermessages.config.Config;
import com.imjustdoom.bettermessages.manager.PlayerManager;
import com.imjustdoom.bettermessages.message.EventType;
import com.imjustdoom.bettermessages.message.Message;
import com.imjustdoom.bettermessages.util.MessageUtil;
import com.imjustdoom.bettermessages.util.VanishUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;

public class PlayerWorldChangeListener implements Listener {

    @EventHandler(priority = EventPriority.LOWEST)
    public void worldChangeEvent(PlayerChangedWorldEvent event) {
        Player player = event.getPlayer();

        PlayerManager.removePlayer(player.getUniqueId());

        if (VanishUtil.isVanished(player) || player.hasPermission("bettermessages.silent-world-change")) return;

        for (Message msg : Config.MESSAGES.get(EventType.WORLD_CHANGE)) {

            if (!msg.isEnabled()) continue;

            Bukkit.getScheduler().scheduleAsyncDelayedTask(BetterMessages.getInstance(), () -> {

                if (PlayerManager.waiting.containsKey(player.getUniqueId()) && msg.getDontRunIf().equalsIgnoreCase(PlayerManager.waiting.get(player.getUniqueId()).getName())) {
                    PlayerManager.removeWaitingPlayer(player.getUniqueId());
                    return;
                }

                if (msg.getExtraInfo() != null) {
                    String from = msg.getExtraInfo().split("/")[0];
                    String to = msg.getExtraInfo().split("/")[1];

                    if (!from.equalsIgnoreCase(event.getFrom().getName()) || !player.getWorld().getName().equalsIgnoreCase(to))
                        return;
                }

                BetterMessages.getInstance().getStorage().update(player.getUniqueId(), msg.getParent());

                if (msg.isPermission() && !player.hasPermission(msg.getPermissionString())) return;
                if (!msg.getCount().contains(BetterMessages.getInstance().getStorage().getCount(player.getUniqueId(), msg.getParent().replace("-", "_"))) && !msg.getCount().contains(-1))
                    return;

                String tempMsg = BetterMessages.getInstance().getStorage().getMessage(player.getUniqueId(), msg.getParent()).equals("")
                        ? msg.getMessage() : BetterMessages.getInstance().getStorage().getMessage(player.getUniqueId(), msg.getParent());
                String message = MessageUtil.translatePlaceholders(tempMsg, player);

                for (String command : msg.getCommands())
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), MessageUtil.translatePlaceholders(command, player));

                switch (msg.getAudience()) {
                    case "server":
                        for (Player p : Bukkit.getOnlinePlayers()) p.sendMessage(message);
                        break;
                    case "world":
                        for (Player p : player.getWorld().getPlayers()) p.sendMessage(message);
                        break;
                    case "user":
                        player.sendMessage(message);
                        break;
                    default:
                        if (!msg.getAudience().startsWith("world/")) break;
                        for (Player p : Bukkit.getWorld(msg.getAudience().replace("world/", "")).getPlayers())
                            p.sendMessage(message);
                }
            }, msg.getDelay());
        }
    }
}