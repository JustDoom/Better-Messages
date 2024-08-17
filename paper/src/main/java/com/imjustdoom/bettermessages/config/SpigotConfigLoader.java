package com.imjustdoom.bettermessages.config;

import com.imjustdoom.bettermessages.BetterMessages;
import com.imjustdoom.bettermessages.message.Message;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

public class SpigotConfigLoader implements ConfigLoader {

    private FileConfiguration configuration;

    @Override
    public void load() {

        BetterMessages.getInstance().reloadConfig();
        this.configuration = BetterMessages.getInstance().getConfig();

//        // Get messages
//        for (String parent : getConfig().getConfigurationSection("messages").getKeys(false)) {
//            boolean enabled = getMessageSection(parent).getBoolean("enabled");
//            String message = getMessageSection(parent).getString("message");
//            Configuration.MESSAGES.add(new Message(enabled, message));
//        }
    }

    @Override
    public void save() {
        getConfig().options().copyDefaults(true);
        BetterMessages.getInstance().saveConfig();
    }

    @Override
    public void setHeader(String header) {
        this.configuration.options().header(header);
    }

    @Override
    public String getString(String path, String defaultString) {
        getConfig().addDefault(path, defaultString);
        return getConfig().getString(path, getConfig().getString(path));
    }

    @Override
    public boolean getBoolean(String path, boolean defaultBoolean) {
        getConfig().addDefault(path, defaultBoolean);
        return getConfig().getBoolean(path, getConfig().getBoolean(path));
    }

    @Override
    public int getInt(String path, int defaultInt) {
        getConfig().addDefault(path, defaultInt);
        return getConfig().getInt(path, getConfig().getInt(path));
    }

    @Override
    public void generatePath(String path) {
        getConfig().createSection(path);
    }

    @Override
    public void generatePath(String... path) {

    }

    private FileConfiguration getConfig() {
        return this.configuration;
    }
}
