package com.imjustdoom.bettermessages.old.message;

import com.imjustdoom.bettermessages.player.BMPlayer;

public interface Message {

    String getMessage();

    boolean canRun(BMPlayer player, Event event);

    boolean otherChecks(BMPlayer player, Event event);

    int getCount(BMPlayer player);

    boolean hasValidCount(int count);

    String translateCustomPlaceholders(String message);

    void sendMessage(BMPlayer player);

    String translatePlaceholders(String message, BMPlayer player);
}
