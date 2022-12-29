package com.imjustdoom.bettermessages.message.msg;

import com.imjustdoom.bettermessages.BetterMessages;
import com.imjustdoom.bettermessages.message.Message;
import org.bukkit.Statistic;
import org.bukkit.entity.Player;

import java.util.List;

public class JoinMessage extends Message {

    public JoinMessage(String parent, List<String> message, List<String> commands, List<Integer> count, boolean permission, boolean enabled, String audience, String storageType, String dontRunIf, long delay, int priority) {
        super(parent, message, commands, count, permission, enabled, audience, storageType, dontRunIf, delay, priority);
    }

    public JoinMessage(String parent, List<String> message, List<String> commands, List<Integer> count, boolean permission, boolean enabled, String audience, String storageType, String dontRunIf, long delay, int priority, String extraInfo) {
        super(parent, message, commands, count, permission, enabled, audience, storageType, dontRunIf, delay, priority, extraInfo);
    }

    @Override
    public int getCount(Player player) {
        return getStorageType().equals("default") ? BetterMessages.getInstance().getStorage().getCount(player.getUniqueId(), getParent()) : player.getStatistic(Statistic.RECORD_PLAYED) + 1; // TODO: make sure the statistics all work
    }
}
