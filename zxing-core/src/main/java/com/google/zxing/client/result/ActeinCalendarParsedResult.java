package com.google.zxing.client.result;

/**
 * Represents a parsed result that encodes an actein calendar event at a certain time, optionally
 * with attendees and a location. Actein options: event type, game, booth.
 *
 * @author Artem Brazhnikov
 */

public class ActeinCalendarParsedResult extends ParsedResult
{
    public ActeinCalendarParsedResult(CalendarParsedResult calendarParsedResult,
                                      String eventType,
                                      String game,
                                      int boothId)
    {
        super(ParsedResultType.ACTEIN_CALENDAR);

        this.calendarParsedResult = calendarParsedResult;
        this.eventType = eventType;
        this.game = game;
        this.boothId = boothId;
    }

    public String getEventType()
    {
        return eventType;
    }

    public String getGame()
    {
        return game;
    }

    public int getBoothId()
    {
        return boothId;
    }

    public CalendarParsedResult getCalendarParsedResult()
    {
        return calendarParsedResult;
    }

    @Override
    public String getDisplayResult()
    {
        StringBuilder result = new StringBuilder();
        result.append(calendarParsedResult.getDisplayResult());
        maybeAppend(eventType, result);
        maybeAppend(game, result);
        maybeAppend(Integer.toString(boothId), result);
        return result.toString();
    }

    private CalendarParsedResult calendarParsedResult;
    private String eventType;
    private String game;
    private int boothId;
}
