package com.imjustdoom.bettermessages.old.message;

import com.imjustdoom.bettermessages.old.message.msg.JoinMessage;
import com.imjustdoom.bettermessages.old.message.msg.QuitMessage;
import com.imjustdoom.bettermessages.old.message.msg.SwitchServerMessage;
import com.imjustdoom.bettermessages.old.message.msg.WorldChangeMessage;

public enum EventType {

    JOIN(JoinMessage.class),
    QUIT(QuitMessage.class),
    WORLD_CHANGE(WorldChangeMessage.class),
    SERVER_SWITCH(SwitchServerMessage.class);

    private final Class<? extends Message> clazz;

    EventType(Class<? extends Message> clazz) {
        this.clazz = clazz;
    }

    public Class<? extends Message> getClazz() {
        return clazz;
    }
}
