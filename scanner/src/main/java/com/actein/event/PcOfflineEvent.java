package com.actein.event;

public class PcOfflineEvent
{
    public final int boothId;
    public final boolean showErrorMessage;

    public PcOfflineEvent(int boothId, boolean showErrorMessage)
    {
        this.boothId = boothId;
        this.showErrorMessage = showErrorMessage;
    }
}
