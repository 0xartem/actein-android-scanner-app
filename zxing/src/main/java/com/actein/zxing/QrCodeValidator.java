package com.actein.zxing;

import com.actein.zxing.utils.DateTimeUtils;
import com.google.zxing.client.result.CalendarParsedResult;

import java.util.Date;

public class QrCodeValidator
{
    public QrCodeValidator(CalendarParsedResult calendarParsedResult, QrCodeSettings settings)
    {
        mCalendarParsedResult = calendarParsedResult;
        mSettings = settings;
    }

    public QrCodeStatus getQrCodeStatus()
    {
        if (mCalendarParsedResult.getStart() == null
                || mCalendarParsedResult.getEnd() == null
                || mCalendarParsedResult.getLocation() == null)
        {
            return QrCodeStatus.QR_CODE_INVALID;
        }

        Date now = DateTimeUtils.getCurrentInternetDateTime();
        Date nowMinus5Min = DateTimeUtils.dateTimeMinus5Minutes(now);

        if (!mSettings.isAllowStartBeforeAppointment() && mCalendarParsedResult.getStart().after(nowMinus5Min))
        {
            return QrCodeStatus.QR_CODE_NOT_STARTED_YET;
        }

        if (!mSettings.isAllowStartAfterExpiration() && mCalendarParsedResult.getEnd().before(now))
        {
            return QrCodeStatus.QR_CODE_EXPIRED;
        }

        if (!mCalendarParsedResult.getLocation().equals("Larvik VR Activity Center"))
        {
            return QrCodeStatus.WRONG_LOCATION;
        }

        return QrCodeStatus.SUCCESS;
    }

    private QrCodeSettings mSettings;
    private CalendarParsedResult mCalendarParsedResult;
}
