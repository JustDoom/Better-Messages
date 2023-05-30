package com.imjustdoom.bettermessages.old.listener;

import com.imjustdoom.bettermessages.BetterMessages;
import com.imjustdoom.bettermessages.old.listener.event.ServerSwitchEvent;
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
