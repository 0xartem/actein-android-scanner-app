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
import com.actein.transport.mqtt.BoothIdParser;
import com.actein.vr_events.topics.VrTopicBuilder;
import com.google.protobuf.InvalidProtocolBufferException;

import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.util.regex.Pattern;

class MqttVrEventsSubscriber implements VrEventsSubscriber, MessageHandler
{
    MqttVrEventsSubscriber(Subscriber subscriber,
                           ActionStatusObserver actionObserver)
    {
        mSubscriber = subscriber;

        mSubscribeListener = new CommonActionListener(Action.SUBSCRIBE, actionObserver);
        mUnsubscribeListener = new CommonActionListener(Action.UNSUBSCRIBE, actionObserver);

        mGameStatusVrTopicPattern = Pattern.compile(new VrTopicBuilder().setToGameStatus()
                                                                        .setBoothDigitPattern()
                                                                        .build());

        mGameOnVrTopicPattern = Pattern.compile(new VrTopicBuilder().setToGameOn()
                                                                    .setBoothDigitPattern()
                                                                    .build());

        mGameOffVrTopicPattern = Pattern.compile(new VrTopicBuilder().setToGameOff()
                                                                     .setBoothDigitPattern()
                                                                     .build());

        mAllVrEventsTopic = new VrTopicBuilder().setToGameAll()
                                                .setAllBooths()
                                                .build();

        mGameStatusVrTopic = new VrTopicBuilder().setToGameStatus()
                                                 .setAllBooths()
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
    public void registerVrEventsHandler(VrEventsHandler vrEventsHandler)
    {
        mVrEventsHandler = vrEventsHandler;
    }

    @Override
    public void handleMessage(String topic, MqttMessage message) throws Exception
    {
        if (mGameOnVrTopicPattern.matcher(topic).matches())
        {
            processGameOnEvent(topic, message);
        }
        else if (mGameOffVrTopicPattern.matcher(topic).matches())
        {
            processGameOffEvent(topic, message);
        }
        else if (mGameStatusVrTopicPattern.matcher(topic).matches())
        {
            processVrStatusEvent(topic, message);
        }
    }

    private void processGameOnEvent(String topic, MqttMessage message) throws InvalidProtocolBufferException
    {
        VrGameOnProtos.VrGameOnEvent event = VrGameOnProtos.VrGameOnEvent
                .parseFrom(message.getPayload());
        if (mVrEventsHandler != null)
        {
            mVrEventsHandler.handleVrGameOnEvent(buildBoothInfo(topic), event);
        }
    }

    private void processGameOffEvent(String topic, MqttMessage message) throws InvalidProtocolBufferException
    {
        VrGameOffProtos.VrGameOffEvent event = VrGameOffProtos.VrGameOffEvent
                .parseFrom(message.getPayload());
        if (mVrEventsHandler != null)
        {
            mVrEventsHandler.handleVrGameOffEvent(buildBoothInfo(topic), event);
        }
    }

    private void processVrStatusEvent(String topic, MqttMessage message) throws InvalidProtocolBufferException
    {
        VrGameStatusProtos.VrGameStatusEvent event = VrGameStatusProtos.VrGameStatusEvent
                .parseFrom(message.getPayload());
        if (mVrEventsHandler != null)
        {
            mVrEventsHandler.handleVrGameStatusEvent(buildBoothInfo(topic),event);
        }
    }

    private VrBoothInfoProtos.VrBoothInfo buildBoothInfo(String topic)
    {
        int boothId = BoothIdParser.parseBoothId(topic);
        return VrBoothInfoProtos.VrBoothInfo.newBuilder().setId(boothId).build();
    }

    private final Pattern mGameStatusVrTopicPattern;
    private final Pattern mGameOnVrTopicPattern;
    private final Pattern mGameOffVrTopicPattern;

    private final String mAllVrEventsTopic;
    private final String mGameStatusVrTopic;

    private Subscriber mSubscriber;
    private VrEventsHandler mVrEventsHandler;

    private CommonActionListener mSubscribeListener;
    private CommonActionListener mUnsubscribeListener;

    private static String TAG = MqttVrEventsSubscriber.class.getSimpleName();
}
