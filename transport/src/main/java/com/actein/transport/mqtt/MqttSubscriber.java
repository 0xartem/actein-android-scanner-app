package com.actein.transport.mqtt;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttException;

public class MqttSubscriber
{
    public MqttSubscriber(MqttAndroidClient client)
    {
        mClient = client;
    }

    public void setupCallback(MqttCallback callback)
    {
        mClient.setCallback(callback);
    }

    public void subscribe(String topic) throws MqttException
    {
        IMqttToken token = mClient.subscribe(topic, 1);
        token.setActionCallback(new LogActionListener());
    }

    public void unsubscribe(String topic) throws MqttException
    {
        IMqttToken token = mClient.unsubscribe(topic);
        token.setActionCallback(new LogActionListener());
    }

    private MqttAndroidClient mClient;
}
