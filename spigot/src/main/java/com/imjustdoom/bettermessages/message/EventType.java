package com.imjustdoom.bettermessages.message;

import com.imjustdoom.bettermessages.message.msg.JoinMessage;
import com.imjustdoom.bettermessages.message.msg.QuitMessage;
import com.imjustdoom.bettermessages.message.msg.SwitchServerMessage;
import com.imjustdoom.bettermessages.message.msg.WorldChangeMessage;

public enum EventType {

    JOIN(JoinMessage.class),
    QUIT(QuitMessage.class),
    WORLD_CHANGE(WorldChangeMessage.class),
    SERVER_SWITCH(SwitchServerMessage.class);

    private Class<? extends Message> clazz;

    EventType(Class<? extends Message> clazz) {
        this.clazz = clazz;
    }

    public Class<? extends Message> getClazz() {
        return clazz;
    }
}
