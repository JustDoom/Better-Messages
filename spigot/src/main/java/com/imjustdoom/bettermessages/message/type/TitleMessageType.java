package com.imjustdoom.bettermessages.message.type;

import com.imjustdoom.bettermessages.message.MessageType;
import org.bukkit.entity.Player;

public class TitleMessageType implements MessageType {

    public void send(Player player, String message) {
        player.sendTitle(message, "bottom", 10, 40, 10); // TODO: need to finish this
    }
}