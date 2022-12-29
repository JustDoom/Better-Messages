package com.imjustdoom.bettermessages.message;

import com.imjustdoom.bettermessages.BetterMessages;
import com.imjustdoom.bettermessages.message.type.ChatMessageType;
import com.imjustdoom.bettermessages.util.MessageUtil;
import lombok.Getter;
import lombok.Setter;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Statistic;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Getter
@Setter
public class Message {

    private String parent;
    private List<String> message, commands;
    private List<Integer> count;
    private boolean permission, enabled;
    private String audience, storageType, dontRunIf, permissionString, extraInfo;
    private long delay;
    private int priority;
    private MessageType messageType;

    // TODO: all actual message stuff will be handled in the MessageType and all checking if it can run etc will be in this class
    // makes it easier and simpler to add new message types etc
    public Message(String parent, List<String> message, List<String> commands, List<Integer> count, boolean permission, boolean enabled, String audience, String storageType, String dontRunIf, long delay, int priority, String messageType) {
        this.parent = parent;
        this.message = message;
        this.commands = commands;
        this.count = count;
        this.permission = permission;
        this.enabled = enabled;
        this.audience = audience;
        this.storageType = storageType;
        this.dontRunIf = dontRunIf;
        this.permissionString = "bettermessages." + this.parent;
        this.delay = delay;
        this.priority = priority;
        this.extraInfo = null;

        if (messageType.equalsIgnoreCase("title")) {
            this.messageType = new ChatMessageType();
        } else if (messageType.equalsIgnoreCase("actionbar")) {
            this.messageType = new ChatMessageType();
        } else if (messageType.equalsIgnoreCase("bossbar")) {
            this.messageType = new ChatMessageType();
        } else {
            this.messageType = new ChatMessageType();
        }
    }

    public Message(String parent, List<String> message, List<String> commands, List<Integer> count, boolean permission, boolean enabled, String audience, String storageType, String dontRunIf, long delay, int priority, String extraInfo, String messageType) {
        this.parent = parent;
        this.message = message;
        this.commands = commands;
        this.count = count;
        this.permission = permission;
        this.enabled = enabled;
        this.audience = audience;
        this.storageType = storageType;
        this.dontRunIf = dontRunIf;
        this.permissionString = "bettermessages." + this.parent;
        this.delay = delay;
        this.priority = priority;
        this.extraInfo = extraInfo;
    }


    public String getMessage() {
        if (message.size() == 1) return message.get(0);
        return message.get((new Random()).nextInt(message.size()));
    }

    public boolean canRun(Player player, Event event) {
        if (!isEnabled()) return false;
        if (isPermission() && !player.hasPermission(getPermissionString())) return false;
        if (!count(getCount(player))) return false;
        if (!otherChecks(player, event)) return false;

        return true;
    }

    public boolean otherChecks(Player player, Event event) {
        return true;
    }

    public int getCount(Player player) {
        return -1;
    }

    public boolean count(int count) {
        return getCount().contains(count) || getCount().contains(-1);
    }

    public void sendMessage(Player player) {
        Bukkit.getScheduler().scheduleAsyncDelayedTask(BetterMessages.getInstance(), () -> {

            String tempMsg = BetterMessages.getInstance().getStorage().getMessage(player.getUniqueId(), getParent()).equals("") ? getMessage() : BetterMessages.getInstance().getStorage().getMessage(player.getUniqueId(), getParent());
            String message = translatePlaceholders(tempMsg, player);

            for (String command : getCommands())
                Bukkit.getScheduler().scheduleSyncDelayedTask(BetterMessages.getInstance(), () -> Bukkit.dispatchCommand(Bukkit.getConsoleSender(), translatePlaceholders(command, player)));

            boolean ignoreUser = getAudience().contains("ignore-user");
            String audience = ignoreUser ? getAudience().split("\\|")[0] : getAudience();

            switch (audience) {
                case "server":
                    for (Player p : Bukkit.getOnlinePlayers()) {
                        if (ignoreUser && p.getUniqueId().equals(player.getUniqueId())) continue;
                        messageType.send(p, message);
                    }
                    break;
                case "world":
                    for (Player p : player.getWorld().getPlayers()) {
                        if (ignoreUser && p.getUniqueId().equals(player.getUniqueId())) continue;
                        messageType.send(p, message);
                    }
                    break;
                case "user":
                    messageType.send(player, message);
                    break;
                default:
                    if (!getAudience().startsWith("world/")) break;
                    for (Player p : Bukkit.getWorld(audience.replace("world/", "")).getPlayers()) {
                        if (ignoreUser && p.getUniqueId().equals(player.getUniqueId())) continue;
                        messageType.send(p, message);
                    }
            }
        }, getDelay());
    }

    private String translatePlaceholders(String message, Player player) {
        message = message.replace("{player}", player.getName());
        message = message.replace("{world}", player.getWorld().getName());
        message = message.replace("{line}", "\n");
        message = message.replace("{stat}", String.valueOf(getCount(player)));

        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null)
            message = PlaceholderAPI.setPlaceholders(player, message);

        return MessageUtil.translate(message);
    }
}
