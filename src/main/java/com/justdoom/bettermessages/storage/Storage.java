package com.justdoom.bettermessages.storage;

import com.justdoom.bettermessages.BetterMessages;
import com.justdoom.bettermessages.config.Config;
import com.justdoom.bettermessages.message.Message;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.*;
import java.util.UUID;

public class Storage {

    // TODO: re-code this at some point too

    private Connection connect() {
        String url = "jdbc:sqlite:" + BetterMessages.getInstance().getDataFolder() + "/database.db";
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }

    public boolean getUuid(UUID uuid) {
        File file = new File(Paths.get(BetterMessages.getInstance().getDataFolder() + "/data/" + uuid + ".yml").toString());

        return file != null;
    }

    public int getCount(UUID uuid, String column) {
        File file = new File(Paths.get(BetterMessages.getInstance().getDataFolder() + "/data/" + uuid + ".yml").toString());
        FileConfiguration config = YamlConfiguration.loadConfiguration(file);

        return config.getInt(column);
    }

    public void createPlayerData(UUID uuid) {
        try {
            File data = new File(BetterMessages.getInstance().getDataFolder() + "/data");
            if(!data.exists()) data.mkdir();
            File file = new File(BetterMessages.getInstance().getDataFolder() + "/data", uuid + ".yml");
            if (!file.exists()) {
                file.createNewFile(); //This needs a try catch
                FileConfiguration config = YamlConfiguration.loadConfiguration(file);

                for(Message msg : Config.MESSAGES) config.set(msg.getParent(), 0);

                config.save(file);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void update(UUID uuid, String column) {

        try {
            File file = new File(Paths.get(BetterMessages.getInstance().getDataFolder() + "/data/" + uuid + ".yml").toString());
            FileConfiguration config = YamlConfiguration.loadConfiguration(file);

            config.set(column, config.getInt(column) + 1);

            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
