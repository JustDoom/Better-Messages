package com.imjustdoom.bettermessages;

import lombok.Getter;
import net.md_5.bungee.api.plugin.Plugin;

@Getter
public final class BetterMessages extends Plugin {

    @Override
    public void onEnable() {
        // You should not put an enable message in your plugin.
        // BungeeCord already does so
        getLogger().info("Yay! It loads!");
    }
}