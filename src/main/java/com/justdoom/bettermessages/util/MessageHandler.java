package com.justdoom.bettermessages.util;

import me.clip.placeholderapi.PlaceholderAPI;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;
import java.util.Random;

public class MessageHandler {

    private String message;

    public String doMessage(Player player, String path, JavaPlugin plugin) {
        if (plugin.getConfig().isList(path + ".message")) {
            List<String> list = plugin.getConfig().getStringList(path + ".message");
            this.message = list.get((new Random()).nextInt(list.size()));
        } else if (plugin.getConfig().isString(path + ".message")) {
            this.message = plugin.getConfig().getString(path + ".message");
        } else {
            plugin.getLogger().warning("Invalid join message.");
        }
        this.message = ChatColor.translateAlternateColorCodes('&', this.message);
        this.message = this.message.replace("{player}", player.getName());
        this.message = this.message.replace("{world}", player.getWorld().getName());
        this.message = this.message.replace("{line}", "\n");
        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null)
            this.message = PlaceholderAPI.setPlaceholders(player, this.message);
        return this.message;
    }

    public void messageType(Player player, String msg, JavaPlugin plugin, String path){
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
}
