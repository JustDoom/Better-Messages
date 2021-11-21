package com.justdoom.bettermessages;

import com.justdoom.bettermessages.commands.BetterMessagesCommand;
import com.justdoom.bettermessages.events.PlayerJoin;
import com.justdoom.bettermessages.events.PlayerPreLogin;
import com.justdoom.bettermessages.events.PlayerQuit;
import com.justdoom.bettermessages.events.PlayerWorldChange;
import com.justdoom.bettermessages.events.tabcomplete.BetterMessagesTabCompletion;
import com.justdoom.bettermessages.sqlite.SQLite;
import com.justdoom.bettermessages.util.MessageUtil;
import com.justdoom.bettermessages.util.Metrics;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class BetterMessages extends JavaPlugin {

    int configVersion = 11;

    public MessageUtil handler = new MessageUtil();
    public SQLite sqlite = new SQLite(this);

    public void onEnable() {
        if (getConfig().getInt("config-version") != this.configVersion && !getConfig().getBoolean("disable-outdated-config-warning"))
            getLogger().warning("The config file needs to be regenerated as it's not the latest version and could have unexpected results.");

        Metrics metrics = new Metrics(this, 8591);
        metrics.addCustomChart(new Metrics.MultiLineChart("players_and_servers", new Callable<Map<String, Integer>>() {
            public Map<String, Integer> call() throws Exception {
                Map<String, Integer> valueMap = new HashMap<>();
                valueMap.put("servers", 1);
                valueMap.put("players", Bukkit.getOnlinePlayers().size());
                return valueMap;
            }
        }));

        saveDefaultConfig();

        getCommand("bettermessages").setExecutor(new BetterMessagesCommand(this));
        getCommand("bettermessages").setTabCompleter(new BetterMessagesTabCompletion());

        Bukkit.getPluginManager().registerEvents(new PlayerPreLogin(this), this);
        Bukkit.getPluginManager().registerEvents(new PlayerJoin(this), this);
        Bukkit.getPluginManager().registerEvents(new PlayerQuit(this), this);
        Bukkit.getPluginManager().registerEvents(new PlayerWorldChange(this), this);

        //SQLite.connect();
        SQLite.createNewDatabase("database.db", this);

        // TODO a config cache system
    }
}