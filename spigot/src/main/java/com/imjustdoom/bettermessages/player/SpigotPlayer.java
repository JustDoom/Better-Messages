package com.imjustdoom.bettermessages.player;

import org.bukkit.entity.Player;

import java.util.UUID;

public class SpigotPlayer implements BMPlayer {

    private final Player player;

    public SpigotPlayer(Player player) {
        this.player = player;
    }

    @Override
    public String getUsername() {
        return this.player.getName();
    }

    @Override
    public UUID getUuid() {
        return this.player.getUniqueId();
    }
}