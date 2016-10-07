package com.actein.transport.mqtt.interfaces;

import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttException;

public interface Subscriber
{
    void setupCallback(MqttCallback callback);
    void subscribe(String[] topics) throws MqttException;
    void subscribe(String topic) throws MqttException;
    void unsubscribe(String[] topics) throws MqttException;
    void unsubscribe(String topic) throws MqttException;
}
