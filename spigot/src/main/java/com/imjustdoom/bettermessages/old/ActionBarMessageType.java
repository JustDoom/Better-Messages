package com.imjustdoom.bettermessages.old;

import com.imjustdoom.bettermessages.old.message.MessageType;
import com.imjustdoom.bettermessages.player.BMPlayer;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class ActionBarMessageType implements MessageType {

    public void send(BMPlayer bmPlayer, String message) {
        Player player = Bukkit.getPlayer(bmPlayer.getUuid());
        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(message));
    }
}