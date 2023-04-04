package com.imjustdoom.bettermessages.listener;

import com.imjustdoom.bettermessages.BetterMessages;
import com.imjustdoom.bettermessages.config.Config;
import com.imjustdoom.bettermessages.listener.event.ServerSwitchEvent;
import com.imjustdoom.bettermessages.manager.PlayerManager;
import com.imjustdoom.bettermessages.message.EventType;
import com.imjustdoom.bettermessages.message.Message;
import com.imjustdoom.bettermessages.util.VanishUtil;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;

public class BungeeCordListener implements PluginMessageListener {

    @Override
    public void onPluginMessageReceived(String channel, Player player, byte[] message) {
        if (!channel.equals("BetterMessages")) {
            return;
        }

        DataInputStream in = new DataInputStream(new ByteArrayInputStream(message));
        try {
            String subChannel = in.readUTF();
            if (subChannel.equals("ServerSwitchEvent")) {
                String fromServer = in.readUTF();
                String toServer = in.readUTF();

                // fire event
                ServerSwitchEvent event = new ServerSwitchEvent(player, fromServer, toServer);
                BetterMessages.getInstance().getServer().getPluginManager().callEvent(event);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
