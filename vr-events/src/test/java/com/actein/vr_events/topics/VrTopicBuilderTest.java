package com.actein.vr_events.topics;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class VrTopicBuilderTest
{
    private VrTopicBuilder mBuilder = new VrTopicBuilder();

    @Before
    public void setUp() throws Exception
    {
        mBuilder = new VrTopicBuilder();
    }

    @Test
    public void setToGameAll() throws Exception
    {
        assertEquals("factory/booths/32/pc/vr/game/#", mBuilder.setToGameAll().setBoothId(32).build());
    }

    @Test
    public void setToGameOn() throws Exception
    {
        assertEquals("factory/booths/55/pc/vr/game/on", mBuilder.setToGameOn().setBoothId(55).build());
    }

    @Test
    public void setToGameOff() throws Exception
    {
        assertEquals("factory/booths/41/pc/vr/game/off", mBuilder.setToGameOff().setBoothId(41).build());
    }

    @Test
    public void setToGameStatus() throws Exception
    {
        assertEquals("factory/booths/42/pc/vr/game/status", mBuilder.setToGameStatus().setBoothId(42).build());
    }

    @Test
    public void setAllBooths() throws Exception
    {
        assertEquals("factory/booths/+/pc/vr/game/status", mBuilder.setToGameStatus().setAllBooths().build());
    }
}