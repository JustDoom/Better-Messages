package com.imjustdoom.bettermessages.message;

import com.imjustdoom.bettermessages.BetterMessages;
import com.imjustdoom.bettermessages.message.type.ActionBarMessageType;
import com.imjustdoom.bettermessages.message.type.ChatMessageType;
import com.imjustdoom.bettermessages.message.type.TitleMessageType;
import com.imjustdoom.bettermessages.util.MessageUtil;
import lombok.Getter;
import lombok.Setter;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

import java.util.List;
import java.util.Random;

@Getter
@Setter
public class Message {

    private String parent;
    private List<String> message, commands;
    private List<Integer> count;
    private boolean permission, enabled;
    private String storageType, dontRunIf, permissionString, extraInfo;
    private List<String> audience;
    private long delay;
    private int priority;
    private MessageType messageType;

    // TODO: all actual message stuff will be handled in the MessageType and all checking if it can run etc will be in this class
    // makes it easier and simpler to add new message types etc
    public Message(String parent, List<String> message, List<String> commands, List<Integer> count, boolean permission, boolean enabled, List<String> audience, String storageType, String dontRunIf, long delay, int priority, String messageType) {
        this(parent, message, commands, count, permission, enabled, audience, storageType, dontRunIf, delay, priority, messageType, null);
    }

    public Message(String parent, List<String> message, List<String> commands, List<Integer> count, boolean permission, boolean enabled, List<String> audience, String storageType, String dontRunIf, long delay, int priority, String messageType, String extraInfo) {
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

        if (messageType.equalsIgnoreCase("title")) {
            this.messageType = new TitleMessageType();
        } else if (messageType.equalsIgnoreCase("actionbar")) {
            this.messageType = new ActionBarMessageType();
        } else if (messageType.equalsIgnoreCase("bossbar")) {
            //this.messageType = new ChatMessageType();
        } else {
            this.messageType = new ChatMessageType();
        }
    }

    public String getMessage() {
        if (message.size() == 1) return message.get(0);
        return message.get((new Random()).nextInt(message.size()));
    }

    public boolean canRun(Player player, Event event) {
        if (!isEnabled()) {
            return false;
        }
        if (isPermission() && !player.hasPermission(getPermissionString())) {
            return false;
        }
        if (!hasValidCount(getCount(player))) {
            return false;
        }
        if (event != null && !otherChecks(player, event)) {
            return false;
        }

        return true;
    }

    public boolean otherChecks(Player player, Event event) {
        return true;
    }

    public int getCount(Player player) {
        return -1;
    }

    public boolean hasValidCount(int count) {
        return getCount().contains(count) || getCount().contains(-1);
    }

    public String translateCustomPlaceholders(String message) {
        return message;
    }

    public void sendMessage(Player player) {

        // This is used to add a delay, maybe try figure a better way to add a delay
        Bukkit.getScheduler().scheduleAsyncDelayedTask(BetterMessages.getInstance(), () -> {

            String tempMsg = BetterMessages.getInstance().getStorage().getMessage(player.getUniqueId(), getParent()).equals("") ? getMessage() : BetterMessages.getInstance().getStorage().getMessage(player.getUniqueId(), getParent());
            String message = translatePlaceholders(tempMsg, player);

            for (String command : getCommands()) {
                Bukkit.getScheduler().scheduleSyncDelayedTask(BetterMessages.getInstance(), () -> {
                        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), translatePlaceholders(command, player));
                });
            }

            for (String audience : getAudience()) {
                String[] split = audience.split("\\|");
                boolean ignoreUser = false;
                String permission = null;
                for (String s : split) {
                    if (s.equalsIgnoreCase("ignore-user")) ignoreUser = true;
                    if (s.contains("permission:")) {
                        String perm = s.split(":")[1];
                        if (player.hasPermission(perm)) {
                            permission = perm;
                        }
                    }
                }

                String a = ignoreUser ? audience.split("\\|")[0] : audience;

                // TODO: Make a better way to get the users to send the message to
                switch (a) {
                    case "server":
                        for (Player p : Bukkit.getOnlinePlayers()) {
                            if ((ignoreUser && p.getUniqueId().equals(player.getUniqueId())) || (permission != null && !p.hasPermission(permission))) {
                                continue;
                            }
                            messageType.send(p, message);
                        }
                        break;
                    case "world":
                        if (audience.startsWith("world/")) {
                            for (Player p : Bukkit.getWorld(a.replace("world/", "")).getPlayers()) {
                                if ((ignoreUser && p.getUniqueId().equals(player.getUniqueId())) || (permission != null && !p.hasPermission(permission))) {
                                    continue;
                                }
                                messageType.send(p, message);
                            }
                        }
                        for (Player p : player.getWorld().getPlayers()) {
                            if ((ignoreUser && p.getUniqueId().equals(player.getUniqueId())) || (permission != null && !p.hasPermission(permission))) {
                                continue;
                            }
                            messageType.send(p, message);
                        }
                        break;
                    case "user":
                        if (permission != null && !player.hasPermission(permission)) break;
                        this.messageType.send(player, message);
                        break;
                    default:
                        System.err.println("Unknown audience type");
                }
            }
        }, getDelay());
    }

    private String translatePlaceholders(String message, Player player) {
        message = translateCustomPlaceholders(message);
        message = message.replace("{player}", player.getName())
                .replace("{world}", player.getWorld().getName())
                .replace("{line}", "\n")
                .replace("{stat}", String.valueOf(getCount(player)));

        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            message = PlaceholderAPI.setPlaceholders(player, message);
        }

        return MessageUtil.translate(message);
    }
}
