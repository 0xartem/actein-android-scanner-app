package com.actein.transport.mqtt;

import android.util.Log;

import com.actein.transport.mqtt.interfaces.ConnectionObserver;
import com.actein.transport.mqtt.interfaces.MessageHandler;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;

public class MqttSubscriberCallback implements MqttCallback
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
                Log.e(TAG, "Connection lost :" + cause.getMessage(), cause);
                if (mConnectionObserver != null)
                {
                    mConnectionObserver.onConnectionLost();
                }
            }
            else
            {
                Log.i(TAG, "Disconnected gracefully");
            }
        }
        catch (Exception ex)
        {
            Log.e(TAG, ex.getMessage(), ex);
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
            Log.e(TAG, ex.getMessage(), ex);
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
