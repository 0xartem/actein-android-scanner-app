package com.actein.vr_events;

import com.actein.transport.mqtt.Connection;
import com.actein.vr_events.interfaces.VrEventsException;
import com.actein.vr_events.interfaces.VrEventsHandler;
import com.actein.vr_events.interfaces.VrEventsManager;
import com.actein.vr_events.interfaces.VrEventsPublisher;

public class MqttVrEventsManager implements VrEventsManager
{
    public MqttVrEventsManager(Connection connection)
    {
        mConnection = connection;
    }

    @Override
    public void start(boolean subscribe, VrEventsHandler vrEventsHandler) throws VrEventsException
    {
        mSubscribe = subscribe;
        mVrEventsPublisher = new MqttVrEventsPublisher(mConnection.getPublisher());
        mVrEventsSubscriber = new MqttVrEventsSubscriber(vrEventsHandler, mConnection.getSubscriber());
        if (mSubscribe)
        {
            mVrEventsSubscriber.subscribe();
        }
    }

    @Override
    public void stop() throws VrEventsException
    {
        if (mSubscribe)
        {
            mVrEventsSubscriber.unsubscribe();
        }
        mVrEventsSubscriber = null;
        mVrEventsPublisher = null;
        mSubscribe = false;
    }

    @Override
    public VrEventsPublisher getPublisher()
    {
        return mVrEventsPublisher;
    }

    private boolean mSubscribe = false;
    private MqttVrEventsPublisher mVrEventsPublisher = null;
    private MqttVrEventsSubscriber mVrEventsSubscriber = null;
    private Connection mConnection;
}