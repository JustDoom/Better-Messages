package com.justdoom.bettermessages.manager;

import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class PlayerManager {

    public static List<UUID> cachedPlayers = new ArrayList<>();

    public static void addPlayer(UUID uuid){
        cachedPlayers.add(uuid);
    }

    public static void removePlayer(UUID uuid){
        cachedPlayers.remove(uuid);
    }
}