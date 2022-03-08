package com.justdoom.bettermessages;

import com.justdoom.bettermessages.commands.BetterMessagesCommand;
import com.justdoom.bettermessages.config.Config;
import com.justdoom.bettermessages.events.PlayerJoin;
import com.justdoom.bettermessages.events.PlayerPreLogin;
import com.justdoom.bettermessages.events.PlayerQuit;
import com.justdoom.bettermessages.events.PlayerWorldChange;
import com.justdoom.bettermessages.events.tabcomplete.BetterMessagesTabCompletion;
import com.justdoom.bettermessages.storage.Storage;
import com.justdoom.bettermessages.metrics.Metrics;

import java.util.HashMap;
import java.util.Map;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
public final class BetterMessages extends JavaPlugin {

    private static BetterMessages INSTANCE;
    private Storage storage;
    int configVersion = 13;

    public BetterMessages() {
        INSTANCE = this;
    }

    public void onEnable() {
        saveDefaultConfig();
        Config.init();

        if (Config.CONFIG_VERSION != this.configVersion && !Config.DISABLE_OUTDATED_CONFIG_WARNING)
            getLogger().warning("The config file needs to be regenerated as it's not the latest version and could have unexpected results.");

        Metrics metrics = new Metrics(this, 8591);
        metrics.addCustomChart(new Metrics.MultiLineChart("players_and_servers", () -> {
            Map<String, Integer> valueMap = new HashMap<>();
            valueMap.put("servers", 1);
            valueMap.put("players", Bukkit.getOnlinePlayers().size());
            return valueMap;
        }));

        getCommand("bettermessages").setExecutor(new BetterMessagesCommand());
        //getCommand("bettermessages").setTabCompleter(new BetterMessagesTabCompletion());

        Bukkit.getPluginManager().registerEvents(new PlayerPreLogin(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerJoin(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerQuit(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerWorldChange(), this);

        storage = new Storage();
    }

    public static BetterMessages getInstance() {
        return INSTANCE;
    }
}