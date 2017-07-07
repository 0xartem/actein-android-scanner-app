package com.actein.vr_events.interfaces;

import com.actein.vr_events.VrBoothInfoProtos;
import com.actein.vr_events.VrGameErrorProtos;
import com.actein.vr_events.VrGameProtos;
import com.actein.vr_events.VrGameStatusProtos;

public interface VrEventsPublisher
{
    void publishVrGameOnEvent(VrBoothInfoProtos.VrBoothInfo vrBoothInfo,
                              VrGameProtos.VrGame vrGame) throws VrEventsException;

    void publishVrGameOffEvent(VrBoothInfoProtos.VrBoothInfo vrBoothInfo) throws VrEventsException;

    void publishVrGameStatusEvent(VrBoothInfoProtos.VrBoothInfo vrBoothInfo,
                                  VrGameStatusProtos.VrGameStatus status) throws VrEventsException;

    void publishVrGameStatusEvent(VrBoothInfoProtos.VrBoothInfo vrBoothInfo,
                                  VrGameStatusProtos.VrGameStatus status,
                                  VrGameErrorProtos.VrGameError error) throws VrEventsException;
}
