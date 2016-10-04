package com.actein.vr_events.interfaces;

public interface VrEventsPublisher
{
    void publishVrGameOnEvent() throws VrEventsException;
    void publishVrGameOffEvent() throws VrEventsException;
}
