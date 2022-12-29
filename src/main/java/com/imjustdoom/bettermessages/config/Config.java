package com.imjustdoom.bettermessages.config;

import com.imjustdoom.bettermessages.BetterMessages;
import com.imjustdoom.bettermessages.message.*;
import com.imjustdoom.bettermessages.message.msg.JoinMessage;
import com.imjustdoom.bettermessages.message.msg.QuitMessage;
import com.imjustdoom.bettermessages.message.msg.WorldChangeMessage;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.*;

public class Config {

    public static final int LATEST_CONFIG_VERSION = 15;

    public static boolean DISABLE_OUTDATED_CONFIG_WARNING;

    public static int CONFIG_VERSION;
    public static boolean CHECK_FOR_UPDATES;

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
        for (EventType eventType : EventType.values()) {
            MESSAGES.put(eventType, new ArrayList<>());
        }

        // General plugin settings
        DISABLE_OUTDATED_CONFIG_WARNING = getConfig().getBoolean("disable-outdated-config-warning");
        CONFIG_VERSION = getConfig().getInt("config-version");
        CHECK_FOR_UPDATES = getConfig().getBoolean("check-for-updates");

        // Get plugin configurable messages
        InternalMessages.PREFIX = getConfig().getString("internal-messages.prefix");
        InternalMessages.HELP = getConfig().getString("internal-messages.help");
        InternalMessages.RELOADED = getConfig().getString("internal-messages.reloaded");
        InternalMessages.HELP_REDIRECT = getConfig().getString("internal-messages.help-redirect");
        InternalMessages.CHANGED_MESSAGE = getConfig().getString("internal-messages.changed-message");

        // Get the messages from the config
        for (String msg : getConfig().getConfigurationSection("messages").getKeys(false)) {

            List<String> messages;
            if (getConfig().isString("messages." + msg + ".message")) {
                messages = Collections.singletonList(getConfig().getString("messages." + msg + ".message"));
            } else {
                messages = getConfig().getStringList("messages." + msg + ".message");
            }

            List<Integer> count;
            if (getConfig().isInt("messages." + msg + ".count")) {
                count = getConfig().getInt("messages." + msg + ".count") == -1 ? Collections.singletonList(-1) : Collections.singletonList(getConfig().getInt("messages." + msg + ".count"));
            } else {
                count = getConfig().getIntegerList("messages." + msg + ".count");
            }

            boolean permission;
            if (getConfig().isString("messages." + msg + ".permission")) {
                BetterMessages.getInstance().getLogger().info("The permission option in the config is now a boolean (true/false). Please update your config.");
                permission = false;
            } else {
                permission = getConfig().getBoolean("messages." + msg + ".permission");
            }

            for (String type : getConfig().getStringList("messages." + msg + ".activation")) {
                String extraInfo = null;
                if (type.contains("/")) {
                    extraInfo = type.split("/", 2)[1];
                    type = type.split("/")[0];
                }

                EventType eventType = EventType.valueOf(type.toUpperCase().replaceAll("-", "_"));

                switch (eventType) {
                    case JOIN:
                        MESSAGES.get(eventType).add(new JoinMessage(msg, messages, getConfig().getStringList("messages." + msg + ".commands"), count, permission, getConfig().getBoolean("messages." + msg + ".enabled"), getConfig().getString("messages." + msg + ".audience"), getConfig().getString("messages." + msg + ".storage-type"), getConfig().getString("messages." + msg + ".world") == null ? "" : getConfig().getString("messages." + msg + ".world"), getConfig().getInt("messages." + msg + ".delay"), getConfig().getInt("messages." + msg + ".priority"), extraInfo));
                    case QUIT:
                        MESSAGES.get(eventType).add(new QuitMessage(msg, messages, getConfig().getStringList("messages." + msg + ".commands"), count, permission, getConfig().getBoolean("messages." + msg + ".enabled"), getConfig().getString("messages." + msg + ".audience"), getConfig().getString("messages." + msg + ".storage-type"), getConfig().getString("messages." + msg + ".world") == null ? "" : getConfig().getString("messages." + msg + ".world"), getConfig().getInt("messages." + msg + ".delay"), getConfig().getInt("messages." + msg + ".priority"), extraInfo));
                    case WORLD_CHANGE:
                        MESSAGES.get(eventType).add(new WorldChangeMessage(msg, messages, getConfig().getStringList("messages." + msg + ".commands"), count, permission, getConfig().getBoolean("messages." + msg + ".enabled"), getConfig().getString("messages." + msg + ".audience"), getConfig().getString("messages." + msg + ".storage-type"), getConfig().getString("messages." + msg + ".world") == null ? "" : getConfig().getString("messages." + msg + ".world"), getConfig().getInt("messages." + msg + ".delay"), getConfig().getInt("messages." + msg + ".priority"), extraInfo));
                }
            }
        }
    }

    private static FileConfiguration getConfig() {
        return BetterMessages.getInstance().getConfig();
    }

    public static void isConfigUpToDate() {
        if (Config.CONFIG_VERSION != Config.LATEST_CONFIG_VERSION && !Config.DISABLE_OUTDATED_CONFIG_WARNING) {
            BetterMessages.getInstance().getLogger().warning("The config file needs to be regenerated as it's not the latest version and could have unexpected results.");
        }
    }
}
