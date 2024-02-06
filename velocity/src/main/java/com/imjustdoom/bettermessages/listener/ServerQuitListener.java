package com.imjustdoom.bettermessages.listener;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import com.imjustdoom.bettermessages.BetterMessages;
import com.velocitypowered.api.event.PostOrder;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.DisconnectEvent;
import com.velocitypowered.api.event.player.ServerConnectedEvent;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.messages.MinecraftChannelIdentifier;
import com.velocitypowered.api.proxy.server.RegisteredServer;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class ServerQuitListener {

    @Subscribe(order = PostOrder.LAST)
    public void onPlayerChat(DisconnectEvent event) throws IOException {
        Player player = event.getPlayer();

        ByteArrayOutputStream b = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(b);

        out.writeUTF("Quit");
        out.writeUTF(player.getUsername());
        out.writeUTF(event.getPlayer().getCurrentServer().get().getServerInfo().getName());

        for (RegisteredServer server : BetterMessages.getInstance().getServer().getAllServers()) {
            if (!server.getPlayersConnected().isEmpty()) {
                server.sendPluginMessage(MinecraftChannelIdentifier.from("bettermessages:main"), b.toByteArray());
            }
        }
//        BetterMessages.getInstance().getServer().getServer(player.getCurrentServer().get().getServerInfo().getName()).get().sendPluginMessage(MinecraftChannelIdentifier.from("bettermessages:main"), b.toByteArray());
    }
}