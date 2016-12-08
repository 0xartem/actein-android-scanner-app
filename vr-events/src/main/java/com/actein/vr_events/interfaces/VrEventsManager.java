package com.actein.vr_events.interfaces;

import com.actein.transport.mqtt.interfaces.MessageHandler;

public interface VrEventsManager
{
    void start() throws VrEventsException;
    void stop() throws VrEventsException;
    boolean isRunning();

    VrEventsPublisher getPublisher();
    VrEventsSubscriber getSubscriber();
    MessageHandler getMessageHandler();
}
