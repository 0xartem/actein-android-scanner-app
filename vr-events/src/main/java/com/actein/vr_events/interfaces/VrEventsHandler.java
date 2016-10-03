package com.actein.vr_events.interfaces;

import com.actein.vr_events.VrGameOffProtos;
import com.actein.vr_events.VrGameOnProtos;

public interface VrEventsHandler
{
    void handleVrGameOnEvent(VrGameOnProtos.VrGameOnEvent event);
    void handleVrGameOffEvent(VrGameOffProtos.VrGameOffEvent event);
}
