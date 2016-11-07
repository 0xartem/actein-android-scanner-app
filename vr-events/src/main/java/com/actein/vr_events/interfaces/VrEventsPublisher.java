package com.actein.vr_events.interfaces;

import com.actein.vr_events.VrGameErrorProtos;
import com.actein.vr_events.VrGameProtos;
import com.actein.vr_events.VrGameStatusProtos;

public interface VrEventsPublisher
{
    void publishVrGameOnEvent(VrGameProtos.VrGame vrGame) throws VrEventsException;
    void publishVrGameOffEvent() throws VrEventsException;
    void publishVrGameStatusEvent(VrGameStatusProtos.VrGameStatus status) throws VrEventsException;
    void publishVrGameStatusEvent(VrGameStatusProtos.VrGameStatus status,
                                  VrGameErrorProtos.VrGameError error) throws VrEventsException;
}
