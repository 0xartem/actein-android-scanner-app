package com.actein.vr_events.interfaces;

import com.actein.vr_events.VrGameOffProtos;
import com.actein.vr_events.VrGameOnProtos;

public interface VrEventsPublisher
{
    void publishVrGameOnEvent();
    void publishVrGameOffEvent();
}
