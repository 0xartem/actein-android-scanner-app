package com.actein.transport.mqtt;

import android.util.Log;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;

public class MqttSubscriberCallback implements MqttCallback
{
    public MqttSubscriberCallback(MessageHandler messageHandler)
    {
        mMessageHandler = messageHandler;
    }

    @Override
    public void connectionLost(Throwable cause)
    {
        if (cause != null)
        {
            Log.e(TAG, "Connection lost :" + cause.getMessage(), cause);
        }
    }

    @Override
    public void messageArrived(String topic, MqttMessage message) throws Exception
    {
        Log.i(TAG, "Message arrived: " + topic);
        mMessageHandler.handleMessage(topic, message);
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken token)
    {
        Log.i(TAG, "Delivery complete: ");
    }

    private MessageHandler mMessageHandler;
    private static String TAG = MqttSubscriberCallback.class.getSimpleName();
}
