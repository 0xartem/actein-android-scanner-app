package com.google.zxing.client.result;

import java.util.concurrent.TimeUnit;

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

    public long getDurationSeconds()
    {
        return 0;
        /*TODO: utils: move to another projectreturn getDateDifference(calendarParsedResult.getEnd(),
                                 calendarParsedResult.getStart(),
                                 TimeUnit.SECONDS);*/
    }

    public CalendarParsedResult getInnerCalendarResult()
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

    private final CalendarParsedResult calendarParsedResult;
    private final String eventType;
    private final String game;
    private final int boothId;
}
