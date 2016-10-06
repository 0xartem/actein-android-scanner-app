package com.actein.transport.mqtt.policies;

import org.eclipse.paho.client.mqttv3.MqttConnectOptions;

public abstract class DefaultConnectionPolicy implements ConnectionPolicy
{
    @Override
    public boolean isPersistentSession()
    {
        return !MqttConnectOptions.CLEAN_SESSION_DEFAULT;
    }

    @Override
    public int getKeepAliveInterval()
    {
        return MqttConnectOptions.KEEP_ALIVE_INTERVAL_DEFAULT;
    }

    @Override
    public int getConnectionTimeout()
    {
        return MqttConnectOptions.CONNECTION_TIMEOUT_DEFAULT;
    }
}
