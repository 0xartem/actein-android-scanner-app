package com.actein.qr;

import com.actein.mvp.model.VrStation;
import com.google.zxing.client.result.ActeinCalendarParsedResult;

public class QrCodeProcessingResult
{
    public QrCodeProcessingResult(QrCodeStatus status,
                                  ActeinCalendarParsedResult parsedResult,
                                  VrStation vrStation)
    {
        mStatus = status;
        mVrStation = vrStation;
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

    public VrStation getVrStation()
    {
        return mVrStation;
    }

    public ActeinCalendarParsedResult getParsedResult()
    {
        return mParsedResult;
    }

    private QrCodeStatus mStatus;
    private VrStation mVrStation;
    private ActeinCalendarParsedResult mParsedResult;
}
