package com.actein.vr_events;

import com.actein.transport.mqtt.MqttPublisher;
import com.actein.vr_events.interfaces.VrEventsException;
import com.actein.vr_events.interfaces.VrEventsPublisher;

import org.eclipse.paho.client.mqttv3.MqttException;

public class MqttVrEventsPublisher implements VrEventsPublisher
{
    public MqttVrEventsPublisher(MqttPublisher mqttPublisher)
    {
        mMqttPublisher = mqttPublisher;
    }

    @Override
    public void publishVrGameOnEvent() throws VrEventsException
    {
        try
        {
            VrGameOnProtos.VrGameOnEvent event = VrGameOnProtos.VrGameOnEvent.newBuilder().build();
            mMqttPublisher.publish(VrTopics.VR_PC_TURN_GAME_ON, event);
        }
        catch (MqttException ex)
        {
            throw new VrEventsException("Can not publish vr game turn off event", ex);
        }
    }

    @Override
    public void publishVrGameOffEvent() throws VrEventsException
    {
        try
        {
            VrGameOffProtos.VrGameOffEvent event = VrGameOffProtos.VrGameOffEvent.newBuilder().build();
            mMqttPublisher.publish(VrTopics.VR_PC_TURN_GAME_OFF, event);
        }
        catch (MqttException ex)
        {
            throw new VrEventsException("Can not publish vr game turn on event", ex);
        }
    }

    @Override
    public void publishVrGameStatusEvent(VrGameStatusProtos.VrGameStatus status)
            throws VrEventsException
    {
        try
        {
            VrGameStatusProtos.VrGameStatusEvent event = VrGameStatusProtos.VrGameStatusEvent
                    .newBuilder()
                    .setStatus(status)
                    .build();
            mMqttPublisher.publish(VrTopics.VR_PC_GAME_STATUS, event);
        }
        catch (MqttException ex)
        {
            throw new VrEventsException("Can not publish vr game status event", ex);
        }
    }

    private MqttPublisher mMqttPublisher;
}
