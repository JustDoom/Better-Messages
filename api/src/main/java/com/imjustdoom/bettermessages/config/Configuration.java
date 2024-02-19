package com.imjustdoom.bettermessages.config;

import com.imjustdoom.bettermessages.message.Message;

import java.util.ArrayList;
import java.util.List;

// TODO: Maybe make this not a static config class?
// TODO: Make a fully custom config system that generates the default config too
public class Configuration {

    public static ConfigLoader CONFIG_LOADER;

    public static class PluginOptions {
        public static int CONFIG_VERSION = 17;
        public static boolean DISABLE_OUTDATED_CONFIG_WARNING = false;
        public static boolean CHECK_FOR_UPDATES = true;
        public static boolean PROXY_MODE = false;
    }

    public static class PluginMessages {
        public static String PREFIX = "BM";
        public static String HELP = "help";
        public static String RELOADED = "reloaded";
        public static String HELP_REDIRECT = "redirect";
        public static String CHANGED_MESSAGE = "changed";
    }

    public static List<Message> MESSAGES;

    // TODO: Make this fire an event as people using the API might add custom messages
    public static void load() {
        MESSAGES = new ArrayList<>();

        CONFIG_LOADER.load();

        CONFIG_LOADER.setHeader("something blah blah documentation etc link");

        // General plugin settings
        PluginOptions.CONFIG_VERSION = CONFIG_LOADER.getInt("config-version", PluginOptions.CONFIG_VERSION);
        PluginOptions.DISABLE_OUTDATED_CONFIG_WARNING = CONFIG_LOADER.getBoolean("disable-outdated-config-warning", PluginOptions.DISABLE_OUTDATED_CONFIG_WARNING);
        PluginOptions.CHECK_FOR_UPDATES = CONFIG_LOADER.getBoolean("check-for-updates", PluginOptions.CHECK_FOR_UPDATES);
        PluginOptions.PROXY_MODE = CONFIG_LOADER.getBoolean("proxy-mode", PluginOptions.PROXY_MODE);

        PluginMessages.PREFIX = CONFIG_LOADER.getString("internal-messages.prefix", PluginMessages.PREFIX);
        PluginMessages.HELP = CONFIG_LOADER.getString("internal-messages.help", PluginMessages.HELP);
        PluginMessages.RELOADED = CONFIG_LOADER.getString("internal-messages.reloaded", PluginMessages.RELOADED);
        PluginMessages.HELP_REDIRECT = CONFIG_LOADER.getString("internal-messages.help-redirect", PluginMessages.HELP_REDIRECT);
        PluginMessages.CHANGED_MESSAGE = CONFIG_LOADER.getString("internal-messages.changed-message", PluginMessages.CHANGED_MESSAGE);

        CONFIG_LOADER.save();
    }
}