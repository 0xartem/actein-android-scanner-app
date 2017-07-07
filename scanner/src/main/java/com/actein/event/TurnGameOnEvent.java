package com.actein.event;

import com.actein.mvp.model.VrStation;

public class TurnGameOnEvent
{
    public final VrStation vrStation;
    public final String gameName;
    public final long steamGameId;
    public final long durationSeconds;
    public final boolean runTutorial;

    public TurnGameOnEvent(VrStation vrStation,
                           String gameName,
                           long steamGameId,
                           long durationSeconds,
                           boolean runTutorial)
    {
        this.vrStation = vrStation;
        this.gameName = gameName;
        this.steamGameId = steamGameId;
        this.durationSeconds = durationSeconds;
        this.runTutorial = runTutorial;
    }
}
