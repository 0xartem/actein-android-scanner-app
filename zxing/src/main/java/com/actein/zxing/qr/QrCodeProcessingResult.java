package com.actein.zxing.qr;

import com.google.zxing.client.result.ActeinCalendarParsedResult;

public class QrCodeProcessingResult
{
    public QrCodeProcessingResult(QrCodeStatus status, ActeinCalendarParsedResult parsedResult)
    {
        mStatus = status;
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

    public ActeinCalendarParsedResult getParsedResult()
    {
        return mParsedResult;
    }

    private QrCodeStatus mStatus;
    private ActeinCalendarParsedResult mParsedResult;
}
