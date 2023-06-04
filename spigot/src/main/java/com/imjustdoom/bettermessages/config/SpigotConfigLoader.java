package com.imjustdoom.bettermessages.config;

import com.imjustdoom.bettermessages.BetterMessages;
import com.imjustdoom.bettermessages.message.Message;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

public class SpigotConfigLoader implements ConfigLoader {

    @Override
    public void load() {
        // TODO: Use custom config creator when done instead
        BetterMessages.getInstance().saveDefaultConfig();

        BetterMessages.getInstance().reloadConfig();

        // General plugin settings
        Configuration.PluginOptions.DISABLE_OUTDATED_CONFIG_WARNING = getConfig().getBoolean("disable-outdated-config-warning");
        Configuration.PluginOptions.CONFIG_VERSION = getConfig().getInt("config-version");
        Configuration.PluginOptions.CHECK_FOR_UPDATES = getConfig().getBoolean("check-for-updates");
        Configuration.PluginOptions.PROXY_MODE = getConfig().getBoolean("bungeecord-mode");

        // Get plugin configurable messages
        Configuration.PluginMessages.PREFIX = getConfig().getString("internal-messages.prefix");
        Configuration.PluginMessages.HELP = getConfig().getString("internal-messages.help");
        Configuration.PluginMessages.RELOADED = getConfig().getString("internal-messages.reloaded");
        Configuration.PluginMessages.HELP_REDIRECT = getConfig().getString("internal-messages.help-redirect");
        Configuration.PluginMessages.CHANGED_MESSAGE = getConfig().getString("internal-messages.changed-message");

        // Get messages
        for (String parent : getConfig().getConfigurationSection("messages").getKeys(false)) {
            boolean enabled = getMessageSection(parent).getBoolean("enabled");
            String message = getMessageSection(parent).getString("message");
            Configuration.MESSAGES.add(new Message(enabled, message));
        }
    }

    private FileConfiguration getConfig() {
        return BetterMessages.getInstance().getConfig();
    }

    private ConfigurationSection getMessageSection(String parent) {
        return getConfig().getConfigurationSection("messages." + parent);
    }
}
