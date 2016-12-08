package com.actein.vr_events;

import com.actein.transport.mqtt.Connection;
import com.actein.transport.mqtt.actions.ActionStatusObserver;
import com.actein.transport.mqtt.interfaces.MessageHandler;
import com.actein.vr_events.interfaces.VrEventsException;
import com.actein.vr_events.interfaces.VrEventsHandler;
import com.actein.vr_events.interfaces.VrEventsManager;
import com.actein.vr_events.interfaces.VrEventsPublisher;
import com.actein.vr_events.interfaces.VrEventsSubscriber;

public class MqttVrEventsManager implements VrEventsManager
{
    public MqttVrEventsManager(Connection connection,
                               VrEventsHandler vrEventsHandler,
                               ActionStatusObserver actionObserver,
                               VrBoothInfoProtos.VrBoothInfo vrBoothInfo)
    {
        mVrEventsPublisher = new MqttVrEventsPublisher(
                connection.getPublisher(),
                vrBoothInfo,
                actionObserver
        );

        mVrEventsSubscriber = new MqttVrEventsSubscriber(
                connection.getSubscriber(),
                vrBoothInfo,
                vrEventsHandler,
                actionObserver
        );
    }

    @Override
    public synchronized void start() throws VrEventsException
    {
        mIsRunning = true;
        mVrEventsSubscriber.subscribeToStatusEvent();
    }

    @Override
    public synchronized void stop() throws VrEventsException
    {
        mVrEventsSubscriber.unsubscribeFromStatusEvent();
        mIsRunning = false;
    }

    @Override
    public synchronized boolean isRunning()
    {
        return mIsRunning;
    }

    @Override
    public VrEventsPublisher getPublisher()
    {
        return mVrEventsPublisher;
    }

    @Override
    public VrEventsSubscriber getSubscriber()
    {
        return mVrEventsSubscriber;
    }

    @Override
    public MessageHandler getMessageHandler()
    {
        return mVrEventsSubscriber;
    }

    private boolean mIsRunning = false;
    private MqttVrEventsPublisher mVrEventsPublisher = null;
    private MqttVrEventsSubscriber mVrEventsSubscriber = null;
}