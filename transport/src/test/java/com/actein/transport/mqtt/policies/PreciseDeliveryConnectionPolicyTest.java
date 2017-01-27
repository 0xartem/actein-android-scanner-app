package com.actein.transport.mqtt.policies;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class PreciseDeliveryConnectionPolicyTest
{
    @Before
    public void setUp() throws Exception
    {
        mPolicy = new PreciseDeliveryConnectionPolicy(1);
    }

    @After
    public void tearDown() throws Exception
    {
        mPolicy = null;
    }

    @Test
    public void getQualityOfService() throws Exception
    {
        assertEquals(mPolicy.getQualityOfService(), 2);
    }

    @Test
    public void shouldRetainMessages() throws Exception
    {
        assertTrue(mPolicy.shouldRetainMessages());
    }

    @Test
    public void isPersistentSession() throws Exception
    {
        assertFalse(mPolicy.isPersistentSession());
    }

    @Test
    public void isAutomaticReconnect() throws Exception
    {
        assertFalse(mPolicy.isAutomaticReconnect());
    }

    @Test
    public void getConnectionTimeout() throws Exception
    {
        assertEquals(mPolicy.getConnectionTimeout(), 15);
    }

    @Test
    public void getKeepAliveInterval() throws Exception
    {
        assertEquals(mPolicy.getKeepAliveInterval(), 30);
    }

    @Test
    public void shouldUseLastWill() throws Exception
    {
        assertTrue(mPolicy.shouldUseLastWill());
    }

    @Test
    public void getLastWillTopic() throws Exception
    {
        assertEquals("Last will topic is not equal",
                     mPolicy.getLastWillTopic(),
                     "factory/booths/1/embDevice/status");
    }

    private PreciseDeliveryConnectionPolicy mPolicy;
}