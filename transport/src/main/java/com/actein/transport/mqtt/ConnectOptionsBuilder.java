package com.actein.transport.mqtt;

import com.actein.transport.mqtt.policies.ConnectionPolicy;

import org.eclipse.paho.client.mqttv3.MqttConnectOptions;

public class ConnectOptionsBuilder
{
    public static MqttConnectOptions buildConnectOptions(ConnectionPolicy connectionPolicy)
    {
        MqttConnectOptions connectOptions = new MqttConnectOptions();
        connectOptions.setCleanSession(!connectionPolicy.isPersistentSession());

        if (connectionPolicy.shouldUseLastWill())
        {
            connectOptions.setWill(
                    connectionPolicy.getLastWillTopic(),
                    connectionPolicy.getLastWillPayload(),
                    connectionPolicy.getQualityOfService(),
                    connectionPolicy.shouldRetainMessages()
                    );
        }

        connectOptions.setKeepAliveInterval(connectionPolicy.getKeepAliveInterval());
        connectOptions.setConnectionTimeout(connectionPolicy.getConnectionTimeout());
        connectOptions.setAutomaticReconnect(connectionPolicy.isAutomaticReconnect());

        return connectOptions;
    }
}
