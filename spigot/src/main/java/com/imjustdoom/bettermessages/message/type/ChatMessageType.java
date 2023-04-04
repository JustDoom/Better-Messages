package com.imjustdoom.bettermessages.message.type;

import com.imjustdoom.bettermessages.message.MessageType;
import org.bukkit.entity.Player;

public class ChatMessageType implements MessageType {

    public void send(Player player, String message) {
        player.sendMessage(message);
    }
}