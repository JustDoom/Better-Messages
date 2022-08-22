package com.justdoom.bettermessages.util;

import me.clip.placeholderapi.PlaceholderAPI;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MessageUtil {

    public static void messageType(Player player, String msg, JavaPlugin plugin, String path){
        if(plugin.getConfig().getBoolean(path + ".message-type.chat-message")) player.sendMessage(msg);
        if(plugin.getConfig().getBoolean(path + ".message-type.title-message")) player.sendTitle(msg, "bottom", 10, 40, 10);
        if(plugin.getConfig().getBoolean(path + ".message-type.action-bar")) player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(msg));
    }

    public static Pattern HEX_PATTERN = Pattern.compile("&#([A-Fa-f0-9]){6}");
    public static Pattern HEX_PATTERN_2 = Pattern.compile("#([A-Fa-f0-9]){6}");

    public static String translatePlaceholders(String message, Player player) {
        message = message.replace("{player}", player.getName());
        message = message.replace("{world}", player.getWorld().getName());
        message = message.replace("{line}", "\n");

        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null)
            message = PlaceholderAPI.setPlaceholders(player, message);

        return translate(message);
    }

    public static String translate(String message) {

        Matcher matcher = HEX_PATTERN.matcher(message);
        while (matcher.find()) {
            String hexCode = message.substring(matcher.start(), matcher.end());
            String replaceSharp = hexCode.replace("&#", "x");

            char[] ch = replaceSharp.toCharArray();
            StringBuilder builder = new StringBuilder("");
            for (char c : ch) {
                builder.append("&").append(c);
            }

            message = message.replace(hexCode, builder.toString());
            matcher = HEX_PATTERN.matcher(message);
        }

        matcher = HEX_PATTERN_2.matcher(message);
        while (matcher.find()) {
            String hexCode = message.substring(matcher.start(), matcher.end());
            String replaceSharp = hexCode.replace('#', 'x');

            char[] ch = replaceSharp.toCharArray();
            StringBuilder builder = new StringBuilder("");
            for (char c : ch) {
                builder.append("&").append(c);
            }

            message = message.replace(hexCode, builder.toString());
            matcher = HEX_PATTERN_2.matcher(message);
        }
        return ChatColor.translateAlternateColorCodes('&', message);
    }
}
