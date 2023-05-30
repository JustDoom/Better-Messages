package com.imjustdoom.bettermessages;

import com.imjustdoom.bettermessages.config.ConfigLoader;
import com.imjustdoom.bettermessages.config.Configuration;
import com.imjustdoom.bettermessages.message.Message;
import com.imjustdoom.bettermessages.util.UpdateChecker;
import lombok.Getter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Getter
public class BetterMessagesCore {

    private final List<Message> messages;

    public BetterMessagesCore(String version, ConfigLoader configLoader) {
        this.messages = new ArrayList<>();

        // TODO: Maybe pass it through using the load method?
        Configuration.CONFIG_LOADER = configLoader;
        Configuration.load();

        try {
            UpdateChecker.checkUpdates(version);
        } catch (IOException exception) {

            // TODO: Use a logger
            System.out.println(exception.getMessage());
        }
    }
}
