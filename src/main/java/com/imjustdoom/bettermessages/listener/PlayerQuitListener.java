package com.imjustdoom.bettermessages.listener;


import com.imjustdoom.bettermessages.BetterMessages;
import com.imjustdoom.bettermessages.config.Config;
import com.imjustdoom.bettermessages.manager.PlayerManager;
import com.imjustdoom.bettermessages.message.EventType;
import com.imjustdoom.bettermessages.message.Message;
import com.imjustdoom.bettermessages.util.MessageUtil;
import com.imjustdoom.bettermessages.util.VanishUtil;
import org.bukkit.Bukkit;
import org.bukkit.Statistic;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerQuitListener implements Listener {

    @EventHandler(priority = EventPriority.LOWEST)
    public void quitEvent(PlayerQuitEvent event) {

        Player player = event.getPlayer();

        PlayerManager.removePlayer(player.getUniqueId());

        if (VanishUtil.isVanished(player) || player.hasPermission("bettermessages.silent-quit")) return;

        Message pMessage = null;

        for (Message msg : Config.MESSAGES.get(EventType.QUIT)) {

            if (!msg.isEnabled()) continue;

            BetterMessages.getInstance().getStorage().update(player.getUniqueId(), msg.getParent());

            if (msg.isPermission() && !player.hasPermission(msg.getPermissionString())) continue;

            int count = msg.getStorageType().equals("default")
                    ? BetterMessages.getInstance().getStorage().getCount(player.getUniqueId(), msg.getParent())
                    : player.getStatistic(Statistic.LEAVE_GAME);

            if ((!msg.getCount().contains(count) && !msg.getCount().contains(-1))) continue;

//            if (msg.getPriority() != -1) {
//                if (pMessage == null) {
//                    pMessage = msg;
//                    continue;
//                }
//                if (msg.getPriority() < pMessage.getPriority())
//                    pMessage = msg;
//                continue;
//            }

            event.setQuitMessage(null);
            sendMessage(player, msg);
        }
//        if (pMessage != null) {
//            event.setQuitMessage(null);
//            sendMessage(player, pMessage);
//        }
    }

    private void sendMessage(Player player, Message msg) {
        Bukkit.getScheduler().scheduleAsyncDelayedTask(BetterMessages.getInstance(), () -> {

            String tempMsg = BetterMessages.getInstance().getStorage().getMessage(player.getUniqueId(), msg.getParent()).equals("") ? msg.getMessage() : BetterMessages.getInstance().getStorage().getMessage(player.getUniqueId(), msg.getParent());
            String message = MessageUtil.translatePlaceholders(tempMsg, player);

            for (String command : msg.getCommands())
                Bukkit.getScheduler().scheduleSyncDelayedTask(BetterMessages.getInstance(), () -> Bukkit.dispatchCommand(Bukkit.getConsoleSender(), MessageUtil.translatePlaceholders(command, player)));

            boolean ignoreUser = msg.getAudience().contains("ignore-user");
            String audience = ignoreUser ? msg.getAudience().split("\\|")[0] : msg.getAudience();

            switch (audience) {
                case "server":
                    for (Player p : Bukkit.getOnlinePlayers()) {
                        if (ignoreUser && p.getUniqueId().equals(player.getUniqueId())) continue;
                        p.sendMessage(message);
                    }
                    break;
                case "world":
                    for (Player p : player.getWorld().getPlayers()) {
                        if (ignoreUser && p.getUniqueId().equals(player.getUniqueId())) continue;
                        p.sendMessage(message);
                    }
                    break;
                case "user":
                    player.sendMessage(message);
                    break;
                default:
                    if (!msg.getAudience().startsWith("world/")) break;
                    for (Player p : Bukkit.getWorld(audience.replace("world/", "")).getPlayers()) {
                        if (ignoreUser && p.getUniqueId().equals(player.getUniqueId())) continue;
                        p.sendMessage(message);
                    }
            }
        }, msg.getDelay());
    }
}

