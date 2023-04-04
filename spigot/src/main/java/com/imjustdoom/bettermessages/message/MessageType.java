package com.imjustdoom.bettermessages.message;

import org.bukkit.entity.Player;

public interface MessageType {
    void send(Player player, String message);
}