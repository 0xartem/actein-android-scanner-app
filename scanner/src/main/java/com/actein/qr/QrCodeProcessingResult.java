package com.actein.qr;

import com.google.zxing.client.result.ActeinCalendarParsedResult;

public class QrCodeProcessingResult
{
    public QrCodeProcessingResult(QrCodeStatus status,
                                  ActeinCalendarParsedResult parsedResult,
                                  int busyBoothId)
    {
        mStatus = status;
        mBusyBoothId = busyBoothId;
        mParsedResult = parsedResult;
    }

    public QrCodeProcessingResult(QrCodeStatus status)
    {
        mStatus = status;
        mParsedResult = null;
    }

    public QrCodeStatus getStatus()
    {
        return mStatus;
    }

    public boolean hasBusyBooths()
    {
        return mBusyBoothId != -1;
    }

    public Integer getBusyBoothId()
    {
        return mBusyBoothId;
    }

    public ActeinCalendarParsedResult getParsedResult()
    {
        return mParsedResult;
    }

    private QrCodeStatus mStatus;
    private int mBusyBoothId;
    private ActeinCalendarParsedResult mParsedResult;
}
