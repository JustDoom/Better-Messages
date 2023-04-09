package com.imjustdoom.bettermessages.old.storage;

import com.imjustdoom.bettermessages.BetterMessages;
import com.imjustdoom.bettermessages.old.config.Config;
import com.imjustdoom.bettermessages.old.message.MessageImpl;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

public class Storage {

    // TODO: Improve this system

    public int getCount(UUID uuid, String column) {
        File file = new File(Paths.get(BetterMessages.getInstance().getDataFolder() + "/data/" + uuid + ".yml").toString());
        FileConfiguration config = YamlConfiguration.loadConfiguration(file);

        return config.getInt(column);
    }

    public void createPlayerData(UUID uuid) {
        try {
            File data = new File(BetterMessages.getInstance().getDataFolder() + "/data");
            if(!data.exists()) {
                data.mkdir();
            }
            File file = new File(BetterMessages.getInstance().getDataFolder() + "/data", uuid + ".yml");
            if (!file.exists()) {
                file.createNewFile(); //This needs a try catch
                FileConfiguration config = YamlConfiguration.loadConfiguration(file);

                for (List<MessageImpl> msgList : Config.MESSAGES.values()) {
                    for (MessageImpl msg : msgList) {
                        config.set(msg.getParent(), 0);
                        config.set("messages." + msg.getParent(), "");
                    }
                }

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

    public void updateMessage(UUID uuid, String column, String message) {

        try {
            File file = new File(Paths.get(BetterMessages.getInstance().getDataFolder() + "/data/" + uuid + ".yml").toString());
            FileConfiguration config = YamlConfiguration.loadConfiguration(file);

            config.set(column, message);

            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getMessage(UUID uuid, String column) {

        File file = new File(Paths.get(BetterMessages.getInstance().getDataFolder() + "/data/" + uuid + ".yml").toString());
        FileConfiguration config = YamlConfiguration.loadConfiguration(file);

        String msg = config.getString("messages." + column);

        if(msg == null) {
            config.set("messages." + column, "");
            try {
                config.save(file);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return "";
        }
        return msg;
    }
}
