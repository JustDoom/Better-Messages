package com.imjustdoom.bettermessages.config;

import com.imjustdoom.bettermessages.BetterMessages;
import com.imjustdoom.bettermessages.message.Message;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Config {

    public static boolean DISABLE_OUTDATED_CONFIG_WARNING;

    public static int CONFIG_VERSION;

    public static List<Message> MESSAGES = new ArrayList<>();

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

        for(String msg : getConfig().getConfigurationSection("messages").getKeys(false)) {
            Message message = new Message();
            message.setEnabled(getConfig().getBoolean("messages." + msg + ".enabled"));
            message.setPermissionString("bettermessages." + msg);
            if(getConfig().isString("messages." + msg + ".permission")) {
                System.out.println("[BetterMessages] The permission option in the config is now a boolean (true/false). Please update your config.");
                message.setPermission(false);
            } else {
                message.setPermission(getConfig().getBoolean("messages." + msg + ".permission"));
            }
            message.setActivation(getConfig().getStringList("messages." + msg + ".activation"));
            message.setAudience(getConfig().getString("messages." + msg + ".audience"));
            message.setParent(msg);
            message.setCommands(getConfig().getStringList("messages." + msg + ".commands"));
            message.setDelay(getConfig().getInt("messages." + msg + ".delay"));
            message.setStorageType(getConfig().getString("messages." + msg + ".storage-type"));

            message.setMessage(getConfig().getStringList("messages." + msg + ".message"));

            if (getConfig().isString("messages." + msg + ".message")) {
                message.setMessage(Collections.singletonList(getConfig().getString("messages." + msg + ".message")));
            } else {
                message.setMessage(getConfig().getStringList("messages." + msg + ".message"));
            }

            if (getConfig().isInt("messages." + msg + ".count")) {
                message.setCount(getConfig().getInt("messages." + msg + ".count") == -1
                        ? Arrays.asList(-1)
                        : Arrays.asList(getConfig().getInt("messages." + msg + ".count")));
            } else {
                message.setCount(getConfig().getIntegerList("messages." + msg + ".count"));
            }

            message.setDontRunIf(getConfig().getString("messages." + msg + ".world") == null ? "" :
                    getConfig().getString("messages." + msg + ".world"));

            MESSAGES.add(message);
        }
    }

    private static FileConfiguration getConfig() {
        return BetterMessages.getInstance().getConfig();
    }
}
