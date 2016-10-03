package com.actein.vr.events.interfaces;

import com.actein.vr.events.VrGameOffProtos;
import com.actein.vr.events.VrGameOnProtos;

public interface VrEventsHandler
{
    void handleVrGameOnEvent(VrGameOnProtos.VrGameOnEvent event);
    void handleVrGameOffEvent(VrGameOffProtos.VrGameOffEvent event);
}
