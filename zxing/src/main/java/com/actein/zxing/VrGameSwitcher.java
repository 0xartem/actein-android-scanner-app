package com.actein.zxing;

import android.content.Context;

import com.actein.transport.mqtt.Connection;
import com.actein.transport.mqtt.interfaces.UINotifier;
import com.actein.transport.mqtt.listeners.Action;
import com.actein.transport.mqtt.listeners.CommonActionListener;
import com.actein.vr_events.MqttVrEventsManager;
import com.actein.vr_events.interfaces.VrEventsException;
import com.actein.zxing.data.Preferences;

import org.eclipse.paho.client.mqttv3.MqttException;

public class VrGameSwitcher
{
    public VrGameSwitcher(Context appContext, UINotifier uiNotifier) throws MqttException, VrEventsException
    {
        mUiNotifier = uiNotifier;
        mConnection = Connection.createInstance(
                appContext,
                Preferences.getServerUri(appContext)
                );

        mConnection.connect(new CommonActionListener(Action.CONNECT, uiNotifier));
        mManager = new MqttVrEventsManager(mConnection, uiNotifier);
        mManager.start(null);
    }

    public void turnGameOn() throws VrEventsException
    {
        mManager.getPublisher().publishVrGameOnEvent();
    }

    public void turnGameOff() throws VrEventsException
    {
        mManager.getPublisher().publishVrGameOffEvent();
    }

    public Exception getCloseException()
    {
        return mCloseException;
    }

    public void close()
    {
        try
        {
            mManager.stop();
            mConnection.disconnect(new CommonActionListener(Action.DISCONNECT, mUiNotifier));
        }
        catch (Exception ex)
        {
            mCloseException = ex;
        }
    }

    private UINotifier mUiNotifier;
    private Exception mCloseException = null;
    private Connection mConnection;
    private MqttVrEventsManager mManager;
}
