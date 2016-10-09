package com.actein.vr_events.interfaces;

import com.actein.transport.mqtt.interfaces.ActionStatusObserver;

public interface VrEventsSubscriber
{
    void subscribe(ActionStatusObserver actionStatusObserver) throws VrEventsException;
    void unsubscribe(ActionStatusObserver actionStatusObserver) throws VrEventsException;
    void subscribeToStatusEvent(ActionStatusObserver actionStatusObserver) throws VrEventsException;
    void unsubscribeFromStatusEvent(ActionStatusObserver actionStatusObserver) throws VrEventsException;

}
