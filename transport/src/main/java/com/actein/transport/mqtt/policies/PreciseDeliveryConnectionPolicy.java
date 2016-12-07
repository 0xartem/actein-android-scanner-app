package com.actein.transport.mqtt.policies;

import com.actein.transport.mqtt.OnlineStatusProtos;
import com.actein.transport.mqtt.Topics;

public class PreciseDeliveryConnectionPolicy extends DefaultConnectionPolicy
{
    public PreciseDeliveryConnectionPolicy(int boothId)
    {
        mLastWillTopic = Topics.EMB_DEVICE_ONLINE_STATUS.replace(Topics.BOOTH_ID,
                                                                 Integer.toString(boothId));
    }

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
        return mLastWillTopic;
    }

    @Override
    public byte[] getLastWillPayload()
    {
        OnlineStatusProtos.OnlineStatusEvent event = OnlineStatusProtos.OnlineStatusEvent
                .newBuilder()
                .setStatus(OnlineStatusProtos.OnlineStatus.OFFLINE)
                .build();

        return event.toByteArray();
    }

    private String mLastWillTopic;
}
