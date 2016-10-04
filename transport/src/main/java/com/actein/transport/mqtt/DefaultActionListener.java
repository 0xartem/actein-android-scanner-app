package com.actein.transport.mqtt;

import android.util.Log;

import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;

public class DefaultActionListener implements IMqttActionListener
{
    public void onSuccess(IMqttToken asyncActionToken)
    {
        String topics[] = asyncActionToken.getTopics();
        if (topics != null)
        {
            Log.i("tag", "success " + topics[0]);
        }
        else
        {
            Log.i("tag", "success");
        }
    }

    public void onFailure(IMqttToken asyncActionToken, Throwable exception)
    {
        String topics[] = asyncActionToken.getTopics();
        if (topics != null)
        {
            Log.i("tag", "failure " + topics[0]);
        }
        else
        {
            Log.i("tag", "failure");
        }
    }
}
