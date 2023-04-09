package com.imjustdoom.bettermessages.player;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
public class BMPlayer {

    private String username;
    private UUID uuid;
}