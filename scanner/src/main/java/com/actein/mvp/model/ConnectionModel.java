package com.actein.mvp.model;

import android.util.Log;

import com.actein.data.Preferences;
import com.actein.transport.mqtt.Connection;
import com.actein.transport.mqtt.MqttSubscriberCallback;
import com.actein.transport.mqtt.LastWillManager;
import com.actein.transport.mqtt.actions.ActionStatusObserver;
import com.actein.transport.mqtt.interfaces.ConnectionObserver;
import com.actein.transport.mqtt.actions.Action;
import com.actein.transport.mqtt.actions.CommonActionListener;
import com.actein.transport.mqtt.interfaces.MessageHandler;
import com.actein.vr_events.MqttVrEventsManager;
import com.actein.vr_events.VrBoothInfoProtos;
import com.actein.vr_events.VrGameProtos;
import com.actein.vr_events.interfaces.VrEventsException;
import com.actein.vr_events.interfaces.VrEventsManager;

import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ConnectionModel
        implements
        Model,
        MessageHandler, ConnectionObserver, ActionStatusObserver
{
    public static synchronized ConnectionModel createInstance(Connection connection,
                                                              ConnectionModelObserver modelObserver,
                                                              String clientId)
    {
        mSelf = new ConnectionModel(connection, modelObserver, clientId);
        return mSelf;
    }

    public static synchronized ConnectionModel getInstance()
    {
        return mSelf;
    }

    private ConnectionModel(Connection connection,
                            ConnectionModelObserver modelObserver,
                            String clientId)
    {
        mConnection = connection;
        mModelObserver = modelObserver;

        mLastWillManager = new LastWillManager(mConnection, this, clientId);
        mVrEventsManager = new MqttVrEventsManager(mConnection, this);

        mMessageHandlers.put("last-will", mLastWillManager);
        mMessageHandlers.put("vr-events", mVrEventsManager.getMessageHandler());
    }

    public VrEventsManager getVrEventsManager()
    {
        return mVrEventsManager;
    }

    public LastWillManager getLastWillManager()
    {
        return mLastWillManager;
    }

    public synchronized boolean isConnected()
    {
        return mConnection.getClient().isConnected();
    }

    public void publishGameOffEvent(int boothId)
    {
        try
        {
            VrBoothInfoProtos.VrBoothInfo vrBoothInfo = VrBoothInfoProtos.VrBoothInfo
                    .newBuilder()
                    .setId(boothId)
                    .build();
            mVrEventsManager.getPublisher().publishVrGameOffEvent(vrBoothInfo);
        }
        catch (VrEventsException ex)
        {
            Log.e(TAG, ex.toString(), ex);
            mModelObserver.onError(ex.getMessage());
        }
    }

    public void publishGameOnEvent(int boothId,
                                   String gameName,
                                   long steamGameId,
                                   long durationSeconds,
                                   boolean runTutorial)
    {
        try
        {
            VrGameProtos.VrGame vrGame = VrGameProtos.VrGame.newBuilder()
                                                            .setGameName(gameName)
                                                            .setSteamGameId(steamGameId)
                                                            .setGameDurationSeconds(durationSeconds)
                                                            .setRunTutorial(runTutorial)
                                                            .build();

            VrBoothInfoProtos.VrBoothInfo vrBoothInfo = VrBoothInfoProtos.VrBoothInfo
                    .newBuilder()
                    .setId(boothId)
                    .build();

            mVrEventsManager.getPublisher().publishVrGameOnEvent(vrBoothInfo, vrGame);
        }
        catch (VrEventsException ex)
        {
            Log.e(TAG, ex.toString(), ex);
            mModelObserver.onError(ex.getMessage());
        }
    }

    // Model implementation
    @Override
    public synchronized void onCreate()
    {
        try
        {
            mConnection.getSubscriber().setupCallback(new MqttSubscriberCallback(this, this));
            mConnection.connect(new CommonActionListener(Action.CONNECT, this));
        }
        catch (MqttException ex)
        {
            Log.e(TAG, ex.toString(), ex);
            mModelObserver.onError(ex.getMessage());
        }
    }

    @Override
    public synchronized void onDestroy(boolean isChangingConfiguration)
    {
        try
        {
            if (!isChangingConfiguration)
            {
                if (mLastWillManager.isRunning())
                {
                    mLastWillManager.stop();
                }
                if (mVrEventsManager.isRunning())
                {
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

    // MessageHandler implementation
    @Override
    public void handleMessage(String topic, MqttMessage message) throws Exception
    {
        for (MessageHandler handler : mMessageHandlers.values())
        {
            handler.handleMessage(topic, message);
        }
    }

    // ConnectionObserver implementation
    @Override
    public void onConnectionLost()
    {
        this.startManualReconnectThread();
        mModelObserver.onConnectionLost(false);
    }

    @Override
    public synchronized void onConnected()
    {
        try
        {
            mLastWillManager.start();
            mVrEventsManager.start();
        }
        catch (MqttException | VrEventsException ex)
        {
            Log.e(TAG, ex.toString(), ex);
            mModelObserver.onError(ex.getMessage());
        }
    }

    @Override
    public void onReconnected()
    {
        onConnected();
        mModelObserver.onConnected("MQTT reconnection succeed");
    }

    // ActionStatusObserver implementation
    @Override
    public void onActionSuccess(Action action, String message)
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

    @Override
    public void onActionFailure(Action action, String message)
    {
        switch (action)
        {
        case CONNECT:
            this.startManualReconnectThread();
            mModelObserver.onConnectionLost(false);
            break;
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

    private void startManualReconnectThread()
    {
        mReconnectExecutor.execute(new Runnable()
        {
            public void run()
            {
                try
                {
                    Thread.sleep(3000);
                    mConnection.connect(new CommonActionListener(Action.CONNECT,
                                                                 ConnectionModel.this));
                }
                catch (Exception ex)
                {
                    Log.e(TAG, ex.toString(), ex);
                    mModelObserver.onError(ex.getMessage());
                }
            }
        });
    }

    private Connection mConnection;
    private ConnectionModelObserver mModelObserver;

    private Map<String, MessageHandler> mMessageHandlers = new TreeMap<>();

    private VrEventsManager mVrEventsManager;
    private LastWillManager mLastWillManager;

    private ExecutorService mReconnectExecutor = Executors.newSingleThreadExecutor();

    private static ConnectionModel mSelf = null;
    private static final String TAG = ConnectionModel.class.getSimpleName();
}
