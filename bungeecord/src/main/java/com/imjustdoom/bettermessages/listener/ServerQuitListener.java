package com.imjustdoom.bettermessages.listener;

import com.imjustdoom.bettermessages.BetterMessages;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.event.ServerDisconnectEvent;
import net.md_5.bungee.api.event.ServerSwitchEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class ServerQuitListener implements Listener {

    @EventHandler
    public void onServerChange(PlayerDisconnectEvent event) {
        ProxiedPlayer player = event.getPlayer();

        ByteArrayOutputStream b = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(b);

        try {
            out.writeUTF("Quit");
            out.writeUTF(player.getName());
            out.writeUTF(player.getServer().getInfo().getName());
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (ServerInfo serverInfo : BetterMessages.getInstance().getProxy().getServers().values()) {
            if (!serverInfo.getPlayers().isEmpty()) {
                serverInfo.sendData("BungeeCord", b.toByteArray());
            }
        }
//        event.getPlayer().getServer().sendData("BungeeCord", b.toByteArray());
    }
}