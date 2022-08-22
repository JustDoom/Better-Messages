package com.justdoom.bettermessages.message;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Random;

@Getter
@Setter
public class Message {

    private String parent;
    private List<String> message, commands, activation;
    private List<Integer> count;
    private boolean permission, enabled;
    private String audience, storageType, dontRunIf, permissionString;
    private long delay;

    public String getMessage() {
        if (message.size() == 1) return message.get(0);
        return message.get((new Random()).nextInt(message.size()));
    }
}
