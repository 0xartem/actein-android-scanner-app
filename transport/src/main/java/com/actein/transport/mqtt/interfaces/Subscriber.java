package com.actein.transport.mqtt.interfaces;

import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttException;

public interface Subscriber
{
    void setupCallback(MqttCallback callback);
    void subscribe(String[] topics, IMqttActionListener mqttActionListener) throws MqttException;
    void subscribe(String topic, IMqttActionListener mqttActionListener) throws MqttException;
    void unsubscribe(String[] topics, IMqttActionListener mqttActionListener) throws MqttException;
    void unsubscribe(String topic, IMqttActionListener mqttActionListener) throws MqttException;
}
