package com.actein.transport.mqtt;

import org.junit.Test;

import static org.junit.Assert.*;


public class MqttClientEndPointTest
{
    @Test
    public void getClientId() throws Exception
    {
        MqttClientEndPoint clientEndPoint = new MqttClientEndPoint("clientId");
        assertEquals("Client Ids are not equal", clientEndPoint.getClientId(), "clientId");
    }

}