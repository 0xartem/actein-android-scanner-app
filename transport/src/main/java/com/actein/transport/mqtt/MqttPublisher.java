package com.actein.transport.mqtt;

import com.actein.transport.mqtt.interfaces.Publisher;
import com.actein.transport.mqtt.policies.ConnectionPolicy;
import com.google.protobuf.MessageLite;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttException;

public class MqttPublisher implements Publisher
{
    MqttPublisher(MqttAndroidClient client,
                  ConnectionPolicy connectionPolicy)
    {
        mClient = client;
        mConnectionPolicy = connectionPolicy;
    }

    @Override
    public void publish(String topic,
                        MessageLite protobufMessage,
                        IMqttActionListener mqttActionListener) throws MqttException
    {
        IMqttDeliveryToken token = mClient.publish(
                topic,
                protobufMessage.toByteArray(),
                mConnectionPolicy.getQualityOfService(),
                mConnectionPolicy.shouldRetainMessages()
        );
        token.setActionCallback(mqttActionListener);
    }

    @Override
    public void publish(String topic,
                        MessageLite protobufMessage,
                        IMqttActionListener mqttActionListener,
                        boolean retained) throws MqttException
    {
        IMqttDeliveryToken token = mClient.publish(
                topic,
                protobufMessage.toByteArray(),
                mConnectionPolicy.getQualityOfService(),
                retained
        );
        token.setActionCallback(mqttActionListener);
    }

    private MqttAndroidClient mClient;
    private ConnectionPolicy mConnectionPolicy;
}
