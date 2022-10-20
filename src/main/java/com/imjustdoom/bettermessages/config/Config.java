package com.imjustdoom.bettermessages.config;

import com.imjustdoom.bettermessages.BetterMessages;
import com.imjustdoom.bettermessages.message.EventType;
import com.imjustdoom.bettermessages.message.Message;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.*;

public class Config {

    public static boolean DISABLE_OUTDATED_CONFIG_WARNING;

    public static int CONFIG_VERSION;

    public static final HashMap<EventType, List<Message>> MESSAGES = new HashMap<>();

    public static class InternalMessages {
        public static String PREFIX;
        public static String HELP;
        public static String RELOADED;
        public static String HELP_REDIRECT;
        public static String CHANGED_MESSAGE;
    }

    public static void init() {
        BetterMessages.getInstance().reloadConfig();

        MESSAGES.clear();

        DISABLE_OUTDATED_CONFIG_WARNING = getConfig().getBoolean("disable-outdated-config-warning");
        CONFIG_VERSION = getConfig().getInt("config-version");

        InternalMessages.PREFIX = getConfig().getString("internal-messages.prefix");
        InternalMessages.HELP = getConfig().getString("internal-messages.help");
        InternalMessages.RELOADED = getConfig().getString("internal-messages.reloaded");
        InternalMessages.HELP_REDIRECT = getConfig().getString("internal-messages.help-redirect");
        InternalMessages.CHANGED_MESSAGE = getConfig().getString("internal-messages.changed-message");

        for (String msg : getConfig().getConfigurationSection("messages").getKeys(false)) {

            List<String> messages;
            if (getConfig().isString("messages." + msg + ".message")) {
                messages = Collections.singletonList(getConfig().getString("messages." + msg + ".message"));
            } else {
                messages = getConfig().getStringList("messages." + msg + ".message");
            }

            List<Integer> count;
            if (getConfig().isInt("messages." + msg + ".count")) {
                count = getConfig().getInt("messages." + msg + ".count") == -1 ? Arrays.asList(-1) : Arrays.asList(getConfig().getInt("messages." + msg + ".count"));
            } else {
                count = getConfig().getIntegerList("messages." + msg + ".count");
            }

            boolean permission;
            if (getConfig().isString("messages." + msg + ".permission")) {
                System.out.println("[BetterMessages] The permission option in the config is now a boolean (true/false). Please update your config.");
                permission = false;
            } else {
                permission = getConfig().getBoolean("messages." + msg + ".permission");
            }

            for (String type : getConfig().getStringList("messages." + msg + ".activation")) {
                MESSAGES.get(EventType.valueOf(type.toUpperCase())).add(new Message(msg, messages, getConfig().getStringList("messages." + msg + ".commands"), count, permission, getConfig().getBoolean("messages." + msg + ".enabled"), getConfig().getString("messages." + msg + ".audience"), getConfig().getString("messages." + msg + ".storage-type"), getConfig().getString("messages." + msg + ".world") == null ? "" : getConfig().getString("messages." + msg + ".world"), getConfig().getInt("messages." + msg + ".delay")));
            }
        }
    }

    private static FileConfiguration getConfig() {
        return BetterMessages.getInstance().getConfig();
    }
}
