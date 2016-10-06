package com.actein.transport.mqtt.policies;

public interface ConnectionPolicy
{
    int getQualityOfService();

    boolean shouldRetainMessages();
    boolean isPersistentSession();

    boolean shouldUseLastWill();
    String getLastWillTopic();
    byte[] getLastWillPayload();

    int getKeepAliveInterval();
    int getConnectionTimeout();
}