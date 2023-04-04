package com.imjustdoom.bettermessages;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;
import com.imjustdoom.bettermessages.listener.*;
import com.imjustdoom.bettermessages.message.EventType;
import com.imjustdoom.bettermessages.message.Message;
import com.imjustdoom.cmdinstruction.CMDInstruction;
import com.imjustdoom.bettermessages.command.BetterMessagesCmd;
import com.imjustdoom.bettermessages.config.Config;
import com.imjustdoom.bettermessages.storage.Storage;
import com.imjustdoom.bettermessages.metrics.Metrics;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandMap;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
public final class BetterMessages extends JavaPlugin {

    private static BetterMessages INSTANCE;
    private Storage storage;

    public BetterMessages() {
        INSTANCE = this;
    }

    @Override
    public void onEnable() {

        // Load and handle the config
        saveDefaultConfig();
        Config.init();
        Config.isConfigUpToDate();

        // Load the storage
        this.storage = new Storage();

        // Register commands
        CMDInstruction.registerCommands(this, new BetterMessagesCmd().setName("bettermessages").setPermission("bettermessages"));

        // Register proxy listener if enabled
        if (Config.BUNGEECORD_MODE) {
            getServer().getMessenger().registerIncomingPluginChannel(this, "BungeeCord", new BungeeCordListener());
        }

        // Register events
        Bukkit.getPluginManager().registerEvents(new PlayerPreLoginListener(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerJoinListener(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerQuitListener(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerWorldChangeListener(), this);
        Bukkit.getPluginManager().registerEvents(new ServerSwitchListener(), this);

        // Update checker
        try {
            if (Config.CHECK_FOR_UPDATES) {
                getLogger().warning(UpdateChecker.checkUpdates(getDescription().getVersion()));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Setup bStats metrics
        Metrics metrics = new Metrics(this, 8591);
        metrics.addCustomChart(new Metrics.MultiLineChart("players_and_servers", () -> {
            Map<String, Integer> valueMap = new HashMap<>();
            valueMap.put("servers", 1);
            valueMap.put("players", Bukkit.getOnlinePlayers().size());
            return valueMap;
        }));
    }

    @Override
    public void onDisable() {
        HandlerList.unregisterAll(this);
    }

    public static BetterMessages getInstance() {
        return INSTANCE;
    }
}