package com.actein.transport.mqtt;

import android.util.Log;

import com.actein.transport.mqtt.interfaces.ConnectionObserver;
import com.actein.transport.mqtt.interfaces.MessageHandler;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttMessage;

public class MqttSubscriberCallback implements MqttCallbackExtended
{
    public MqttSubscriberCallback(MessageHandler messageHandler, ConnectionObserver connectionObserver)
    {
        mMessageHandler = messageHandler;
        mConnectionObserver = connectionObserver;
    }

    @Override
    public void connectionLost(Throwable cause)
    {
        try
        {
            if (cause != null)
            {
                Log.e(TAG, "MQTT connection lost: " + cause.toString(), cause);
                if (mConnectionObserver != null)
                {
                    mConnectionObserver.onConnectionLost();
                }
            }
            else
            {
                Log.i(TAG, "MQTT client disconnected gracefully");
            }
        }
        catch (Exception ex)
        {
            Log.e(TAG, ex.toString(), ex);
        }
    }

    @Override
    public void connectComplete(boolean reconnect, String serverURI)
    {
        try
        {
            if (reconnect)
            {
                Log.i(TAG, "MQTT client reconnected successfully: " +  serverURI);
                if (mConnectionObserver != null)
                    mConnectionObserver.onReconnected();
            }
            else
            {
                Log.i(TAG, "MQTT client connected successfully: " + serverURI);
                if (mConnectionObserver != null)
                    mConnectionObserver.onConnected();
            }
        }
        catch (Exception ex)
        {
            Log.e(TAG, ex.toString(), ex);
        }
    }

    @Override
    public void messageArrived(String topic, MqttMessage message) throws Exception
    {
        try
        {
            Log.i(TAG, "Message arrived: " + topic);
            if (mMessageHandler != null)
            {
                mMessageHandler.handleMessage(topic, message);
            }
        }
        catch (Exception ex)
        {
            Log.e(TAG, ex.toString(), ex);
        }
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken token)
    {
    }

    private MessageHandler mMessageHandler;
    private ConnectionObserver mConnectionObserver;
    private static final String TAG = MqttSubscriberCallback.class.getSimpleName();
}
