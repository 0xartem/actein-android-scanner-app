package com.actein.vr_events;

import android.util.Log;

import com.actein.transport.mqtt.actions.ActionStatusObserver;
import com.actein.transport.mqtt.interfaces.MessageHandler;
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
            VrEventsHandler vrEventsHandler,
            ActionStatusObserver actionObserver)
    {
        mSubscriber = subscriber;
        mVrEventsHandler = vrEventsHandler;

        mSubscribeListener = new CommonActionListener(Action.SUBSCRIBE, actionObserver);
        mUnsubscribeListener = new CommonActionListener(Action.UNSUBSCRIBE, actionObserver);

        mAllVrEventsTopic = new VrTopicBuilder().setToAll()
                                                .setBoothId(vrBoothInfo.getId())
                                                .build();

        mGameStatusVrTopic = new VrTopicBuilder().setToGameStatus()
                                                 .setBoothId(vrBoothInfo.getId())
                                                 .build();

        mGameOnVrTopic = new VrTopicBuilder().setToGameOn()
                                             .setBoothId(vrBoothInfo.getId())
                                             .build();

        mGameOffVrTopic = new VrTopicBuilder().setToGameOff()
                                              .setBoothId(vrBoothInfo.getId())
                                              .build();
    }

    @Override
    public void subscribeToAll() throws VrEventsException
    {
        try
        {
            mSubscriber.subscribe(mAllVrEventsTopic, mSubscribeListener);
        }
        catch (MqttException ex)
        {
            Log.e(TAG, ex.toString(), ex);
            throw new VrEventsException("Can not subscribe to all vr events", ex);
        }
    }

    @Override
    public void unsubscribeFromAll() throws VrEventsException
    {
        try
        {
            mSubscriber.unsubscribe(mAllVrEventsTopic, mUnsubscribeListener);
        }
        catch (MqttException ex)
        {
            Log.e(TAG, ex.toString(), ex);
            throw new VrEventsException("Can not unsubscribe from all vr events", ex);
        }
    }

    @Override
    public void subscribeToStatusEvent() throws VrEventsException
    {
        try
        {
            mSubscriber.subscribe(mGameStatusVrTopic, mSubscribeListener);
        }
        catch (MqttException ex)
        {
            Log.e(TAG, ex.toString(), ex);
            throw new VrEventsException("Can not subscribe to vr status event", ex);
        }
    }

    @Override
    public void unsubscribeFromStatusEvent() throws VrEventsException
    {
        try
        {
            mSubscriber.unsubscribe(mGameStatusVrTopic, mUnsubscribeListener);
        }
        catch (MqttException ex)
        {
            Log.e(TAG, ex.toString(), ex);
            throw new VrEventsException("Can not unsubscribe from vr status event", ex);
        }
    }

    @Override
    public void handleMessage(String topic, MqttMessage message) throws Exception
    {
        if (topic.equals(mGameOnVrTopic))
        {
            processGameOnEvent(message);
        }
        else if (topic.equals(mGameOffVrTopic))
        {
            processGameOffEvent(message);
        }
        else if (topic.equals(mGameStatusVrTopic))
        {
            processVrStatusEvent(message);
        }
    }

    private void processGameOnEvent(MqttMessage message) throws InvalidProtocolBufferException
    {
        VrGameOnProtos.VrGameOnEvent event = VrGameOnProtos.VrGameOnEvent
                .parseFrom(message.getPayload());
        if (mVrEventsHandler != null)
        {
            mVrEventsHandler.handleVrGameOnEvent(event);
        }
    }

    private void processGameOffEvent(MqttMessage message) throws InvalidProtocolBufferException
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

    private final String mAllVrEventsTopic;
    private final String mGameStatusVrTopic;
    private final String mGameOnVrTopic;
    private final String mGameOffVrTopic;

    private Subscriber mSubscriber;
    private VrEventsHandler mVrEventsHandler;

    private CommonActionListener mSubscribeListener;
    private CommonActionListener mUnsubscribeListener;

    private static String TAG = MqttVrEventsSubscriber.class.getSimpleName();
}
