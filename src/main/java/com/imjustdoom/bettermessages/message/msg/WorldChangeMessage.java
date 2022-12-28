package com.imjustdoom.bettermessages.message.msg;

import com.imjustdoom.bettermessages.BetterMessages;
import com.imjustdoom.bettermessages.manager.PlayerManager;
import com.imjustdoom.bettermessages.message.Message;
import org.bukkit.Statistic;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerChangedWorldEvent;

import java.util.List;

public class WorldChangeMessage extends Message {

    public WorldChangeMessage(String parent, List<String> message, List<String> commands, List<Integer> count, boolean permission, boolean enabled, String audience, String storageType, String dontRunIf, long delay, int priority) {
        super(parent, message, commands, count, permission, enabled, audience, storageType, dontRunIf, delay, priority);
    }

    public WorldChangeMessage(String parent, List<String> message, List<String> commands, List<Integer> count, boolean permission, boolean enabled, String audience, String storageType, String dontRunIf, long delay, int priority, String extraInfo) {
        super(parent, message, commands, count, permission, enabled, audience, storageType, dontRunIf, delay, priority, extraInfo);
    }

    @Override
    public boolean otherChecks(Player player, Event event) {
        PlayerChangedWorldEvent worldEvent = (PlayerChangedWorldEvent) event;

        if (getExtraInfo() != null) {
            String from = getExtraInfo().split("/")[0];
            String to = getExtraInfo().split("/")[1];

            if (!from.equalsIgnoreCase(worldEvent.getFrom().getName()) || !player.getWorld().getName().equalsIgnoreCase(to))
                return false;
        }

        if (PlayerManager.waiting.containsKey(player.getUniqueId()) && getDontRunIf().equalsIgnoreCase(PlayerManager.waiting.get(player.getUniqueId()).getName())) {
            PlayerManager.removeWaitingPlayer(player.getUniqueId());
            return false;
        }

        return true;
    }

    @Override
    public boolean getCount(Player player) {
        return getCount().contains(BetterMessages.getInstance().getStorage().getCount(player.getUniqueId(), getParent().replace("-", "_"))) || getCount().contains(-1);
    }
}
