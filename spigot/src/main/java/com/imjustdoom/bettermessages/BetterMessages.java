package com.imjustdoom.bettermessages;

import com.imjustdoom.bettermessages.config.SpigotConfigLoader;
import com.imjustdoom.bettermessages.listener.PlayerJoinListener;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
public final class BetterMessages extends JavaPlugin {

    private static BetterMessages INSTANCE;

    private BetterMessagesCore betterMessagesCore;

    public BetterMessages() {
        INSTANCE = this;
    }

    @Override
    public void onEnable() {

        this.betterMessagesCore = new BetterMessagesCore(getDescription().getVersion(), new SpigotConfigLoader());

        Bukkit.getPluginManager().registerEvents(new PlayerJoinListener(), this);
    }

    @Override
    public void onDisable() {
        HandlerList.unregisterAll(this);
    }

    public static BetterMessages getInstance() {
        return INSTANCE;
    }
}