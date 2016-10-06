package com.actein.vr_events.interfaces;

import com.actein.vr_events.VrGameOffProtos;
import com.actein.vr_events.VrGameOnProtos;
import com.actein.vr_events.VrGameStatusProtos;

public interface VrEventsHandler
{
    void handleVrGameOnEvent(VrGameOnProtos.VrGameOnEvent event);
    void handleVrGameOffEvent(VrGameOffProtos.VrGameOffEvent event);
    void handleVrGameStatusEvent(VrGameStatusProtos.VrGameStatusEvent event);
}
