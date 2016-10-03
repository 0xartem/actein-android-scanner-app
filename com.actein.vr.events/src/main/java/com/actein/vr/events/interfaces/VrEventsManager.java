package com.actein.vr.events.interfaces;

public interface VrEventsManager
{
    void start();
    void stop();
    VrEventsPublisher getPublisher();
}
