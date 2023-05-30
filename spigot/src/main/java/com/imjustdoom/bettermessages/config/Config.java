package com.imjustdoom.bettermessages.config;

import com.imjustdoom.bettermessages.BetterMessages;
import com.imjustdoom.bettermessages.message.EventType;
import com.imjustdoom.bettermessages.message.Message;
import com.imjustdoom.bettermessages.message.MessageBuilder;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.*;

public class Config {

    public static final int LATEST_CONFIG_VERSION = 16;

    public static boolean DISABLE_OUTDATED_CONFIG_WARNING;

    public static int CONFIG_VERSION;
    public static boolean CHECK_FOR_UPDATES;
    public static boolean BUNGEECORD_MODE;

    public static final Map<Class<? extends Message>, List<Message>> MESSAGES = new HashMap<>();

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
            MESSAGES.put(eventType.getClazz(), new ArrayList<>());
        }

        // General plugin settings
        DISABLE_OUTDATED_CONFIG_WARNING = getConfig().getBoolean("disable-outdated-config-warning");
        CONFIG_VERSION = getConfig().getInt("config-version");
        CHECK_FOR_UPDATES = getConfig().getBoolean("check-for-updates");
        BUNGEECORD_MODE = getConfig().getBoolean("bungeecord-mode");

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
                BetterMessages.getInstance().getLogger().warning("The permission option in the config is now a boolean (true/false). Please update your config.");
                permission = false;
            } else {
                permission = getConfig().getBoolean("messages." + msg + ".permission");
            }

            String messageType = getConfig().getString("messages." + msg + ".message-type");

            for (String type : getConfig().getStringList("messages." + msg + ".activation")) {
                String extraInfo = null;
                if (type.contains("/")) {
                    extraInfo = type.split("/", 2)[1];
                    type = type.split("/")[0];
                }

                EventType eventType = EventType.valueOf(type.toUpperCase().replaceAll("-", "_"));

                Message message = new MessageBuilder()
                        .setParent(msg)
                        .setMessage(messages)
                        .setCommands(getConfig().getStringList("messages." + msg + ".commands"))
                        .setCount(count)
                        .setPermission(permission)
                        .setEnabled(getConfig().getBoolean("messages." + msg + ".enabled"))
                        .setAudience(getConfig().getString("messages." + msg + ".audience"))
                        .setStorageType(getConfig().getString("messages." + msg + ".storage-type"))
                        .setDontRunIf(getConfig().getString("messages." + msg + ".world") == null ? "" : getConfig().getString("messages." + msg + ".world"))
                        .setDelay(getConfig().getInt("messages." + msg + ".delay"))
                        .setPriority(getConfig().getInt("messages." + msg + ".priority"))
                        .setExtraInfo(extraInfo)
                        .setMessageType(messageType)
                        .build(eventType);

                MESSAGES.get(message.getClass()).add(message);
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
