package com.imjustdoom.bettermessages.message;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Random;

@Getter
@Setter
public class Message {

    private String parent;
    private List<String> message, commands;
    private List<Integer> count;
    private boolean permission, enabled;
    private String audience, storageType, dontRunIf, permissionString;
    private long delay;

    public Message(String parent, List<String> message, List<String> commands, List<Integer> count, boolean permission, boolean enabled, String audience, String storageType, String dontRunIf, long delay) {
        this.parent = parent;
        this.message = message;
        this.commands = commands;
        this.count = count;
        this.permission = permission;
        this.enabled = enabled;
        this.audience = audience;
        this.storageType = storageType;
        this.dontRunIf = dontRunIf;
        this.permissionString = "bettermessages." + this.parent;
        this.delay = delay;
    }

    public String getMessage() {
        if (message.size() == 1) return message.get(0);
        return message.get((new Random()).nextInt(message.size()));
    }
}
