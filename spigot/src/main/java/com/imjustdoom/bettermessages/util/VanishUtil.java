package com.imjustdoom.bettermessages.util;

import org.bukkit.entity.Player;
import org.bukkit.metadata.MetadataValue;

public class VanishUtil {

    public static boolean isVanished(Player player) {
        for (MetadataValue meta : player.getMetadata("vanished")) {
            if (meta.asBoolean()) {
                return true;
            }
        }
        return false;
    }
}