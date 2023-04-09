package com.imjustdoom.bettermessages.old.message;

import com.imjustdoom.bettermessages.player.BMPlayer;

public interface MessageType {
    void send(BMPlayer player, String message);
}