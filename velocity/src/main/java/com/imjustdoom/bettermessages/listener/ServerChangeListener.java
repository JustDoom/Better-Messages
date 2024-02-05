package com.imjustdoom.bettermessages.listener;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import com.imjustdoom.bettermessages.BetterMessages;
import com.velocitypowered.api.event.PostOrder;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.player.ServerConnectedEvent;
import com.velocitypowered.api.event.player.ServerPostConnectEvent;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.messages.MinecraftChannelIdentifier;
import com.velocitypowered.api.proxy.server.RegisteredServer;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class ServerChangeListener {

    @Subscribe(order = PostOrder.LAST)
    public void onPlayerChat(ServerPostConnectEvent event) throws IOException {
        Player player = event.getPlayer();

        ByteArrayOutputStream b = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(b);

        if (event.getPreviousServer() == null) {
            out.writeUTF("Join");
            out.writeUTF(player.getUsername());

        } else {
            out.writeUTF("ServerSwitchEvent");
            out.writeUTF(player.getUsername());
            out.writeUTF(event.getPreviousServer() == null ? "" : event.getPreviousServer().getServerInfo().getName());

        }
        out.writeUTF(player.getCurrentServer().get().getServerInfo().getName());

        for (RegisteredServer server : BetterMessages.getInstance().getServer().getAllServers()) {
            server.sendPluginMessage(MinecraftChannelIdentifier.from("bettermessages:main"), b.toByteArray());
        }
//        BetterMessages.getInstance().getServer().getServer(player.getCurrentServer().get().getServerInfo().getName()).get().sendPluginMessage(MinecraftChannelIdentifier.from("bettermessages:main"), b.toByteArray());
    }
}