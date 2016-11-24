package com.actein.transport.mqtt.policies;

public class OneTimeConnectionPolicy extends DefaultConnectionPolicy
{
    @Override
    public int getQualityOfService()
    {
        return QualityOfService.convertToMqttQosValues(QualityOfService.EXACTLY_ONCE_DELIVERY_GUARANTEE);
    }

    @Override
    public boolean shouldRetainMessages()
    {
        return false;
    }

    @Override
    public boolean isPersistentSession()
    {
        return false;
    }

    @Override
    public boolean isAutomaticReconnect()
    {
        return false;
    }

    @Override
    public boolean shouldUseLastWill()
    {
        return false;
    }

    @Override
    public String getLastWillTopic()
    {
        return null;
    }

    @Override
    public byte[] getLastWillPayload()
    {
        return null;
    }
}
