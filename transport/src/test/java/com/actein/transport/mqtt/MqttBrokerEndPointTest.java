package com.actein.transport.mqtt;

import org.junit.Test;

import static org.junit.Assert.*;

public class MqttBrokerEndPointTest
{
    @Test
    public void MqttBrokerEndPointTest_MqttUri() throws Exception
    {
        MqttBrokerEndPoint brokerEndPoint = new MqttBrokerEndPoint("54.45.87.32");
        assertEquals("Wrong broker host", brokerEndPoint.getBrokerHost(), "54.45.87.32");
        assertEquals("Wrong mqtt port", brokerEndPoint.getPort(), 1883);
        assertFalse(brokerEndPoint.isTlsEndPoint());
        assertEquals("Wrong MQTT broker uri", brokerEndPoint.getEndPointUri(), "tcp://54.45.87.32:1883");
    }

    @Test
    public void MqttBrokerEndPointTest_MqttSslUri() throws Exception
    {
        MqttBrokerEndPoint brokerEndPoint = new MqttBrokerEndPoint("54.45.87.32",
                                                                   MqttBrokerEndPoint.STANDARD_MQTT_SSL_PORT,
                                                                   true);

        assertEquals("Wrong broker host", brokerEndPoint.getBrokerHost(), "54.45.87.32");
        assertEquals("Wrong mqtt port", brokerEndPoint.getPort(), 8883);
        assertTrue(brokerEndPoint.isTlsEndPoint());
        assertEquals("Wrong MQTT broker uri", brokerEndPoint.getEndPointUri(), "ssl://54.45.87.32:8883");
    }

    @Test
    public void isTlsEndPoint() throws Exception
    {

    }

    @Test
    public void getEndPointUri() throws Exception
    {

    }

}