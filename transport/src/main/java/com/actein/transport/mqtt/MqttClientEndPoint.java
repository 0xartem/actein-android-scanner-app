package com.actein.transport.mqtt;

import java.util.UUID;

public class MqttClientEndPoint
{
    public MqttClientEndPoint()
    {
        this(UUID.randomUUID().toString());
    }

    public MqttClientEndPoint(String clientId)
    {
        mClientId = clientId;
    }

    public String getClientId()
    {
        return mClientId;
    }

    private String mClientId;
}
