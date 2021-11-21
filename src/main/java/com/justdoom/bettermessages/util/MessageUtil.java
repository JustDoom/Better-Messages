package com.justdoom.bettermessages.util;

import com.justdoom.bettermessages.sqlite.SQLite;
import me.clip.placeholderapi.PlaceholderAPI;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MessageUtil {

    public static String doMessage(Player player, String path, JavaPlugin plugin) {
        String message = "";
        if (plugin.getConfig().isList(path + ".message")) {
            List<String> list = plugin.getConfig().getStringList(path + ".message");
            message = list.get((new Random()).nextInt(list.size()));
        } else if (plugin.getConfig().isString(path + ".message")) {
            message = plugin.getConfig().getString(path + ".message");
        } else {
            plugin.getLogger().warning("Invalid join message.");
        }
        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null)
            message = PlaceholderAPI.setPlaceholders(player, message);
        message = translate(message);
        message = message.replace("{player}", player.getName());
        message = message.replace("{world}", player.getWorld().getName());
        message = message.replace("{line}", "\n");
        return message;
    }

    public static void messageType(Player player, String msg, JavaPlugin plugin, String path){
        if(plugin.getConfig().getBoolean(path + ".message-type.chat-message")) {
            player.sendMessage(msg);
        }
        if(plugin.getConfig().getBoolean(path + ".message-type.title-message")) {
            player.sendTitle(msg, "bottom", 10, 40, 10);
        }
        if(plugin.getConfig().getBoolean(path + ".message-type.action-bar")) {
            player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(msg));
        }
    }

    public static String translate(String message) {
        Pattern pattern = Pattern.compile("#[a-fA-F0-9]{6}");
        Matcher matcher = pattern.matcher(message);
        while (matcher.find()) {
            String hexCode = message.substring(matcher.start(), matcher.end());
            String replaceSharp = hexCode.replace('#', 'x');

            char[] ch = replaceSharp.toCharArray();
            StringBuilder builder = new StringBuilder("");
            for (char c : ch) {
                builder.append("&" + c);
            }

            message = message.replace(hexCode, builder.toString());
            matcher = pattern.matcher(message);
        }
        return ChatColor.translateAlternateColorCodes('&', message);
    }
}
