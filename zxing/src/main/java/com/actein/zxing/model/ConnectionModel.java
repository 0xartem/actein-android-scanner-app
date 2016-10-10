package com.actein.zxing.model;

import android.util.Log;

import com.actein.mvp.ContextOwner;
import com.actein.mvp.Model;
import com.actein.transport.mqtt.Connection;
import com.actein.transport.mqtt.interfaces.ActionStatusObserver;
import com.actein.transport.mqtt.interfaces.ConnectionObserver;
import com.actein.transport.mqtt.actions.Action;
import com.actein.transport.mqtt.actions.CommonActionListener;
import com.actein.vr_events.MqttVrEventsManager;
import com.actein.vr_events.interfaces.VrEventsException;
import com.actein.vr_events.interfaces.VrEventsManager;
import com.actein.zxing.data.Preferences;
import com.actein.zxing.qr.TestVrEventsHandler;

import org.eclipse.paho.client.mqttv3.MqttException;

public class ConnectionModel implements Model, ConnectionObserver, ActionStatusObserver
{
    public ConnectionModel(ContextOwner contextOwner, ConnectionModelObserver modelObserver)
    {
        mContextOwner = contextOwner;
        mModelObserver = modelObserver;
    }

    public VrEventsManager getVrEventsManager()
    {
        return mVrEventsManager;
    }

    @Override
    public void onCreate()
    {
        try
        {
            mConnection = Connection.createInstance(
                    mContextOwner.getApplicationContext(),
                    Preferences.getServerUri(mContextOwner.getApplicationContext())
                    );
            mConnection.connect(new CommonActionListener(Action.CONNECT, this));

            mVrEventsManager = new MqttVrEventsManager(mConnection);
            mVrEventsManager.start(
                    new TestVrEventsHandler(mContextOwner.getApplicationContext()),
                    this
                    );
        }
        catch (MqttException | VrEventsException ex)
        {
            Log.e(TAG, ex.getMessage(), ex);
            mModelObserver.onError(ex.getMessage());
        }
    }

    @Override
    public void onDestroy(boolean isChangingConfiguration)
    {
        try
        {
            if (!isChangingConfiguration)
            {
                if (mVrEventsManager.isRunning())
                {
                    mVrEventsManager.getSubscriber().unsubscribe(this);
                    mVrEventsManager.stop();
                }

                if (mConnection.getClient().isConnected())
                {
                    mConnection.disconnect(new CommonActionListener(Action.DISCONNECT, this));
                }
            }
        }
        catch (MqttException | VrEventsException ex)
        {
            Log.e(TAG, ex.getMessage(), ex);
        }
    }

    // ConnectionObserver implementation
    @Override
    public void onConnectionLost()
    {
        mModelObserver.onConnectionLost();
    }

    // ActionStatusObserver implementation
    @Override
    public void onActionSuccess(Action action, String message)
    {
        switch (action)
        {
        case CONNECT:
            mModelObserver.onConnected(message);
            try
            {
                mVrEventsManager.getSubscriber().subscribe(this);
            }
            catch (VrEventsException ex)
            {
                Log.e(TAG, ex.getMessage(), ex);
                mModelObserver.onError(ex.getMessage());
            }
            break;
        case DISCONNECT:
            mModelObserver.onDisconnected(message);
            //mConnection.close();
            //mConnection = null;
            break;
        case SUBSCRIBE:
            mModelObserver.onSubscribed(message);
            break;
        case UNSUBSCRIBE:
            mModelObserver.onUnsubscribed(message);
            break;
        case PUBLISH:
            mModelObserver.onPublished(message);
            break;
        default:
            throw new UnsupportedOperationException("Unknown action type");
        }
    }

    @Override
    public void onActionFailure(Action action, String message)
    {
        switch (action)
        {
        case CONNECT:
            mModelObserver.onConnected(message);
            break;
        case DISCONNECT:
            mModelObserver.onDisconnected(message);
            break;
        case SUBSCRIBE:
            mModelObserver.onSubscribed(message);
            break;
        case UNSUBSCRIBE:
            mModelObserver.onUnsubscribed(message);
            break;
        case PUBLISH:
            mModelObserver.onPublished(message);
            break;
        default:
            throw new UnsupportedOperationException("Unknown action type");
        }
    }

    private ContextOwner mContextOwner;
    private ConnectionModelObserver mModelObserver;

    private Connection mConnection = null;
    private VrEventsManager mVrEventsManager = null;

    private static final String TAG = ConnectionModel.class.getSimpleName();
}
