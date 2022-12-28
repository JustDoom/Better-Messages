package com.imjustdoom.bettermessages.message;

import com.imjustdoom.bettermessages.BetterMessages;
import com.imjustdoom.bettermessages.util.MessageUtil;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.Statistic;
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
    private String audience, storageType, dontRunIf, permissionString, extraInfo;
    private long delay;
    private int priority;

    public Message(String parent, List<String> message, List<String> commands, List<Integer> count, boolean permission, boolean enabled, String audience, String storageType, String dontRunIf, long delay, int priority) {
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
    }

    public Message(String parent, List<String> message, List<String> commands, List<Integer> count, boolean permission, boolean enabled, String audience, String storageType, String dontRunIf, long delay, int priority, String extraInfo) {
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
        if (!getCount(player)) return false;
        if (!otherChecks(player, event)) return false;

        return true;
    }

    public boolean otherChecks(Player player, Event event) {
        return true;
    }

    public boolean getCount(Player player) {
        return true;
    }

    public void sendMessage(Player player) {
        Bukkit.getScheduler().scheduleAsyncDelayedTask(BetterMessages.getInstance(), () -> {

            String tempMsg = BetterMessages.getInstance().getStorage().getMessage(player.getUniqueId(), getParent()).equals("") ? getMessage() : BetterMessages.getInstance().getStorage().getMessage(player.getUniqueId(), getParent());
            String message = MessageUtil.translatePlaceholders(tempMsg, player);

            for (String command : getCommands())
                Bukkit.getScheduler().scheduleSyncDelayedTask(BetterMessages.getInstance(), () -> Bukkit.dispatchCommand(Bukkit.getConsoleSender(), MessageUtil.translatePlaceholders(command, player)));

            boolean ignoreUser = getAudience().contains("ignore-user");
            String audience = ignoreUser ? getAudience().split("\\|")[0] : getAudience();

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
                    if (!getAudience().startsWith("world/")) break;
                    for (Player p : Bukkit.getWorld(audience.replace("world/", "")).getPlayers()) {
                        if (ignoreUser && p.getUniqueId().equals(player.getUniqueId())) continue;
                        p.sendMessage(message);
                    }
            }
        }, getDelay());
    }
}
