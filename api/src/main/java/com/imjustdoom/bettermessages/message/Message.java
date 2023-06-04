package com.imjustdoom.bettermessages.message;

import lombok.Getter;

@Getter
public class Message {

    private boolean enabled;
    private String message;

    public Message(boolean enabled, String message) {
        this.enabled = enabled;
        this.message = message;
    }
}