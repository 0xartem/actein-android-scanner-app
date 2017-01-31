package com.actein.helpers;

import org.junit.Test;

import static org.junit.Assert.*;

public class VrBoothSelectorTest
{
    @Test
    public void getBoothId() throws Exception
    {
        VrBoothSelector vrBoothSelector = new VrBoothSelector("Booth");
        int boothId = vrBoothSelector.getBoothId("Booth 1");
        assertEquals(boothId, 1);
    }

}