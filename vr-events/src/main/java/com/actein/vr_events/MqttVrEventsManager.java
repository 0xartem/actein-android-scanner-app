package com.actein.vr_events;

import com.actein.transport.mqtt.Connection;
import com.actein.transport.mqtt.interfaces.ConnectionObserver;
import com.actein.vr_events.interfaces.VrEventsException;
import com.actein.vr_events.interfaces.VrEventsHandler;
import com.actein.vr_events.interfaces.VrEventsManager;
import com.actein.vr_events.interfaces.VrEventsPublisher;
import com.actein.vr_events.interfaces.VrEventsSubscriber;

public class MqttVrEventsManager implements VrEventsManager
{
    public MqttVrEventsManager(Connection connection)
    {
        mConnection = connection;
    }

    @Override
    public void start(VrEventsHandler vrEventsHandler, ConnectionObserver connectionObserver)
            throws VrEventsException
    {
        mVrEventsPublisher = new MqttVrEventsPublisher(mConnection.getPublisher());

        mVrEventsSubscriber = new MqttVrEventsSubscriber(
                mConnection.getSubscriber(),
                connectionObserver,
                vrEventsHandler
        );

        mIsRunning = true;
    }

    @Override
    public void stop() throws VrEventsException
    {
        mIsRunning = false;
        mVrEventsSubscriber = null;
        mVrEventsPublisher = null;
    }

    @Override
    public boolean isRunning()
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

    private boolean mIsRunning = false;
    private MqttVrEventsPublisher mVrEventsPublisher = null;
    private MqttVrEventsSubscriber mVrEventsSubscriber = null;
    private Connection mConnection;
}