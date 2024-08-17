package com.imjustdoom.bettermessages;

import com.imjustdoom.bettermessages.config.ConfigLoader;
import com.imjustdoom.bettermessages.config.Configuration;
import com.imjustdoom.bettermessages.message.Message;

import java.util.ArrayList;
import java.util.List;

public class BetterMessagesCore {

    private final List<Message> messages;

    public BetterMessagesCore(String version, ConfigLoader configLoader) {
        this.messages = new ArrayList<>();

        // TODO: Maybe pass it through using the load method?
        Configuration.CONFIG_LOADER = configLoader;
        Configuration.load();

        // TODO: Get the api back up again
//        try {
//            UpdateChecker.checkUpdates(version);
//        } catch (IOException exception) {
//
//            // TODO: Use a logger
//            System.out.println(exception.getMessage());
//        } catch (URISyntaxException e) {
//            throw new RuntimeException(e);
//        }
    }

    public List<Message> getMessages() {
        return this.messages;
    }
}
