package com.actein.event;

public class ConnectionLostEvent
{
    public final boolean showErrorMessage;

    public ConnectionLostEvent(boolean showErrorMessage)
    {
        this.showErrorMessage = showErrorMessage;
    }
}
