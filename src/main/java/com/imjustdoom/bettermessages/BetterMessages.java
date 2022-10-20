package com.imjustdoom.bettermessages;

import com.imjustdoom.bettermessages.message.EventType;
import com.imjustdoom.bettermessages.message.Message;
import com.imjustdoom.cmdinstruction.CMDInstruction;
import com.imjustdoom.bettermessages.command.BetterMessagesCmd;
import com.imjustdoom.bettermessages.config.Config;
import com.imjustdoom.bettermessages.listener.PlayerJoinListener;
import com.imjustdoom.bettermessages.listener.PlayerPreLoginListener;
import com.imjustdoom.bettermessages.listener.PlayerQuitListener;
import com.imjustdoom.bettermessages.listener.PlayerWorldChangeListener;
import com.imjustdoom.bettermessages.storage.Storage;
import com.imjustdoom.bettermessages.metrics.Metrics;

import java.util.HashMap;
import java.util.Map;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
public final class BetterMessages extends JavaPlugin {

    private static BetterMessages INSTANCE;
    private Storage storage;
    int configVersion = 14;

    public BetterMessages() {
        INSTANCE = this;
    }

    public void onEnable() {
        saveDefaultConfig();
        Config.init();

        storage = new Storage();

        // Check if config is up to date
        if (Config.CONFIG_VERSION != this.configVersion && !Config.DISABLE_OUTDATED_CONFIG_WARNING)
            getLogger().warning("The config file needs to be regenerated as it's not the latest version and could have unexpected results.");

        // Setup bStats metrics
        Metrics metrics = new Metrics(this, 8591);
        metrics.addCustomChart(new Metrics.MultiLineChart("players_and_servers", () -> {
            Map<String, Integer> valueMap = new HashMap<>();
            valueMap.put("servers", 1);
            valueMap.put("players", Bukkit.getOnlinePlayers().size());
            return valueMap;
        }));

        // Register commands
        CMDInstruction.registerCommands(this, new BetterMessagesCmd().setName("bettermessages").setPermission("bettermessages"));

        // Register events
        Bukkit.getPluginManager().registerEvents(new PlayerPreLoginListener(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerJoinListener(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerQuitListener(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerWorldChangeListener(), this);
    }

    public static BetterMessages getInstance() {
        return INSTANCE;
    }
}