package com.actein.scanner.booth;

import org.junit.Test;

import static org.junit.Assert.*;


public class VrBoothSelectorTest
{
    @Test
    public void getBoothId() throws Exception
    {
        VrBoothSelector vrBoothSelector = new VrBoothSelector("booth");
        int boothId = vrBoothSelector.getBoothId("booth 2");
        assertEquals(boothId, 2);
    }

}