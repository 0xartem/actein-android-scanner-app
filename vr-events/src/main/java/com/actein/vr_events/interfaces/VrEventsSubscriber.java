package com.actein.vr_events.interfaces;

public interface VrEventsSubscriber
{
    void subscribeToAll() throws VrEventsException;
    void unsubscribeFromAll() throws VrEventsException;
    void subscribeToStatusEvent() throws VrEventsException;
    void unsubscribeFromStatusEvent()throws VrEventsException;

}
