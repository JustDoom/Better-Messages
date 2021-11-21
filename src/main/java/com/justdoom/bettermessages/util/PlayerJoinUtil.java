package com.justdoom.bettermessages.util;

import java.util.HashMap;
import java.util.UUID;

public class PlayerJoinUtil {

    public static HashMap<UUID, Boolean> cachedPlayers = new HashMap<>();

    public static void addPlayer(UUID uuid, Boolean bool){
        cachedPlayers.put(uuid, bool);
    }

    public static Boolean getPlayer(UUID uuid){
        return cachedPlayers.get(uuid);
    }

    public static void removePlayer(UUID uuid){
        cachedPlayers.remove(uuid);
    }
}