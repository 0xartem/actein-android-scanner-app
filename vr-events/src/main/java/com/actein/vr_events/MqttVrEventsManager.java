package com.actein.vr_events;

import com.actein.transport.mqtt.Connection;
import com.actein.transport.mqtt.interfaces.UINotifier;
import com.actein.transport.mqtt.listeners.Action;
import com.actein.transport.mqtt.listeners.CommonActionListener;
import com.actein.vr_events.interfaces.VrEventsException;
import com.actein.vr_events.interfaces.VrEventsHandler;
import com.actein.vr_events.interfaces.VrEventsManager;
import com.actein.vr_events.interfaces.VrEventsPublisher;
import com.actein.vr_events.interfaces.VrEventsSubscriber;

public class MqttVrEventsManager implements VrEventsManager
{
    public MqttVrEventsManager(Connection connection, UINotifier uiNotifier)
    {
        mConnection = connection;
        mUiNotifier = uiNotifier;
    }

    @Override
    public void start(VrEventsHandler vrEventsHandler) throws VrEventsException
    {
        mVrEventsPublisher = new MqttVrEventsPublisher(mConnection.createPublisher(
                new CommonActionListener(Action.PUBLISH, mUiNotifier)
        ));

        mVrEventsSubscriber = new MqttVrEventsSubscriber(
                mConnection.createSubscriber(
                new CommonActionListener(Action.SUBSCRIBE, mUiNotifier),
                new CommonActionListener(Action.UNSUBSCRIBE, mUiNotifier)),
                mUiNotifier,
                vrEventsHandler
        );
    }

    @Override
    public void stop() throws VrEventsException
    {
        mVrEventsSubscriber = null;
        mVrEventsPublisher = null;
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

    private MqttVrEventsPublisher mVrEventsPublisher = null;
    private MqttVrEventsSubscriber mVrEventsSubscriber = null;
    private Connection mConnection;
    private UINotifier mUiNotifier;
}