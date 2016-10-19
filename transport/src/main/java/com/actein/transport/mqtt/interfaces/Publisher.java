package com.actein.transport.mqtt.interfaces;

import com.google.protobuf.MessageLite;

import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.MqttException;

public interface Publisher
{
    void publish(String topic,
                 MessageLite protobufMessage,
                 IMqttActionListener mqttActionListener) throws MqttException;

    void publish(String topic,
                 MessageLite protobufMessage,
                 IMqttActionListener mqttActionListener,
                 boolean retained) throws MqttException;
}
