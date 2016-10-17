package com.actein.zxing.qr;

public enum EventType
{
    UNKNOWN,
    VIRTUAL_REALITY_HTC,
    SONY_PLAYSTATION;

    static EventType convertToEventType(String eventType)
    {
        if (eventType.equals("Virtual Reality HTC"))
        {
            return VIRTUAL_REALITY_HTC;
        }
        else if (eventType.equals("Sony Playstation"))
        {
            return SONY_PLAYSTATION;
        }
        return UNKNOWN;
    }
}
