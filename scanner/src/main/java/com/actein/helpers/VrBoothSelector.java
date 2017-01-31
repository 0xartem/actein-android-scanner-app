package com.actein.helpers;

import java.util.ArrayList;
import java.util.List;

public class VrBoothSelector
{
    public VrBoothSelector(CharSequence commonText)
    {
        for (Integer i = 1; i <= BOOTHS_COUNT; i++)
        {
            mBoothsList.add(commonText + " " + i.toString());
        }
    }

    public List<CharSequence> getBoothsNames()
    {
        return mBoothsList;
    }

    public int getBoothId(final String boothStr)
    {
        String idStr = boothStr.substring(boothStr.lastIndexOf(" ") + 1);
        return Integer.parseInt(idStr);
    }

    private static final int BOOTHS_COUNT = 16;
    private List<CharSequence> mBoothsList = new ArrayList<>();

}
