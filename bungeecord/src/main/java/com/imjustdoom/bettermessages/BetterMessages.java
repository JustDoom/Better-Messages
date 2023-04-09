package com.imjustdoom.bettermessages;

import com.imjustdoom.bettermessages.old.listener.ServerChangeListener;
import lombok.Getter;
import net.md_5.bungee.api.plugin.Plugin;

@Getter
public final class BetterMessages extends Plugin {

    @Override
    public void onEnable() {
        // Register event listener
        getProxy().getPluginManager().registerListener(this, new ServerChangeListener());
    }
}