package com.actein.vr_events;

import com.actein.transport.mqtt.interfaces.ActionStatusObserver;
import com.actein.transport.mqtt.interfaces.ConnectionObserver;
import com.actein.transport.mqtt.interfaces.MessageHandler;
import com.actein.transport.mqtt.MqttSubscriberCallback;
import com.actein.transport.mqtt.interfaces.Subscriber;
import com.actein.transport.mqtt.actions.Action;
import com.actein.transport.mqtt.actions.CommonActionListener;
import com.actein.vr_events.interfaces.VrEventsException;
import com.actein.vr_events.interfaces.VrEventsHandler;
import com.actein.vr_events.interfaces.VrEventsSubscriber;
import com.google.protobuf.InvalidProtocolBufferException;

import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

public class MqttVrEventsSubscriber implements VrEventsSubscriber, MessageHandler
{
    public MqttVrEventsSubscriber(
            Subscriber subscriber,
            ConnectionObserver connectionObserver,
            VrEventsHandler vrEventsHandler
            )
    {
        mVrEventsHandler = vrEventsHandler;
        mSubscriber = subscriber;
        mSubscriber.setupCallback(new MqttSubscriberCallback(this, connectionObserver));
    }

    @Override
    public void subscribe(ActionStatusObserver actionStatusObserver) throws VrEventsException
    {
        try
        {
            mSubscriber.subscribe(VrTopics.VR_PC_GAME_ALL,
                                  new CommonActionListener(Action.SUBSCRIBE, actionStatusObserver));
        }
        catch (MqttException ex)
        {
            throw new VrEventsException("Can not subscribe to all vr events", ex);
        }
    }

    @Override
    public void unsubscribe(ActionStatusObserver actionStatusObserver) throws VrEventsException
    {
        try
        {
            mSubscriber.unsubscribe(VrTopics.VR_PC_GAME_ALL,
                                    new CommonActionListener(Action.UNSUBSCRIBE, actionStatusObserver));
        }
        catch (MqttException ex)
        {
            throw new VrEventsException("Can not unsubscribe from all vr events", ex);
        }
    }

    @Override
    public void subscribeToStatusEvent(ActionStatusObserver actionStatusObserver)
            throws VrEventsException
    {
        try
        {
            mSubscriber.subscribe(VrTopics.VR_PC_GAME_STATUS,
                                  new CommonActionListener(Action.SUBSCRIBE, actionStatusObserver));
        }
        catch (MqttException ex)
        {
            throw new VrEventsException("Can not subscribe to vr status event", ex);
        }
    }

    @Override
    public void unsubscribeFromStatusEvent(ActionStatusObserver actionStatusObserver)
            throws VrEventsException
    {
        try
        {
            mSubscriber.unsubscribe(VrTopics.VR_PC_GAME_ALL,
                                    new CommonActionListener(Action.UNSUBSCRIBE, actionStatusObserver));
        }
        catch (MqttException ex)
        {
            throw new VrEventsException("Can not unsubscribe from vr status event", ex);
        }
    }

    @Override
    public void handleMessage(String topic, MqttMessage message) throws Exception
    {
        switch (topic)
        {
        case VrTopics.VR_PC_TURN_GAME_ON:
            processVrOnEvent(message);
            break;
        case VrTopics.VR_PC_TURN_GAME_OFF:
            processVrOffEvent(message);
            break;
        case VrTopics.VR_PC_GAME_STATUS:
            processVrStatusEvent(message);
            break;
        default:
            throw new UnsupportedOperationException("Unknown vr event message type");
        }
    }

    private void processVrOnEvent(MqttMessage message) throws InvalidProtocolBufferException
    {
        VrGameOnProtos.VrGameOnEvent event = VrGameOnProtos.VrGameOnEvent
                .parseFrom(message.getPayload());
        if (mVrEventsHandler != null)
        {
            mVrEventsHandler.handleVrGameOnEvent(event);
        }
    }

    private void processVrOffEvent(MqttMessage message) throws InvalidProtocolBufferException
    {
        VrGameOffProtos.VrGameOffEvent event = VrGameOffProtos.VrGameOffEvent
                .parseFrom(message.getPayload());
        if (mVrEventsHandler != null)
        {
            mVrEventsHandler.handleVrGameOffEvent(event);
        }
    }

    private void processVrStatusEvent(MqttMessage message) throws InvalidProtocolBufferException
    {
        VrGameStatusProtos.VrGameStatusEvent event = VrGameStatusProtos.VrGameStatusEvent
                .parseFrom(message.getPayload());
        if (mVrEventsHandler != null)
        {
            mVrEventsHandler.handleVrGameStatusEvent(event);
        }
    }

    private VrEventsHandler mVrEventsHandler;
    private Subscriber mSubscriber;
}
