package com.imjustdoom.bettermessages.config;

import com.imjustdoom.bettermessages.message.Message;

import java.util.ArrayList;
import java.util.List;

// TODO: Maybe make this not a static config class?
// TODO: Make a fully custom config system that generates the default config too
public class Configuration {

    public static ConfigLoader CONFIG_LOADER;

    private static final int LATEST_CONFIG_VERSION = 17;

    public static class PluginOptions {
        public static int CONFIG_VERSION;
        public static boolean DISABLE_OUTDATED_CONFIG_WARNING;
        public static boolean CHECK_FOR_UPDATES;
        public static boolean PROXY_MODE;
    }

    public static class PluginMessages {
        public static String PREFIX;
        public static String HELP;
        public static String RELOADED;
        public static String HELP_REDIRECT;
        public static String CHANGED_MESSAGE;
    }

    public static List<Message> MESSAGES;

    // TODO: Make this fire an event as people using the API might add custom messages
    public static void load() {
        CONFIG_LOADER.load();

        MESSAGES = new ArrayList<>();
    }
}