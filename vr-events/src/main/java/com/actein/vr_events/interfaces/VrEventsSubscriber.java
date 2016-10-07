package com.actein.vr_events.interfaces;

public interface VrEventsSubscriber
{
    void subscribe() throws VrEventsException;
    void unsubscribe() throws VrEventsException;
    void subscribeToStatusEvent() throws VrEventsException;
    void unsubscribeFromStatusEvent() throws VrEventsException;

}
