package com.actein.transport.mqtt;

import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;

import java.util.ArrayList;
import java.util.List;

public class ActionListenerHub implements IMqttActionListener
{
    public ActionListenerHub(IMqttActionListener defaultListener)
    {
        mListeners.add(defaultListener);
    }

    public void registerListener(IMqttActionListener actionListener)
    {
        mListeners.add(actionListener);
    }

    public void onSuccess(IMqttToken asyncActionToken)
    {
        for (IMqttActionListener listener : mListeners)
        {
            listener.onSuccess(asyncActionToken);
        }
    }

    public void onFailure(IMqttToken asyncActionToken, Throwable exception)
    {
        for (IMqttActionListener listener : mListeners)
        {
            listener.onFailure(asyncActionToken, exception);
        }
    }

    private List<IMqttActionListener> mListeners = new ArrayList<>();
}
