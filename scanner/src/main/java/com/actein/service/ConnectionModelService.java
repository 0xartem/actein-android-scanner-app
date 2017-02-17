package com.actein.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.actein.data.Preferences;
import com.actein.event.ConnectionLostEvent;
import com.actein.event.ErrorEvent;
import com.actein.event.InfoEvent;
import com.actein.event.TurnGameOffEvent;
import com.actein.event.TurnGameOnEvent;
import com.actein.mvp.model.ConnectionModel;
import com.actein.mvp.model.ConnectionModelObserver;
import com.actein.mvp.model.VrStationsModel;
import com.actein.transport.mqtt.Connection;
import com.actein.transport.mqtt.MqttBrokerEndPoint;
import com.actein.transport.mqtt.MqttClientEndPoint;
import com.actein.transport.mqtt.policies.PreciseDeliveryConnectionPolicy;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

public class ConnectionModelService extends Service implements ConnectionModelObserver
{
    @Override
    public void onCreate()
    {
        super.onCreate();

        String clientId = Preferences.getClientId(getApplicationContext());
        String brokerAddr = Preferences.getBrokerAddr(getApplicationContext());

        Connection connection = Connection.createInstance(
                getApplicationContext(),
                new MqttBrokerEndPoint(brokerAddr),
                new MqttClientEndPoint(clientId),
                new PreciseDeliveryConnectionPolicy(clientId)
                                                         );

        mConnectionModel = ConnectionModel.createInstance(connection, this, clientId);
        mConnectionModel.getVrEventsManager()
                        .getSubscriber()
                        .registerVrEventsHandler(VrStationsModel.getInstance());
        mConnectionModel.getLastWillManager()
                        .registerPcOnlineStatusHandler(VrStationsModel.getInstance());

        mConnectionModel.onCreate();
        mEventReceiver = new EventReceiver();
        EventBus.getDefault().register(mEventReceiver);
    }

    @Override
    public void onDestroy()
    {
        EventBus.getDefault().unregister(mEventReceiver);
        mConnectionModel.onDestroy(false);
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent)
    {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        return START_STICKY;
    }

    public synchronized void turnGameOn(TurnGameOnEvent event)
    {
        if (!mConnectionModel.isConnected())
        {
            onConnectionLost(true);
        }
        else
        {
            mConnectionModel.publishGameOnEvent(event.vrStation.getBoothId(),
                                                event.gameName,
                                                event.steamGameId,
                                                event.durationSeconds,
                                                event.runTutorial);
        }
    }

    public synchronized void turnGameOff(TurnGameOffEvent event)
    {
        if (!mConnectionModel.isConnected())
        {
            onConnectionLost(true);
        }
        else
        {
            mConnectionModel.publishGameOffEvent(event.vrStation.getBoothId());
        }
    }

    // ConnectionModelObserver implementation
    @Override
    public void onConnected(String message)
    {
    }

    @Override
    public void onDisconnected(String message)
    {
    }

    @Override
    public void onSubscribed(String message)
    {
    }

    @Override
    public void onUnsubscribed(String message)
    {
    }

    @Override
    public void onPublished(String message)
    {
    }

    @Override
    public void onConnectionLost(boolean showErrorMsg)
    {
        EventBus.getDefault().post(new ConnectionLostEvent(showErrorMsg));
    }

    @Override
    public void onError(String message)
    {
        EventBus.getDefault().post(new ErrorEvent(message));
    }

    public void onInfo(String message)
    {
        EventBus.getDefault().post(new InfoEvent(message));
    }

    private EventReceiver mEventReceiver;
    private ConnectionModel mConnectionModel;

    private class EventReceiver
    {
        @Subscribe
        public void onTurnGameOnEventReceived(TurnGameOnEvent event)
        {
            turnGameOn(event);
        }

        @Subscribe
        public void onTurnGameOffEventReceived(TurnGameOffEvent event)
        {
            turnGameOff(event);
        }
    }
}
