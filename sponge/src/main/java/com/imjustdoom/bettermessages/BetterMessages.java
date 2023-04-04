package com.imjustdoom.bettermessages;

import com.google.inject.Inject;
import lombok.Getter;
import org.apache.logging.log4j.Logger;
import org.spongepowered.api.Server;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.lifecycle.StartedEngineEvent;
import org.spongepowered.plugin.builtin.jvm.Plugin;

@Getter
@Plugin("bettermessages")
public class BetterMessages {

    @Inject
    private Logger logger;

    @Listener
    public void onServerStart(final StartedEngineEvent<Server> event) {
        logger.info("Successfully running ExamplePlugin!!!");
    }
}