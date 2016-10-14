package com.actein.zxing.qr;

import com.actein.zxing.model.BoothSettings;
import com.actein.android.utils.InternetTime;
import com.actein.utils.Geo;
import com.actein.utils.DateTimeUtils;
import com.google.zxing.client.result.ActeinCalendarParsedResult;

import java.util.Date;

public class QrCodeValidator
{
    public QrCodeValidator(ActeinCalendarParsedResult acteinCalParsedResult,
                           QrCodeSettings qrCodeSettings,
                           BoothSettings boothSettings)
    {
        mActeinCalParsedResult = acteinCalParsedResult;
        mQrCodeSettings = qrCodeSettings;
        mBoothSettings = boothSettings;
        mFactoryGeo = new Geo(59.046717, 10.055840);
    }

    public QrCodeStatus validateQrCode()
    {
        if (mActeinCalParsedResult.getInnerCalendarResult().getStart() == null ||
            mActeinCalParsedResult.getInnerCalendarResult().getEnd() == null ||
            mActeinCalParsedResult.getEventType() == null ||
            mActeinCalParsedResult.getGame() == null ||
            mActeinCalParsedResult.getBoothId() <= 0)
        {
            return QrCodeStatus.QR_CODE_INVALID;
        }

        Date now = InternetTime.getCurrentInternetDateTime();
        Date nowMinus5Min = DateTimeUtils.dateTimeMinus5Minutes(now);

        if (!mQrCodeSettings.isAllowEarlyQrCodes() &&
            mActeinCalParsedResult.getInnerCalendarResult().getStart().after(nowMinus5Min))
        {
            return QrCodeStatus.QR_CODE_NOT_STARTED_YET;
        }

        if (!mQrCodeSettings.isAllowExpiredQrCodes() &&
            mActeinCalParsedResult.getInnerCalendarResult().getEnd().before(now))
        {
            return QrCodeStatus.QR_CODE_EXPIRED;
        }

        if (mActeinCalParsedResult.getBoothId() != mBoothSettings.getBoothId())
        {
            return QrCodeStatus.WRONG_BOOTH;
        }

        if (mActeinCalParsedResult.getInnerCalendarResult().getLatitude() !=
            mFactoryGeo.getLatitude() ||
            mActeinCalParsedResult.getInnerCalendarResult().getLongitude() !=
            mFactoryGeo.getLongitude())
        {
            return QrCodeStatus.WRONG_LOCATION;
        }

        return QrCodeStatus.SUCCESS;
    }

    private QrCodeSettings mQrCodeSettings;
    private BoothSettings mBoothSettings;
    private Geo mFactoryGeo;
    private ActeinCalendarParsedResult mActeinCalParsedResult;
}
