package com.google.zxing.client.result;

import com.actein.utils.DateTimeUtils;

import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Represents a parsed result that encodes an actein calendar event at a certain time, optionally
 * with attendees and a location. Actein options: version, event type, equipment game, booth,
 * signature fields.
 *
 * @author Artem Brazhnikov
 */

public class ActeinCalendarParsedResult extends ParsedResult
{
    public ActeinCalendarParsedResult(int version,
                                      String bid,
                                      String eventType,
                                      String equipment,
                                      String gameName,
                                      long steamGameId,
                                      String boothIdsStr,
                                      List<Integer> boothIds,
                                      String signature,
                                      byte[] signedData,
                                      CalendarParsedResult calendarParsedResult)
    {
        super(ParsedResultType.ACTEIN_CALENDAR);

        this.version = version;
        this.bid = bid;
        this.eventType = eventType;
        this.equipment = equipment;
        this.gameName = gameName;
        this.steamGameId = steamGameId;
        this.boothIdsStr = boothIdsStr;
        this.boothIds = boothIds;
        this.signature = signature;
        this.signedData = signedData;
        this.calendarParsedResult = calendarParsedResult;
    }

    public int getVersion()
    {
        return version;
    }

    public String getBid()
    {
        return bid;
    }

    public String getEventType()
    {
        return eventType;
    }

    public String getEquipment()
    {
        return equipment;
    }

    public String getGameName()
    {
        return gameName;
    }

    public long getSteamGameId()
    {
        return steamGameId;
    }

    public String getBoothIdsStr()
    {
        return boothIdsStr;
    }

    public List<Integer> getBoothIds()
    {
        return boothIds;
    }

    public String getSignature()
    {
        return signature;
    }

    public byte[] getSignedData()
    {
        return signedData;
    }

    public long getDurationSeconds()
    {
        return DateTimeUtils.getDateDifference(calendarParsedResult.getStart(),
                                               calendarParsedResult.getEnd(),
                                               TimeUnit.SECONDS);
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
        maybeAppend(equipment, result);
        maybeAppend(gameName, result);
        maybeAppend(boothIdsStr, result);
        return result.toString();
    }

    private final int version;
    private final String bid;
    private final String eventType;
    private final String equipment;
    private final String gameName;
    private final long steamGameId;
    private final String boothIdsStr;
    private final List<Integer> boothIds;

    private final String signature;
    private final byte[] signedData;

    private final CalendarParsedResult calendarParsedResult;
}
