package com.actein.event;

import com.actein.mvp.model.VrStation;

public class TurnGameOffEvent
{
    public VrStation vrStation;

    public TurnGameOffEvent(VrStation vrStation)
    {
        this.vrStation = vrStation;
    }
}
