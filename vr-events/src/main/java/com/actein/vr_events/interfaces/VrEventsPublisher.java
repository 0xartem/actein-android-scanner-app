package com.actein.vr_events.interfaces;

import com.actein.vr_events.VrGameStatusProtos;

public interface VrEventsPublisher
{
    void publishVrGameOnEvent() throws VrEventsException;
    void publishVrGameOffEvent() throws VrEventsException;
    void publishVrGameStatusEvent(VrGameStatusProtos.VrGameStatus status) throws VrEventsException;
}
