package com.actein.vr_events;

import android.util.Log;

import com.actein.transport.mqtt.MqttPublisher;
import com.actein.transport.mqtt.Topics;
import com.actein.vr_events.interfaces.VrEventsPublisher;

import org.eclipse.paho.client.mqttv3.MqttException;

public class MqttVrEventsPublisher implements VrEventsPublisher
{
    public MqttVrEventsPublisher(MqttPublisher mqttPublisher)
    {
        mMqttPublisher = mqttPublisher;
    }
    public void publishVrGameOnEvent()
    {
        try
        {
            VrGameOnProtos.VrGameOnEvent event = VrGameOnProtos.VrGameOnEvent.newBuilder().build();
            mMqttPublisher.publish(Topics.VR_GAME_ON, event);
        }
        catch (MqttException ex)
        {
            Log.e("", ex.getMessage(), ex);
        }
    }

    public void publishVrGameOffEvent()
    {
        try
        {
            VrGameOffProtos.VrGameOffEvent event = VrGameOffProtos.VrGameOffEvent.newBuilder().build();
            mMqttPublisher.publish(Topics.VR_GAME_OFF, event);
        }
        catch (MqttException ex)
        {
            Log.e("", ex.getMessage(), ex);
        }
    }

    private MqttPublisher mMqttPublisher;
}
