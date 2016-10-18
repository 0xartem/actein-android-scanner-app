package com.actein.vr_events.interfaces;

import com.actein.transport.mqtt.actions.ActionStatusObserver;
import com.actein.transport.mqtt.interfaces.ConnectionObserver;

public interface VrEventsManager
{
    void start(VrEventsHandler vrEventsHandler,
               ConnectionObserver connectionObserver,
               ActionStatusObserver actionObserver) throws VrEventsException;

    void stop() throws VrEventsException;
    boolean isRunning();

    VrEventsPublisher getPublisher();
    VrEventsSubscriber getSubscriber();
}
