package com.actein.vr_events;

import com.actein.transport.mqtt.Connection;
import com.actein.transport.mqtt.actions.ActionStatusObserver;
import com.actein.transport.mqtt.interfaces.MessageHandler;
import com.actein.vr_events.interfaces.VrEventsException;
import com.actein.vr_events.interfaces.VrEventsManager;
import com.actein.vr_events.interfaces.VrEventsPublisher;
import com.actein.vr_events.interfaces.VrEventsSubscriber;

public class MqttVrEventsManager implements VrEventsManager
{
    public MqttVrEventsManager(Connection connection,
                               ActionStatusObserver actionObserver)
    {
        mVrEventsPublisher = new MqttVrEventsPublisher(
                connection.getPublisher(),
                actionObserver
        );

        mVrEventsSubscriber = new MqttVrEventsSubscriber(
                connection.getSubscriber(),
                actionObserver
        );
    }

    @Override
    public synchronized void start() throws VrEventsException
    {
        mIsRunning = true;
        mVrEventsSubscriber.subscribeToAll();
    }

    @Override
    public synchronized void stop() throws VrEventsException
    {
        mVrEventsSubscriber.unsubscribeFromAll();
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