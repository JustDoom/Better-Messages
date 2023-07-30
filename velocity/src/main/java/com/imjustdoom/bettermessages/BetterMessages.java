package com.imjustdoom.bettermessages;

import com.google.inject.Inject;
import com.imjustdoom.bettermessages.listener.ServerChangeListener;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.proxy.ProxyServer;
import lombok.Getter;
import org.slf4j.Logger;

@Getter
@Plugin(id = "bettermessages", name = "BetterMessages", version = "3.3.0", authors = {"JustDoom"})
public final class BetterMessages {

    private final ProxyServer server;
    private final Logger logger;

    @Inject
    public BetterMessages(ProxyServer server, Logger logger) {
        this.server = server;
        this.logger = logger;
    }

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {
        server.getEventManager().register(this, new ServerChangeListener());
    }
}