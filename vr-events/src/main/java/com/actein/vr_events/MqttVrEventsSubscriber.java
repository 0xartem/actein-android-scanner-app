package com.actein.vr_events;

import android.util.Log;

import com.actein.transport.mqtt.MessageHandler;
import com.actein.transport.mqtt.MqttSubscriber;
import com.actein.transport.mqtt.MqttSubscriberCallback;
import com.actein.transport.mqtt.Topics;
import com.actein.vr_events.interfaces.VrEventsException;
import com.actein.vr_events.interfaces.VrEventsHandler;
import com.google.protobuf.InvalidProtocolBufferException;

import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

public class MqttVrEventsSubscriber implements MessageHandler
{
    public MqttVrEventsSubscriber(VrEventsHandler vrEventsHandler, MqttSubscriber mqttSubscriber)
    {
        mVrEventsHandler = vrEventsHandler;
        mMqttSubscriber = mqttSubscriber;
        mMqttSubscriber.setupCallback(new MqttSubscriberCallback(this));
    }

    public void subscribe() throws VrEventsException
    {
        try
        {
            mMqttSubscriber.subscribe(Topics.VR_GAME_ON);
            mMqttSubscriber.subscribe(Topics.VR_GAME_OFF);
        }
        catch (MqttException ex)
        {
            throw new VrEventsException(ex);
        }
    }

    public void unsubscribe() throws VrEventsException
    {
        try
        {
            mMqttSubscriber.unsubscribe(Topics.VR_GAME_OFF);
            mMqttSubscriber.unsubscribe(Topics.VR_GAME_ON);
        }
        catch (MqttException ex)
        {
            throw new VrEventsException(ex);
        }
    }

    @Override
    public void handleMessage(String topic, MqttMessage message) throws Exception
    {
        if (topic.equals(Topics.VR_GAME_ON))
        {
            processVrOnEvent(message);
        }
        else if (topic.equals(Topics.VR_GAME_OFF))
        {
            processVrOffEvent(message);
        }
    }

    private void processVrOnEvent(MqttMessage message) throws InvalidProtocolBufferException
    {
        VrGameOnProtos.VrGameOnEvent event = VrGameOnProtos.VrGameOnEvent.parseFrom(message.getPayload());
        if (mVrEventsHandler != null)
        {
            mVrEventsHandler.handleVrGameOnEvent(event);
        }
    }

    private void processVrOffEvent(MqttMessage message) throws InvalidProtocolBufferException
    {
        VrGameOffProtos.VrGameOffEvent event = VrGameOffProtos.VrGameOffEvent.parseFrom(message.getPayload());
        if (mVrEventsHandler != null)
        {
            mVrEventsHandler.handleVrGameOffEvent(event);
        }
    }

    private VrEventsHandler mVrEventsHandler;
    private MqttSubscriber mMqttSubscriber;

    private static String TAG = MqttVrEventsSubscriber.class.getSimpleName();
}
