package com.imjustdoom.bettermessages.listener;

import com.imjustdoom.bettermessages.BetterMessages;
import com.imjustdoom.bettermessages.listener.event.ServerSwitchEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;

public class BungeeCordListener implements PluginMessageListener {

    private int test = 0;

    @Override
    public void onPluginMessageReceived(String channel, Player player, byte[] message) {
        if (!channel.equals("BungeeCord")) {
            return;
        }

        DataInputStream in = new DataInputStream(new ByteArrayInputStream(message));
        try {
            String subChannel = in.readUTF();
            if (subChannel.equals("ServerSwitchEvent")) {
                String playerString = in.readUTF();
                String fromServer = in.readUTF();
                String toServer = in.readUTF();

                // fire event
                System.out.println("fire switch event - " + test++);
                ServerSwitchEvent event = new ServerSwitchEvent(Bukkit.getPlayer(playerString), fromServer, toServer);
                BetterMessages.getInstance().getServer().getPluginManager().callEvent(event);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
