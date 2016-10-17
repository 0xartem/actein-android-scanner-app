package com.google.zxing.client.result;

import com.google.zxing.Result;

import java.util.List;

/**
 * Extends partially implemented the iCalendar format's "VEVENT" format for specifying an actein
 * calendar event. See RFC 2445. Adds new fields VERSION, BID, EVENT_TYPE, GAME, STEAM_GAME_ID,
 * BOOTH, PUBLIC_KEY, SIGNATURE
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
        String versionStr = matchSingleVCardPrefixedField("VERSION", rawText, true);
        String bid = matchSingleVCardPrefixedField("BID", rawText, true);
        String eventType = matchSingleVCardPrefixedField("EVENT_TYPE", rawText, true);
        String gameName = matchSingleVCardPrefixedField("GAME", rawText, true);
        String steamGameIdStr = matchSingleVCardPrefixedField("STEAM_GAME_ID", rawText, true);
        String boothIdStr = matchSingleVCardPrefixedField("BOOTH", rawText, true);
        String publicKey = matchSingleVCardPrefixedField("PUBLIC_KEY", rawText, true);
        String signature = matchSingleVCardPrefixedField("SIGNATURE", rawText, true);
        byte[] signedData = "data".getBytes();

        if (eventType == null || gameName == null || steamGameIdStr == null || boothIdStr == null ||
            publicKey == null || signature == null || signedData == null)
        {
            return null;
        }

        int version = 1;
        if (versionStr != null)
        {
            version = Integer.parseInt(versionStr);
        }
        long steamGameId = Long.parseLong(steamGameIdStr);
        int boothId = Integer.parseInt(boothIdStr);

        return new ActeinCalendarParsedResult(version,
                                              bid,
                                              eventType,
                                              gameName,
                                              steamGameId,
                                              boothId,
                                              publicKey,
                                              signature,
                                              signedData,
                                              calendarParsedResult);
    }

    private static String matchSingleVCardPrefixedField(CharSequence prefix,
                                                          String rawText,
                                                          boolean trim)
    {
        List<String> values = VCardResultParser.matchSingleVCardPrefixedField(prefix, rawText, trim, false);
        return values == null || values.isEmpty() ? null : values.get(0);
    }
}
