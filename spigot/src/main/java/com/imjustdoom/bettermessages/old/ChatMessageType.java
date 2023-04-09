package com.imjustdoom.bettermessages.old;

import com.imjustdoom.bettermessages.old.message.MessageType;
import com.imjustdoom.bettermessages.player.BMPlayer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class ChatMessageType implements MessageType {

    public void send(BMPlayer bmPlayer, String message) {
        Player player = Bukkit.getPlayer(bmPlayer.getUuid());
        player.sendMessage(message);
    }
}