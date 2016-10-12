package com.actein.scanner.booth;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VrBoothSelector
{
    public VrBoothSelector(CharSequence commonText)
    {
        for (Integer i = 1; i <= BOOTHS_COUNT; i++)
        {
            mBoothsMap.put(commonText + " " + i.toString(), i);
        }
    }

    public List<CharSequence> getBoothsNames()
    {
        return new ArrayList<>(mBoothsMap.keySet());
    }

    public int getBoothId(final String boothStr)
    {
        Integer boothId = mBoothsMap.get(boothStr);
        if (boothId == null)
            return 0;
        return boothId;
    }

    private static final int BOOTHS_COUNT = 16;
    private Map<CharSequence, Integer> mBoothsMap = new HashMap<>();

}
