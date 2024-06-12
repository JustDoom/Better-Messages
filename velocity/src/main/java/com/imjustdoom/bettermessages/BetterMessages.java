package com.imjustdoom.bettermessages;

import com.google.inject.Inject;
import com.imjustdoom.bettermessages.listener.ServerChangeListener;
import com.imjustdoom.bettermessages.listener.ServerQuitListener;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.messages.MinecraftChannelIdentifier;
import lombok.Getter;
import org.slf4j.Logger;

@Getter
@Plugin(id = "bettermessages", name = "BetterMessages", version = "3.3.2", authors = {"JustDoom"})
public final class BetterMessages {

    private final ProxyServer server;
    private final Logger logger;

    private static BetterMessages INSTANCE;

    @Inject
    public BetterMessages(ProxyServer server, Logger logger) {
        INSTANCE = this;
        this.server = server;
        this.logger = logger;
    }

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {
        server.getChannelRegistrar().register(MinecraftChannelIdentifier.from("bettermessages:main"));

        server.getEventManager().register(this, new ServerChangeListener());
        server.getEventManager().register(this, new ServerQuitListener());
    }

    public static BetterMessages getInstance() {
        return INSTANCE;
    }
}