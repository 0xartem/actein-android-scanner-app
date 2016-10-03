package com.actein.vr_events.interfaces;

public interface VrEventsManager
{
    void start();
    void stop();
    VrEventsPublisher getPublisher();
}
