package com.actein.transport.mqtt.policies;

import org.junit.Test;

import static org.junit.Assert.*;

public class QualityOfServiceTest
{
    @Test
    public void convertToMqttQosValues_AT_MOST_ONCE_DELIVERY_GUARANTEE() throws Exception
    {
        assertEquals(QualityOfService.convertToMqttQosValues(QualityOfService.AT_MOST_ONCE_DELIVERY_GUARANTEE), 0);
    }

    @Test
    public void convertToMqttQosValues_AT_LEAST_ONCE_DELIVERY_GUARANTEE() throws Exception
    {
        assertEquals(QualityOfService.convertToMqttQosValues(QualityOfService.AT_LEAST_ONCE_DELIVERY_GUARANTEE), 1);
    }

    @Test
    public void convertToMqttQosValues_EXACTLY_ONCE_DELIVERY_GUARANTEE() throws Exception
    {
        assertEquals(QualityOfService.convertToMqttQosValues(QualityOfService.EXACTLY_ONCE_DELIVERY_GUARANTEE), 2);
    }
}