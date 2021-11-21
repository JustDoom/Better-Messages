package com.justdoom.bettermessages;

import com.justdoom.bettermessages.commands.BetterMessagesCommand;
import com.justdoom.bettermessages.events.PlayerJoin;
import com.justdoom.bettermessages.events.PlayerPreLogin;
import com.justdoom.bettermessages.events.PlayerQuit;
import com.justdoom.bettermessages.events.PlayerWorldChange;
import com.justdoom.bettermessages.events.tabcomplete.BetterMessagesTabCompletion;
import com.justdoom.bettermessages.sqlite.SQLite;
import com.justdoom.bettermessages.util.MessageUtil;
import com.justdoom.bettermessages.metrics.Metrics;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
public final class BetterMessages extends JavaPlugin {

    private static BetterMessages instance;
    int configVersion = 11;
    private final SQLite sqlite = new SQLite(this);

    public BetterMessages() {
        instance = this;
    }

    public void onEnable() {
        if (getConfig().getInt("config-version") != this.configVersion && !getConfig().getBoolean("disable-outdated-config-warning"))
            getLogger().warning("The config file needs to be regenerated as it's not the latest version and could have unexpected results.");

        Metrics metrics = new Metrics(this, 8591);
        metrics.addCustomChart(new Metrics.MultiLineChart("players_and_servers", () -> {
            Map<String, Integer> valueMap = new HashMap<>();
            valueMap.put("servers", 1);
            valueMap.put("players", Bukkit.getOnlinePlayers().size());
            return valueMap;
        }));

        saveDefaultConfig();

        getCommand("bettermessages").setExecutor(new BetterMessagesCommand());
        getCommand("bettermessages").setTabCompleter(new BetterMessagesTabCompletion());

        Bukkit.getPluginManager().registerEvents(new PlayerPreLogin(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerJoin(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerQuit(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerWorldChange(), this);

        //SQLite.connect();
        SQLite.createNewDatabase("database.db", this);
    }

    public static BetterMessages getInstance() {
        return instance;
    }
}