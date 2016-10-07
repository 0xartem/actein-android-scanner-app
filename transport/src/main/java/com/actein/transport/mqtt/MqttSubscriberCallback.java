package com.actein.transport.mqtt;

import android.util.Log;

import com.actein.transport.mqtt.interfaces.MessageHandler;
import com.actein.transport.mqtt.interfaces.UINotifier;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;

public class MqttSubscriberCallback implements MqttCallback
{
    public MqttSubscriberCallback(MessageHandler messageHandler, UINotifier uiNotifier)
    {
        mMessageHandler = messageHandler;
        mUiNotifier = uiNotifier;
    }

    @Override
    public void connectionLost(Throwable cause)
    {
        try
        {
            if (cause != null)
            {
                Log.e(TAG, "Connection lost :" + cause.getMessage(), cause);
                if (mUiNotifier != null)
                {
                    mUiNotifier.onConnectionLost();
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
            mMessageHandler.handleMessage(topic, message);
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
    private UINotifier mUiNotifier;
    private static final String TAG = MqttSubscriberCallback.class.getSimpleName();
}
