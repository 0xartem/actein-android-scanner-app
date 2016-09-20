package com.actein.zxing;

import com.google.zxing.client.result.CalendarParsedResult;

import java.util.Date;

public class QrCodeValidator
{
    public QrCodeValidator(CalendarParsedResult calendarParsedResult)
    {
        mCalendarParsedResult = calendarParsedResult;
    }

    public QrCodeStatus getQrCodeStatus()
    {
        Date now = new Date();
        if (mCalendarParsedResult.getStart().after(now))
        {
            return QrCodeStatus.QrCodeNotStartedYet;
        }

        if (mCalendarParsedResult.getEnd().before(now))
        {
            return QrCodeStatus.QrCodeExpired;
        }

        if (mCalendarParsedResult.getLocation().equals(""))
        {
            return QrCodeStatus.WrongLocation;
        }

        return QrCodeStatus.Success;
    }

    private CalendarParsedResult mCalendarParsedResult;
}
