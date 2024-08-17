package com.imjustdoom.bettermessages;

import com.imjustdoom.bettermessages.config.SpigotConfigLoader;
import com.imjustdoom.bettermessages.listener.PlayerJoinListener;

import com.imjustdoom.cmdinstruction.CMDInstruction;
import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;

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

        CMDInstruction.registerCommands(this, new Command().setName("test").setPermission("test"));
    }

    @Override
    public void onDisable() {
        HandlerList.unregisterAll(this);
    }

    public static BetterMessages getInstance() {
        return INSTANCE;
    }
}