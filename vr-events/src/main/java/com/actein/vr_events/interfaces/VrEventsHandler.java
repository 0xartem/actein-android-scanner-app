package com.actein.vr_events.interfaces;

import com.actein.vr_events.VrBoothInfoProtos;
import com.actein.vr_events.VrGameOffProtos;
import com.actein.vr_events.VrGameOnProtos;
import com.actein.vr_events.VrGameStatusProtos;

public interface VrEventsHandler
{
    void handleVrGameOnEvent(VrBoothInfoProtos.VrBoothInfo boothInfo,
                             VrGameOnProtos.VrGameOnEvent event);

    void handleVrGameOffEvent(VrBoothInfoProtos.VrBoothInfo boothInfo,
                              VrGameOffProtos.VrGameOffEvent event);

    void handleVrGameStatusEvent(VrBoothInfoProtos.VrBoothInfo boothInfo,
                                 VrGameStatusProtos.VrGameStatusEvent event);
}
