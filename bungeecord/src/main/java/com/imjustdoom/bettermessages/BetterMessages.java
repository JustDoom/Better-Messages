package com.imjustdoom.bettermessages;

import com.imjustdoom.bettermessages.listener.ServerChangeListener;
import com.imjustdoom.bettermessages.listener.ServerQuitListener;
import lombok.Getter;
import net.md_5.bungee.api.plugin.Plugin;

@Getter
public final class BetterMessages extends Plugin {

    private static BetterMessages INSTANCE;

    public BetterMessages() {
        INSTANCE = this;
    }

    @Override
    public void onEnable() {
        // Register event listener
        getProxy().getPluginManager().registerListener(this, new ServerChangeListener());
        getProxy().getPluginManager().registerListener(this, new ServerQuitListener());
    }

    public static BetterMessages getInstance() {
        return INSTANCE;
    }
}