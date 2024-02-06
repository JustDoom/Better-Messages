package com.imjustdoom.bettermessages.listener;

import com.imjustdoom.bettermessages.BetterMessages;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ServerSwitchEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class ServerChangeListener implements Listener {

    private int test = 0;

    @EventHandler
    public void onServerChange(ServerSwitchEvent event) {
        ProxiedPlayer player = event.getPlayer();

        ByteArrayOutputStream b = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(b);

        if (event.getFrom() == null) {
            try {
                out.writeUTF("Join");
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
//            BetterMessages.getInstance().getProxy().getServers().get(event.getPlayer().getServer().getInfo().getName()).sendData("BungeeCord", b.toByteArray());
        } else {
            try {
                out.writeUTF("ServerSwitchEvent");
                out.writeUTF(player.getName());
                out.writeUTF(event.getFrom() == null ? "" : event.getFrom().getName());
                out.writeUTF(player.getServer().getInfo().getName());
            } catch (IOException e) {
                e.printStackTrace();
            }

            for (ServerInfo serverInfo : BetterMessages.getInstance().getProxy().getServers().values()) {
                if (!serverInfo.getPlayers().isEmpty()) {
                    serverInfo.sendData("BungeeCord", b.toByteArray());
                }
            }

//            player.getServer().sendData("BungeeCord", b.toByteArray());
//            ServerInfo fromServer = BetterMessages.getInstance().getProxy().getServers().get(event.getFrom().getName());
//            if (fromServer.getPlayers().size() - 1 > 0) {
//                event.getFrom().sendData("BungeeCord", b.toByteArray());
//            }
        }
    }
}