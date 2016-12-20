package com.actein.zxing.model;

import android.util.Log;

import com.actein.transport.mqtt.OnlineStatusProtos;
import com.actein.mvp.ContextOwner;
import com.actein.mvp.Model;
import com.actein.transport.mqtt.Connection;
import com.actein.transport.mqtt.MqttSubscriberCallback;
import com.actein.transport.mqtt.LastWillManager;
import com.actein.transport.mqtt.actions.ActionStatusObserver;
import com.actein.transport.mqtt.interfaces.ConnectionObserver;
import com.actein.transport.mqtt.actions.Action;
import com.actein.transport.mqtt.actions.CommonActionListener;
import com.actein.transport.mqtt.interfaces.MessageHandler;
import com.actein.transport.mqtt.interfaces.PcOnlineStatusHandler;
import com.actein.transport.mqtt.policies.PreciseDeliveryConnectionPolicy;
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
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ConnectionModel
        implements
        Model,
        MessageHandler, ConnectionObserver, ActionStatusObserver,
        VrEventsHandler, PcOnlineStatusHandler
{
    public ConnectionModel(ContextOwner contextOwner, ConnectionModelObserver modelObserver)
    {
        mModelObserver = modelObserver;
        mBoothSettings = new BoothSettings(contextOwner.getActivityContext());

        mConnection = Connection.createInstance(
                contextOwner.getApplicationContext(),
                Preferences.getBrokerUri(contextOwner.getApplicationContext()),
                new PreciseDeliveryConnectionPolicy(mBoothSettings.getBoothId())
                );

        mLastWillManager = new LastWillManager(mConnection, this, this, mBoothSettings.getBoothId());

        VrBoothInfoProtos.VrBoothInfo vrBoothInfo = VrBoothInfoProtos.VrBoothInfo
                .newBuilder()
                .setId(mBoothSettings.getBoothId())
                .build();
        mVrEventsManager = new MqttVrEventsManager(mConnection, this, this, vrBoothInfo);

        mMessageHandlers.put("last-will", mLastWillManager);
        mMessageHandlers.put("vr-events", mVrEventsManager.getMessageHandler());
    }

    public BoothSettings getBoothSettings()
    {
        return mBoothSettings;
    }

    public synchronized boolean isConnected()
    {
        return mConnection.getClient().isConnected();
    }

    public synchronized boolean isPcOnline()
    {
        return mPcOnlineStatus == OnlineStatusProtos.OnlineStatus.ONLINE;
    }

    public void publishGameOffEvent()
    {
        try
        {
            mVrEventsManager.getPublisher().publishVrGameOffEvent();
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
            VrGameProtos.VrGame vrGame = VrGameProtos.VrGame.newBuilder()
                                                            .setGameName(gameName)
                                                            .setSteamGameId(steamGameId)
                                                            .setGameDurationSeconds(durationSeconds)
                                                            .setRunTutorial(runTutorial)
                                                            .build();

            mVrEventsManager.getPublisher().publishVrGameOnEvent(vrGame);
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
                mLastWillManager.stop();
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

    // PcOnlineStatusHandler implementation
    @Override
    public synchronized void onPcOnlineStatusChanged(OnlineStatusProtos.OnlineStatus status)
    {
        mPcOnlineStatus = status;
        Log.i(TAG, "PC online status changed: " + status.toString());
        if (status == OnlineStatusProtos.OnlineStatus.OFFLINE)
        {
            mModelObserver.onPcOffline(false);
        }
        else
        {
            mModelObserver.onPcOnline();
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
    private OnlineStatusProtos.OnlineStatus mPcOnlineStatus = OnlineStatusProtos.OnlineStatus.UNKNOWN;

    private Map<String, MessageHandler> mMessageHandlers = new TreeMap<>();
    private BoothSettings mBoothSettings;

    private VrEventsManager mVrEventsManager;
    private LastWillManager mLastWillManager;

    private ExecutorService mReconnectExecutor = Executors.newSingleThreadExecutor();

    private static final String TAG = ConnectionModel.class.getSimpleName();
}
