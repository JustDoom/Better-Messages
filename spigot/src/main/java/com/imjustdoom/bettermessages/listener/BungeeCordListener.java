package com.imjustdoom.bettermessages.listener;

import com.imjustdoom.bettermessages.BetterMessages;
import com.imjustdoom.bettermessages.config.Config;
import com.imjustdoom.bettermessages.listener.event.ServerSwitchEvent;
import com.imjustdoom.bettermessages.manager.PlayerManager;
import com.imjustdoom.bettermessages.message.EventType;
import com.imjustdoom.bettermessages.message.Message;
import com.imjustdoom.bettermessages.util.VanishUtil;
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
        if (!channel.equals("BungeeCord") && !channel.equals("bettermessages:main")) {
            return;
        }

        DataInputStream in = new DataInputStream(new ByteArrayInputStream(message));
        try {
            String subChannel = in.readUTF();
            switch (subChannel) {
                case "ServerSwitchEvent": {
                    String playerString = in.readUTF();
                    String fromServer = in.readUTF();
                    String toServer = in.readUTF();

                    // fire event
                    ServerSwitchEvent event = new ServerSwitchEvent(Bukkit.getPlayer(playerString), fromServer, toServer);
                    BetterMessages.getInstance().getServer().getPluginManager().callEvent(event);
                    break;
                }
                case "Join": {
                    String playerString = in.readUTF();
                    String toServer = in.readUTF();

                    Player p = Bukkit.getPlayer(playerString);

                    PlayerManager.addWaitingPlayer(p.getUniqueId(), p.getWorld());
                    PlayerManager.removePlayer(p.getUniqueId());

                    if (VanishUtil.isVanished(p) || p.hasPermission("bettermessages.silent-join")) {
                        return;
                    }

                    Message pMessage = null;

                    for (Message msg : Config.MESSAGES.get(EventType.JOIN.getClazz())) {

                        if (!msg.canRun(p, null)) {
                            continue;
                        }

                        BetterMessages.getInstance().getStorage().update(p.getUniqueId(), msg.getParent());

                        if (msg.getPriority() != -1) {
                            if (pMessage == null) {
                                pMessage = msg;
                                continue;
                            }
                            if (msg.getPriority() < pMessage.getPriority()) {
                                pMessage = msg;
                            }
                            continue;
                        }

                        msg.sendMessage(p);
                    }
                    if (pMessage != null) {
                        pMessage.sendMessage(p);
                    }
                    break;
                }
                case "Quit": {
                    String playerString = in.readUTF();
                    String toServer = in.readUTF();

                    Player p = Bukkit.getPlayer(playerString);

                    if (p != null) {
                        PlayerManager.removePlayer(p.getUniqueId());
                    }

                    if (VanishUtil.isVanished(p) || p.hasPermission("bettermessages.silent-quit")) {
                        return;
                    }

                    Message pMessage = null;

                    for (Message msg : Config.MESSAGES.get(EventType.QUIT.getClazz())) {

                        if (!msg.canRun(p, null)) {
                            continue;
                        }

                        BetterMessages.getInstance().getStorage().update(p.getUniqueId(), msg.getParent());

                        if (msg.getPriority() != -1) {
                            if (pMessage == null) {
                                pMessage = msg;
                                continue;
                            }
                            if (msg.getPriority() < pMessage.getPriority()) {
                                pMessage = msg;
                            }
                            continue;
                        }

                        msg.sendMessage(p);
                    }
                    if (pMessage != null) {
                        pMessage.sendMessage(p);
                    }
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
