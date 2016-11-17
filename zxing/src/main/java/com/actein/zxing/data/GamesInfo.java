package com.actein.zxing.data;

import com.actein.mvp.ContextOwner;
import com.google.zxing.client.android.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class GamesInfo
{
    public GamesInfo(ContextOwner contextOwner)
    {
        fillGamesMap(contextOwner);
    }

    public List<CharSequence> getGamesNames()
    {
        return new ArrayList<>(mGamesMap.keySet());
    }

    public long getSteamId(CharSequence gameName)
    {
        return mGamesMap.get(gameName);
    }

    private void fillGamesMap(ContextOwner contextOwner)
    {
        String[] gamesArray = contextOwner.getActivityContext()
                                          .getResources()
                                          .getStringArray(R.array.games_list);
        for (String value : gamesArray)
        {
            String[] nameAndSteamId = value.split("\\|", 2);
            mGamesMap.put(nameAndSteamId[0], Long.parseLong(nameAndSteamId[1]));
        }
    }

    private Map<CharSequence, Long> mGamesMap = new TreeMap<>();
}
