package com.imjustdoom.bettermessages.listener;

import com.imjustdoom.bettermessages.BetterMessages;
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
        try {
            out.writeUTF("ServerSwitchEvent");
            out.writeUTF(player.getName());
            out.writeUTF(event.getFrom() == null ? "" : event.getFrom().getName());
            out.writeUTF(player.getServer().getInfo().getName());
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("fire - " + test++);
        System.out.println("from - " + event.getFrom().getName());
        System.out.println("to - " + event.getPlayer().getServer().getInfo().getName());
        BetterMessages.getInstance().getProxy().getServers().get(event.getPlayer().getServer().getInfo().getName()).sendData("BungeeCord", b.toByteArray());
        //player.getServer().sendData("BungeeCord", b.toByteArray());
        BetterMessages.getInstance().getProxy().getServers().get(event.getFrom().getName()).sendData("BungeeCord", b.toByteArray());
    }
}