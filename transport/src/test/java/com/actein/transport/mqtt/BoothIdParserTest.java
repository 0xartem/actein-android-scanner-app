package com.actein.transport.mqtt;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.junit.Assert.*;

public class BoothIdParserTest
{
    @Rule
    public final ExpectedException mThrown = ExpectedException.none();

    @Test
    public void parseBoothId_Status() throws Exception
    {
        int boothId = BoothIdParser.parseBoothId("factory/booths/42/pc/vr/game/status");
        assertEquals(boothId, 42);
    }

    @Test
    public void parseBoothId_GameOn() throws Exception
    {
        int boothId = BoothIdParser.parseBoothId("factory/booths/42/pc/vr/game/on");
        assertEquals(boothId, 42);
    }

    @Test
    public void parseBoothId_GameOff() throws Exception
    {
        int boothId = BoothIdParser.parseBoothId("factory/booths/42/pc/vr/game/off");
        assertEquals(boothId, 42);
    }

    @Test
    public void parseBoothId_GameAll() throws Exception
    {
        int boothId = BoothIdParser.parseBoothId("factory/booths/42/pc/vr/game/#");
        assertEquals(boothId, 42);
    }

    @Test
    public void parseBoothId_Throw_ArrayIndexOutOfBoundsException() throws Exception
    {
        mThrown.expect(ArrayIndexOutOfBoundsException.class);
        mThrown.expectMessage("Invalid MQTT topic");
        BoothIdParser.parseBoothId("factory/booths/");
    }

    @Test
    public void parseBoothId_Throw_NumberFormatException() throws Exception
    {
        mThrown.expect(NumberFormatException.class);
        BoothIdParser.parseBoothId("factory/booths/x/pc/vr/game/#");
    }
}