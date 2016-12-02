package com.actein.zxing.model;

import android.util.Log;

import com.actein.mvp.ContextOwner;
import com.actein.mvp.Model;
import com.actein.transport.mqtt.Connection;
import com.actein.transport.mqtt.actions.ActionStatusObserver;
import com.actein.transport.mqtt.interfaces.ConnectionObserver;
import com.actein.transport.mqtt.actions.Action;
import com.actein.transport.mqtt.actions.CommonActionListener;
import com.actein.vr_events.MqttVrEventsManager;
import com.actein.vr_events.VrBoothInfoProtos;
import com.actein.vr_events.VrGameOffProtos;
import com.actein.vr_events.VrGameOnProtos;
import com.actein.vr_events.VrGameProtos;
import com.actein.vr_events.VrGameStatusProtos;
import com.actein.vr_events.interfaces.VrEventsException;
import com.actein.vr_events.interfaces.VrEventsHandler;
import com.actein.vr_events.interfaces.VrEventsManager;
import com.actein.zxing.data.Preferences;

import org.eclipse.paho.client.mqttv3.MqttException;

public class ConnectionModel
        implements
        Model,
        ConnectionObserver,
        ActionStatusObserver,
        VrEventsHandler
{
    public ConnectionModel(ContextOwner contextOwner, ConnectionModelObserver modelObserver)
    {
        mModelObserver = modelObserver;
        mBoothSettings = new BoothSettings(contextOwner.getActivityContext());

        mConnection = Connection.createInstance(
                contextOwner.getApplicationContext(),
                Preferences.getBrokerUri(contextOwner.getApplicationContext())
                );

        VrBoothInfoProtos.VrBoothInfo vrBoothInfo = VrBoothInfoProtos.VrBoothInfo
                .newBuilder()
                .setId(mBoothSettings.getBoothId())
                .build();

        mVrEventsManager = new MqttVrEventsManager(mConnection, vrBoothInfo);
    }

    public BoothSettings getBoothSettings()
    {
        return mBoothSettings;
    }

    public boolean isConnected()
    {
        return mConnection.getClient().isConnected();
    }

    public void publishGameOffEvent()
    {
        try
        {
            if (mVrEventsManager.getPublisher() != null)
            {
                mVrEventsManager.getPublisher().publishVrGameOffEvent();
            }
        }
        catch (VrEventsException ex)
        {
            Log.e(TAG, ex.toString(), ex);
            mModelObserver.onError(ex.getMessage());
        }
    }

    public void publishGameOnEvent(String gameName,
                                   long steamGameId,
                                   long durationSeconds,
                                   boolean runTutorial)
    {
        try
        {
            if (mVrEventsManager.getPublisher() != null)
            {
                VrGameProtos.VrGame vrGame = VrGameProtos.VrGame.newBuilder()
                                                                .setGameName(gameName)
                                                                .setSteamGameId(steamGameId)
                                                                .setGameDurationSeconds(durationSeconds)
                                                                .setRunTutorial(runTutorial)
                                                                .build();

                mVrEventsManager.getPublisher().publishVrGameOnEvent(vrGame);
            }
        }
        catch (VrEventsException ex)
        {
            Log.e(TAG, ex.toString(), ex);
            mModelObserver.onError(ex.getMessage());
        }
    }

    @Override
    public void onCreate()
    {
        try
        {
            mConnection.connect(new CommonActionListener(Action.CONNECT, this));
        }
        catch (MqttException ex)
        {
            Log.e(TAG, ex.toString(), ex);
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
                    mVrEventsManager.getSubscriber().unsubscribeFromStatusEvent();
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
            Log.e(TAG, ex.toString(), ex);
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
                mVrEventsManager.start(this, this, this);
                mVrEventsManager.getSubscriber().subscribeToStatusEvent();
            }
            catch (VrEventsException ex)
            {
                Log.e(TAG, ex.toString(), ex);
                mModelObserver.onError(ex.getMessage());
            }
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

    // VrEventsHandler implementation
    @Override
    public void handleVrGameOnEvent(VrGameOnProtos.VrGameOnEvent event)
    {
    }

    @Override
    public void handleVrGameOffEvent(VrGameOffProtos.VrGameOffEvent event)
    {
    }

    @Override
    public void handleVrGameStatusEvent(VrGameStatusProtos.VrGameStatusEvent event)
    {
        StringBuilder messageBuilder = new StringBuilder();
        messageBuilder.append("The status event received: ").append(event.getStatus().toString());
        if (!event.hasError())
        {
            Log.i(TAG, messageBuilder.toString());
            mModelObserver.onVrEventStatusReceived(event.getStatus(), messageBuilder.toString());
        }
        else
        {
            messageBuilder.append("; Error code: ")
                          .append(event.getError().getErrorCode().toString())
                          .append("; Error message: ")
                          .append(event.getError().getErrorMessage());

            Log.e(TAG, messageBuilder.toString());
            mModelObserver.onVrEventStatusReceived(event.getStatus(), messageBuilder.toString());
            mModelObserver.onError(event.getError().getErrorMessage());
        }
    }

    @Override
    public void onActionFailure(Action action, String message)
    {
        switch (action)
        {
        case CONNECT:
        case DISCONNECT:
        case SUBSCRIBE:
        case UNSUBSCRIBE:
        case PUBLISH:
            mModelObserver.onError(message);
            break;
        default:
            throw new UnsupportedOperationException("Unknown action type");
        }
    }

    private ConnectionModelObserver mModelObserver;

    private Connection mConnection;
    private BoothSettings mBoothSettings;

    private VrEventsManager mVrEventsManager;

    private static final String TAG = ConnectionModel.class.getSimpleName();
}
