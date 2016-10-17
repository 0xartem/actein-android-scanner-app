package com.actein.vr_events.interfaces;

import com.actein.transport.mqtt.actions.ActionStatusObserver;
import com.actein.vr_events.VrGameProtos;
import com.actein.vr_events.VrGameStatusProtos;

public interface VrEventsPublisher
{
    void publishVrGameOnEvent(VrGameProtos.VrGame vrGame,
                              ActionStatusObserver actionStatusObserver) throws VrEventsException;

    void publishVrGameOffEvent(ActionStatusObserver actionStatusObserver) throws VrEventsException;

    void publishVrGameStatusEvent(VrGameStatusProtos.VrGameStatus status,
                                  ActionStatusObserver actionStatusObserver) throws VrEventsException;
}
