package com.justdoom.bettermessages.message;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class Message {

    private String parent;
    private String message;
    private List<Integer> count;
    private String permission;
    private boolean enabled;
    private List<String> activation;
    private String audience;
}
