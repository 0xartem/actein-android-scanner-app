package com.actein.vr_events.interfaces;

import com.actein.transport.mqtt.interfaces.ActionStatusObserver;

public interface VrEventsSubscriber
{
    void subscribeToAll(ActionStatusObserver actionStatusObserver) throws VrEventsException;
    void unsubscribeFromAll(ActionStatusObserver actionStatusObserver) throws VrEventsException;
    void subscribeToStatusEvent(ActionStatusObserver actionStatusObserver) throws VrEventsException;
    void unsubscribeFromStatusEvent(ActionStatusObserver actionStatusObserver)throws VrEventsException;

}
