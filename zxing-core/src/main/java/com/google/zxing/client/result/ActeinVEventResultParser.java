package com.google.zxing.client.result;

import com.google.zxing.Result;

import java.util.List;

/**
 * Extends partially implemented the iCalendar format's "VEVENT" format for specifying an actein
 * calendar event. See RFC 2445. Adds new fields
 *
 * @author Artem Brazhnikov
 */

public class ActeinVEventResultParser extends ResultParser
{
    @Override
    public ActeinCalendarParsedResult parse(Result result)
    {
        VEventResultParser vEventResultParser = new VEventResultParser();
        CalendarParsedResult calendarParsedResult = vEventResultParser.parse(result);
        if (calendarParsedResult == null)
        {
            return null;
        }

        String rawText = getMassagedText(result);
        String eventType = matchSingleVCardPrefixedField("EVENT_TYPE", rawText, true);
        String game = matchSingleVCardPrefixedField("GAME", rawText, true);
        String boothIdStr = matchSingleVCardPrefixedField("BOOTH", rawText, true);
        if (eventType == null || game == null || boothIdStr == null)
        {
            return null;
        }
        int boothId = Integer.parseInt(boothIdStr);

        return new ActeinCalendarParsedResult(calendarParsedResult, eventType, game, boothId);
    }

    private static String matchSingleVCardPrefixedField(CharSequence prefix,
                                                          String rawText,
                                                          boolean trim)
    {
        List<String> values = VCardResultParser.matchSingleVCardPrefixedField(prefix, rawText, trim, false);
        return values == null || values.isEmpty() ? null : values.get(0);
    }
}
