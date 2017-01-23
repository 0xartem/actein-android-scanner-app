package com.actein.transport.mqtt;

import com.actein.transport.mqtt.policies.PreciseDeliveryConnectionPolicy;

import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.junit.Test;

import static org.junit.Assert.*;

public class ConnectOptionsBuilderTest
{
    @Test
    public void buildConnectOptions() throws Exception
    {
        MqttConnectOptions options = ConnectOptionsBuilder.buildConnectOptions(
                new PreciseDeliveryConnectionPolicy(1));

        assertTrue(options.isCleanSession());
        assertEquals("Wrong will destination",
                     options.getWillDestination(),
                     "factory/booths/1/embDevice/status");
        assertEquals(options.getKeepAliveInterval(), 30);
        assertEquals(options.getConnectionTimeout(), 15);
        assertFalse(options.isAutomaticReconnect());

    }

}