package com.actein.vr_events.interfaces;

public interface VrEventsManager
{
    void start(VrEventsHandler vrEventsHandler) throws VrEventsException;
    void stop() throws VrEventsException;

    VrEventsPublisher getPublisher();
}
