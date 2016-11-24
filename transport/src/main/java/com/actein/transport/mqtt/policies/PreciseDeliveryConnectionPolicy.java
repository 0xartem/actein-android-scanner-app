package com.actein.transport.mqtt.policies;

import com.actein.transport.mqtt.Topics;

public class PreciseDeliveryConnectionPolicy extends DefaultConnectionPolicy
{
    @Override
    public int getQualityOfService()
    {
        return QualityOfService.convertToMqttQosValues(QualityOfService.EXACTLY_ONCE_DELIVERY_GUARANTEE);
    }

    @Override
    public boolean shouldRetainMessages()
    {
        return true;
    }

    @Override
    public boolean isPersistentSession()
    {
        return false;
    }

    @Override
    public boolean isAutomaticReconnect()
    {
        return true;
    }

    @Override
    public boolean shouldUseLastWill()
    {
        return true;
    }

    @Override
    public String getLastWillTopic()
    {
        return Topics.EMB_DEVICE_ONLINE_STATUS;
    }

    @Override
    public byte[] getLastWillPayload()
    {
        return "offline".getBytes();//TODO
    }
}
