package com.actein.vr.events.interfaces;

import com.actein.vr.events.VrGameOffProtos;
import com.actein.vr.events.VrGameOnProtos;

public interface VrEventsPublisher
{
    void publishVrGameOnEvent(VrGameOnProtos.VrGameOnEvent event);
    void publishVrGameOffEvent(VrGameOffProtos.VrGameOffEvent event);
}
