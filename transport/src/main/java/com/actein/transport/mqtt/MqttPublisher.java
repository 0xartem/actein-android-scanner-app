package com.actein.transport.mqtt;

import com.google.protobuf.MessageLite;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

public class MqttPublisher
{
    public MqttPublisher(MqttAndroidClient client)
    {
        mClient = client;
    }

    public void publish(String topic, MessageLite protobufMessage) throws MqttException
    {
        MqttMessage mqttMessage = new MqttMessage(protobufMessage.toByteArray());
        IMqttDeliveryToken token = mClient.publish(topic, mqttMessage);
        token.setActionCallback(new DefaultActionListener());
    }

    private MqttAndroidClient mClient;
}