package com.imjustdoom.bettermessages;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;
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
import org.bukkit.plugin.java.JavaPlugin;

@Getter
public final class BetterMessages extends JavaPlugin {

    private static BetterMessages INSTANCE;
    private Storage storage;
    int configVersion = 15;

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

        //getServer().getMessenger().registerIncomingPluginChannel( this, "my:channel" );


        // Register events
        Bukkit.getPluginManager().registerEvents(new PlayerPreLoginListener(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerJoinListener(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerQuitListener(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerWorldChangeListener(), this);

        // Update checker
        try {
            if (Config.CHECK_FOR_UPDATES) {
                checkUpdates();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void checkUpdates() throws IOException {
        JsonElement jsonElement = getJsonFromUrl("https://api.imjustdoom.com/projects/better-messages");

        if (jsonElement == null) {
            throw new IOException("Failed to check for updates");
        }

        JsonElement latestVersion = getJsonFromUrl("https://api.imjustdoom.com/projects/" + jsonElement.getAsJsonObject().get("id").getAsString() + "/latest");

        if (latestVersion == null) {
            throw new IOException("Failed to check for updates");
        }

        if (latestVersion.getAsJsonObject().get("version").getAsString().equals(getDescription().getVersion())) {
            getLogger().info("You are running the latest version of BetterMessages!");
        } else {
            getLogger().info("There is a new version of BetterMessages available!You are running version " + getDescription().getVersion() + " and the latest version is " + latestVersion.getAsJsonObject().get("version").getAsString() + ". Download it at https://imjustdoom.com/projects/better-messages");
        }
    }

    public JsonElement getJsonFromUrl(String url) throws IOException {
        URL uri = new URL(url);
        URLConnection con = uri.openConnection();
        con.setRequestProperty("User-Agent", "Mozilla");
        con.setReadTimeout(5000);
        con.setConnectTimeout(5000);
        con.setUseCaches(false);

        InputStream inputStream = con.getInputStream();

        JsonReader reader = new JsonReader(new InputStreamReader(inputStream));
        reader.setLenient(true);

        return new JsonParser().parse(reader).getAsJsonObject();
    }

    public static BetterMessages getInstance() {
        return INSTANCE;
    }
}