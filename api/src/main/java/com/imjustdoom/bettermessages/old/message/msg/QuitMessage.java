package com.imjustdoom.bettermessages.old.message.msg;

import com.imjustdoom.bettermessages.BetterMessages;
import com.imjustdoom.bettermessages.old.message.Message;
import org.bukkit.Statistic;
import org.bukkit.entity.Player;

import java.util.List;

public class QuitMessage extends Message {

    public QuitMessage(String parent, List<String> message, List<String> commands, List<Integer> count, boolean permission, boolean enabled, String audience, String storageType, String dontRunIf, long delay, int priority, String messageType) {
        super(parent, message, commands, count, permission, enabled, audience, storageType, dontRunIf, delay, priority, messageType);
    }

    public QuitMessage(String parent, List<String> message, List<String> commands, List<Integer> count, boolean permission, boolean enabled, String audience, String storageType, String dontRunIf, long delay, int priority, String messageType, String extraInfo) {
        super(parent, message, commands, count, permission, enabled, audience, storageType, dontRunIf, delay, priority, messageType, extraInfo);
    }

//    @Override
//    public int getCount(BMPlayer player) {
//        return getStorageType().equals("default") ? BetterMessages.getInstance().getStorage().getCount(player.getUniqueId(), getParent()) : player.getStatistic(Statistic.LEAVE_GAME); // TODO: make sure the statistics all work
//    }
}
