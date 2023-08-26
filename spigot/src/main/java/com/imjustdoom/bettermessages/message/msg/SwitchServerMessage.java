package com.imjustdoom.bettermessages.message.msg;

import com.imjustdoom.bettermessages.BetterMessages;
import com.imjustdoom.bettermessages.listener.event.ServerSwitchEvent;
import com.imjustdoom.bettermessages.manager.PlayerManager;
import com.imjustdoom.bettermessages.message.Message;
import org.bukkit.Statistic;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

import java.util.List;

public class SwitchServerMessage extends Message {

    public SwitchServerMessage(String parent, List<String> message, List<String> commands, List<Integer> count, boolean permission, boolean enabled, String audience, String storageType, String dontRunIf, long delay, int priority, String messageType) {
        super(parent, message, commands, count, permission, enabled, audience, storageType, dontRunIf, delay, priority, messageType);
    }

    public SwitchServerMessage(String parent, List<String> message, List<String> commands, List<Integer> count, boolean permission, boolean enabled, String audience, String storageType, String dontRunIf, long delay, int priority, String messageType, String extraInfo) {
        super(parent, message, commands, count, permission, enabled, audience, storageType, dontRunIf, delay, priority, messageType, extraInfo);
    }

    @Override
    public int getCount(Player player) {
        return getStorageType().equals("default") ? BetterMessages.getInstance().getStorage().getCount(player.getUniqueId(), getParent()) : player.getStatistic(Statistic.RECORD_PLAYED) + 1; // TODO: make sure the statistics all work
    }

    @Override
    public boolean otherChecks(Player player, Event event) {
        ServerSwitchEvent switchEvent = (ServerSwitchEvent) event;

        // Check for specific world change settings
        if (getExtraInfo() != null) {
            String from = getExtraInfo().split("/")[0];
            String to = getExtraInfo().split("/")[1];

            if (!from.equalsIgnoreCase(switchEvent.getFrom()) || !switchEvent.getTo().equalsIgnoreCase(to)) {
                return false;
            }
        }

        if (PlayerManager.waiting.containsKey(player.getUniqueId()) && getDontRunIf().equalsIgnoreCase(PlayerManager.waiting.get(player.getUniqueId()).getName())) {
            PlayerManager.removeWaitingPlayer(player.getUniqueId());
            return false;
        }

        return true;
    }

    @Override
    public String translateCustomPlaceholders(String message) {
        return message.replace("{from}", getExtraInfo().split("/")[0]).replace("{to}", getExtraInfo().split("/")[1]);
    }
}