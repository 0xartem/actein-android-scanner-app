package com.google.zxing.client.result;

import com.google.zxing.Result;

import java.util.ArrayList;
import java.util.List;

/**
 * Extends partially implemented the iCalendar format's "VEVENT" format for specifying an actein
 * calendar event. See RFC 2445. Adds new fields VERSION, BID, EVENT_TYPE, GAME, STEAM_GAME_ID,
 * BOOTH, PUBLIC_KEY, SIGNATURE
 *
 * @author Artem Brazhnikov
 */

class ActeinVEventResultParser extends ResultParser
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
        String equipment = matchSingleVCardPrefixedField("EQUIPMENT", rawText, true);
        String gameName = matchSingleVCardPrefixedField("GAME", rawText, true);
        String steamGameIdStr = matchSingleVCardPrefixedField("STEAM_GAME_ID", rawText, true);
        String boothIdsStr = matchSingleVCardPrefixedField("BOOTH", rawText, true);

        int version = 1;
        if (versionStr != null)
        {
            version = Integer.parseInt(versionStr);
        }

        byte[] signedData = null;
        String signature = null;

        if (version == 1)
        {
            int signatureIdx = rawText.indexOf("SIGNATURE:");
            if (signatureIdx == -1 || signatureIdx == 0)
            {
                return null;
            }
            // move one byte back to ignore last \n
            signedData = rawText.substring(0, signatureIdx - 1).getBytes();
            signature = rawText.substring(signatureIdx + "SIGNATURE:".length(), rawText.length());
        }

        if (equipment == null || gameName == null ||
            steamGameIdStr == null || boothIdsStr == null)
        {
            return null;
        }

        long steamGameId = Long.parseLong(steamGameIdStr);
        List<Integer> boothIds = parseBooths(boothIdsStr);

        return new ActeinCalendarParsedResult(version,
                                              bid,
                                              eventType,
                                              equipment,
                                              gameName,
                                              steamGameId,
                                              boothIdsStr,
                                              boothIds,
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

    private List<Integer> parseBooths(final String boothIdsStr)
    {
        List<Integer> boothIds = new ArrayList<>();
        String[] booths = boothIdsStr.split(";");
        for (String booth : booths)
        {
            boothIds.add(Integer.parseInt(booth));
        }
        return boothIds;
    }
}
