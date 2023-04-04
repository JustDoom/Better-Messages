package com.imjustdoom.bettermessages.message;

import com.imjustdoom.bettermessages.message.msg.JoinMessage;
import com.imjustdoom.bettermessages.message.msg.QuitMessage;
import com.imjustdoom.bettermessages.message.msg.SwitchServerMessage;
import com.imjustdoom.bettermessages.message.msg.WorldChangeMessage;

import java.util.List;

public class MessageBuilder {

    private String parent;
    private List<String> message;
    private List<String> commands;
    private List<Integer> count;
    private boolean permission;
    private boolean enabled;
    private String audience;
    private String storageType;
    private String dontRunIf;
    private String extraInfo;
    private long delay;
    private int priority;
    private String messageType;

    public Message build(EventType eventType) {
        switch (eventType) {
            case JOIN:
                return new JoinMessage(parent, message, commands, count, permission, enabled, audience, storageType, dontRunIf, delay, priority, messageType, extraInfo);
            case QUIT:
                return new QuitMessage(parent, message, commands, count, permission, enabled, audience, storageType, dontRunIf, delay, priority, messageType, extraInfo);
            case WORLD_CHANGE:
                return new WorldChangeMessage(parent, message, commands, count, permission, enabled, audience, storageType, dontRunIf, delay, priority, messageType, extraInfo);
            case SERVER_SWITCH:
                return new SwitchServerMessage(parent, message, commands, count, permission, enabled, audience, storageType, dontRunIf, delay, priority, messageType, extraInfo);
            default:
                throw new IllegalStateException("Unexpected value: " + eventType);
        }
    }

    // TODO: do proper checking for all of these
    public MessageBuilder setParent(String parent) {
        this.parent = parent;
        return this;
    }

    public MessageBuilder setMessage(List<String> message) {
        this.message = message;
        return this;
    }

    public MessageBuilder setCommands(List<String> commands) {
        this.commands = commands;
        return this;
    }

    public MessageBuilder setCount(List<Integer> count) {
        this.count = count;
        return this;
    }

    public MessageBuilder setPermission(boolean permission) {
        this.permission = permission;
        return this;
    }

    public MessageBuilder setEnabled(boolean enabled) {
        this.enabled = enabled;
        return this;
    }

    public MessageBuilder setAudience(String audience) {
        this.audience = audience;
        return this;
    }

    public MessageBuilder setStorageType(String storageType) {
        this.storageType = storageType;
        return this;
    }

    public MessageBuilder setDontRunIf(String dontRunIf) {
        this.dontRunIf = dontRunIf;
        return this;
    }

    public MessageBuilder setDelay(long delay) {
        this.delay = delay;
        return this;
    }

    public MessageBuilder setPriority(int priority) {
        this.priority = priority;
        return this;
    }

    public MessageBuilder setExtraInfo(String extraInfo) {
        this.extraInfo = extraInfo;
        return this;
    }

    public MessageBuilder setMessageType(String messageType) {
        this.messageType = messageType;
        return this;
    }
}
