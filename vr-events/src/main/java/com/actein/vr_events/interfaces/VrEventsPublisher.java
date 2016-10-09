package com.actein.vr_events.interfaces;

import com.actein.transport.mqtt.interfaces.ActionStatusObserver;
import com.actein.vr_events.VrGameStatusProtos;

public interface VrEventsPublisher
{
    void publishVrGameOnEvent(ActionStatusObserver actionStatusObserver) throws VrEventsException;
    void publishVrGameOffEvent(ActionStatusObserver actionStatusObserver) throws VrEventsException;
    void publishVrGameStatusEvent(VrGameStatusProtos.VrGameStatus status,
                                  ActionStatusObserver actionStatusObserver) throws VrEventsException;
}
