package com.actein.zxing;

import android.content.Context;

import com.actein.transport.mqtt.Connection;
import com.actein.vr_events.MqttVrEventsManager;
import com.actein.vr_events.interfaces.VrEventsException;
import com.actein.zxing.data.Preferences;

import org.eclipse.paho.client.mqttv3.MqttException;

public class VrGameSwitcher
{
    public VrGameSwitcher(Context appContext) throws MqttException, VrEventsException
    {
        mConnection = Connection.createInstance(
                appContext,
                Preferences.getServerUri(appContext),
                1883
                );

        mConnection.connect();
        mManager = new MqttVrEventsManager(mConnection);
        mManager.start(false, null);
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
            mConnection.disconnect();
        }
        catch (Exception ex)
        {
            mCloseException = ex;
        }
    }

    private Exception mCloseException = null;
    private Connection mConnection;
    private MqttVrEventsManager mManager;
}
