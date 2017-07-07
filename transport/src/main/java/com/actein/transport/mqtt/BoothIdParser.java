package com.actein.transport.mqtt;


public class BoothIdParser
{
    public static int parseBoothId(final String topic)
    {
        String[] parts = topic.split("/");
        if (parts.length < 3)
            throw new ArrayIndexOutOfBoundsException("Invalid MQTT topic");
        return Integer.parseInt(parts[2]);
    }
}
