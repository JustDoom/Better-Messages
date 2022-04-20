package com.justdoom.bettermessages.message;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Random;

@Getter
@Setter
public class Message {

    private String parent;
    private List<String> message;
    private List<Integer> count;
    private boolean permission;
    private String permissionString;
    private boolean enabled;
    private List<String> activation;
    private String audience;
    private List<String> commands;
    private long delay;
    private String dontRunIf;

    public String getMessage() {
        if (message.size() == 1) return message.get(0);
        return message.get((new Random()).nextInt(message.size()));
    }
}
