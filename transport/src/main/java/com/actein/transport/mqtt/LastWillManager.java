package com.actein.transport.mqtt;

import com.actein.transport.mqtt.actions.Action;
import com.actein.transport.mqtt.actions.ActionStatusObserver;
import com.actein.transport.mqtt.actions.CommonActionListener;
import com.actein.transport.mqtt.interfaces.MessageHandler;
import com.actein.transport.mqtt.interfaces.PcOnlineStatusHandler;
import com.actein.transport.mqtt.interfaces.Publisher;
import com.actein.transport.mqtt.interfaces.Subscriber;
import com.google.protobuf.InvalidProtocolBufferException;

import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

public class LastWillManager implements MessageHandler
{
    public LastWillManager(Connection connection,
                           ActionStatusObserver actionObserver,
                           PcOnlineStatusHandler pcOnlineStatusHandler,
                           int boothId)
    {
        mPublisher = connection.getPublisher();
        mSubscriber = connection.getSubscriber();
        mPcOnlineStatusHandler = pcOnlineStatusHandler;
        mActionObserver = actionObserver;

        mPcLastWillTopic = Topics.PC_ONLINE_STATUS.replace(Topics.BOOTH_ID,
                                                           Integer.toString(boothId));
        mEmbDeviceLastWillTopic = Topics.EMB_DEVICE_ONLINE_STATUS.replace(Topics.BOOTH_ID,
                                                                          Integer.toString(boothId));
    }

    public synchronized void start() throws MqttException
    {
        mIsRunning = true;
        subscribeToPcLastWill();
        publishEmbDeviceOnlineStatus(true);
    }

    public synchronized void stop() throws MqttException
    {
        unsubscribeFromPcLastWill();
        publishEmbDeviceOnlineStatus(false);
        mIsRunning = false;
    }

    public synchronized boolean isRunning()
    {
        return mIsRunning;
    }

    public void publishEmbDeviceOnlineStatus(boolean online) throws MqttException
    {
        OnlineStatusProtos.OnlineStatus status = OnlineStatusProtos.OnlineStatus.OFFLINE;
        if (online)
        {
            status = OnlineStatusProtos.OnlineStatus.ONLINE;
        }

        OnlineStatusProtos.OnlineStatusEvent event = OnlineStatusProtos.OnlineStatusEvent
                .newBuilder()
                .setStatus(status)
                .build();

        mPublisher.publish(mEmbDeviceLastWillTopic,
                           event,
                           new CommonActionListener(Action.PUBLISH, mActionObserver));
    }

    public void subscribeToPcLastWill() throws MqttException
    {
        mSubscriber.subscribe(mPcLastWillTopic,
                              new CommonActionListener(Action.SUBSCRIBE, mActionObserver));
    }

    public void unsubscribeFromPcLastWill() throws MqttException
    {
        mSubscriber.unsubscribe(mPcLastWillTopic,
                                new CommonActionListener(Action.UNSUBSCRIBE, mActionObserver));
    }

    public void handleMessage(String topic, MqttMessage message) throws Exception
    {
        if (topic.equals(mPcLastWillTopic))
        {
            processOnlineStatusEvent(message);
        }
    }

    private void processOnlineStatusEvent(MqttMessage message) throws InvalidProtocolBufferException
    {
        OnlineStatusProtos.OnlineStatusEvent event = OnlineStatusProtos.OnlineStatusEvent
                .parseFrom(message.getPayload());

        if (mPcOnlineStatusHandler != null)
        {
            mPcOnlineStatusHandler.onPcOnlineStatusChanged(event.getStatus());
        }
    }

    private boolean mIsRunning = false;
    private String mPcLastWillTopic;
    private String mEmbDeviceLastWillTopic;
    private Publisher mPublisher;
    private Subscriber mSubscriber;
    private PcOnlineStatusHandler mPcOnlineStatusHandler;
    private ActionStatusObserver mActionObserver;
}
