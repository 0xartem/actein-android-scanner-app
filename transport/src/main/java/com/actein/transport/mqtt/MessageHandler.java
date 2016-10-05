package com.actein.transport.mqtt;

import org.eclipse.paho.client.mqttv3.MqttMessage;

public interface MessageHandler
{
    void handleMessage(String topic, MqttMessage message) throws Exception;
}
