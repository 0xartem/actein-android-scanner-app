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
import com.actein.vr_events.topics.VrTopicBuilder;
import com.google.protobuf.InvalidProtocolBufferException;

import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

class MqttVrEventsSubscriber implements VrEventsSubscriber, MessageHandler
{
    MqttVrEventsSubscriber(
            Subscriber subscriber,
            VrBoothInfoProtos.VrBoothInfo vrBoothInfo,
            ConnectionObserver connectionObserver,
            VrEventsHandler vrEventsHandler
            )
    {
        mVrBoothInfo = vrBoothInfo;
        mSubscriber = subscriber;
        mVrEventsHandler = vrEventsHandler;
        mSubscriber.setupCallback(new MqttSubscriberCallback(this, connectionObserver));

        mAllVrEventsTopic = new VrTopicBuilder().setToAll()
                                                .setBoothId(mVrBoothInfo.getId())
                                                .build();

        mGameStatusVrTopic = new VrTopicBuilder().setToGameStatus()
                                                 .setBoothId(mVrBoothInfo.getId())
                                                 .build();

        mGameOnVrTopic = new VrTopicBuilder().setToGameOn()
                                             .setBoothId(mVrBoothInfo.getId())
                                             .build();

        mGameOffVrTopic = new VrTopicBuilder().setToGameOff()
                                              .setBoothId(mVrBoothInfo.getId())
                                              .build();
    }

    @Override
    public void subscribeToAll(ActionStatusObserver actionStatusObserver) throws VrEventsException
    {
        try
        {
            mSubscriber.subscribe(mAllVrEventsTopic,
                                  new CommonActionListener(Action.SUBSCRIBE, actionStatusObserver));
        }
        catch (MqttException ex)
        {
            throw new VrEventsException("Can not subscribe to all vr events", ex);
        }
    }

    @Override
    public void unsubscribeFromAll(ActionStatusObserver actionStatusObserver)
            throws VrEventsException
    {
        try
        {
            mSubscriber.unsubscribe(mAllVrEventsTopic,
                                    new CommonActionListener(Action.UNSUBSCRIBE,actionStatusObserver));
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
            mSubscriber.subscribe(mGameStatusVrTopic,
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
            mSubscriber.unsubscribe(mGameStatusVrTopic,
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
        if (topic.equals(mGameOnVrTopic))
        {
            processVrOnEvent(message);
        }
        else if (topic.equals(mGameOffVrTopic))
        {
            processVrOffEvent(message);
        }
        else if (topic.equals(mGameStatusVrTopic))
        {
            processVrStatusEvent(message);
        }
        else
        {
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

    private String mAllVrEventsTopic;
    private String mGameStatusVrTopic;
    private String mGameOnVrTopic;
    private String mGameOffVrTopic;
    private VrEventsHandler mVrEventsHandler;
    private Subscriber mSubscriber;
    private VrBoothInfoProtos.VrBoothInfo mVrBoothInfo;
}
