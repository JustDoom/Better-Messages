package com.imjustdoom.bettermessages.old.manager;

import org.bukkit.World;

import java.util.*;

public class PlayerManager {

    // I think this was for making sure a player is loaded before sending a message. I think it's coded weirdly
    // It's been so long since I wrote this

    public static List<UUID> cachedPlayers = new ArrayList<>();

    public static void addPlayer(UUID uuid){
        cachedPlayers.add(uuid);
    }

    public static void removePlayer(UUID uuid){
        cachedPlayers.remove(uuid);
    }

    public static Map<UUID, World> waiting = new HashMap<>();

    public static void addWaitingPlayer(UUID uuid, World world){
        waiting.put(uuid, world);
    }

    public static void removeWaitingPlayer(UUID uuid){
        waiting.remove(uuid);
    }
}