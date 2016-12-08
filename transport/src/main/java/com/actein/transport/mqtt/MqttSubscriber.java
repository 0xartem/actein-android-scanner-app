package com.actein.transport.mqtt;

import com.actein.transport.mqtt.interfaces.Subscriber;
import com.actein.transport.mqtt.policies.ConnectionPolicy;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttException;

import java.util.Arrays;

public class MqttSubscriber implements Subscriber
{
    MqttSubscriber(MqttAndroidClient client,
                   ConnectionPolicy connectionPolicy)
    {
        mClient = client;
        mConnectionPolicy = connectionPolicy;
    }

    @Override
    public void setupCallback(MqttCallbackExtended callback)
    {
        mClient.setCallback(callback);
    }

    @Override
    public void subscribe(String[] topics, IMqttActionListener mqttActionListener) throws MqttException
    {
        int[] qosArray = new int[topics.length];
        Arrays.fill(qosArray, mConnectionPolicy.getQualityOfService());
        IMqttToken token = mClient.subscribe(topics, qosArray);
        token.setActionCallback(mqttActionListener);
    }

    @Override
    public void subscribe(String topic, IMqttActionListener mqttActionListener) throws MqttException
    {
        IMqttToken token = mClient.subscribe(topic, mConnectionPolicy.getQualityOfService());
        token.setActionCallback(mqttActionListener);
    }

    @Override
    public void unsubscribe(String[] topics, IMqttActionListener mqttActionListener) throws MqttException
    {
        IMqttToken token = mClient.unsubscribe(topics);
        token.setActionCallback(mqttActionListener);
    }

    @Override
    public void unsubscribe(String topic, IMqttActionListener mqttActionListener) throws MqttException
    {
        IMqttToken token = mClient.unsubscribe(topic);
        token.setActionCallback(mqttActionListener);
    }

    private MqttAndroidClient mClient;
    private ConnectionPolicy mConnectionPolicy;
}
