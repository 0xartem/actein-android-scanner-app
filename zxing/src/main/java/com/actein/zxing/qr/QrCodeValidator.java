package com.actein.zxing.qr;

import android.content.Context;

import com.actein.zxing.model.BoothSettings;
import com.actein.android.utils.InternetTime;
import com.actein.utils.Geo;
import com.actein.utils.DateTimeUtils;
import com.google.zxing.client.result.ActeinCalendarParsedResult;

import java.util.Date;

public class QrCodeValidator
{
    public QrCodeValidator(Context context,
                           ActeinCalendarParsedResult acteinCalParsedResult,
                           QrCodeSettings qrCodeSettings,
                           BoothSettings boothSettings)
    {
        mActeinCalParsedResult = acteinCalParsedResult;
        mQrCodeSettings = qrCodeSettings;
        mBoothSettings = boothSettings;
        mFactoryGeo = new Geo(59.046717, 10.055840);
        mSignatureVerifier = new QrCodeSignatureVerifier(context, acteinCalParsedResult);
    }

    public QrCodeStatus validateQrCode()
    {
        if (!isParsedResultDataValid())
            return QrCodeStatus.QR_CODE_INVALID;

        QrCodeStatus status = mSignatureVerifier.verify();
        if (!QrCodeStatus.isSuccess(status))
            return status;

        status = validateDateTime();
        if (!QrCodeStatus.isSuccess(status))
            return status;

        if (!isBoothValid())
            return QrCodeStatus.WRONG_BOOTH;

        status = validateLocation();
        if (!QrCodeStatus.isSuccess(status))
            return status;

        return status;
    }

    private boolean isParsedResultDataValid()
    {
        return (mActeinCalParsedResult.getVersion() > 0 &&
                mActeinCalParsedResult.getBid() != null &&
                mActeinCalParsedResult.getEventType() != null &&
                mActeinCalParsedResult.getEquipment() != null &&
                mActeinCalParsedResult.getGameName() != null &&
                mActeinCalParsedResult.getSteamGameId() >= 0 &&
                mActeinCalParsedResult.getBoothId() > 0 &&
                mActeinCalParsedResult.getSignature() != null &&
                mActeinCalParsedResult.getSignedData() != null &&
                mActeinCalParsedResult.getInnerCalendarResult().getStart() != null &&
                mActeinCalParsedResult.getInnerCalendarResult().getEnd() != null);
    }

    private QrCodeStatus validateDateTime()
    {
        Date now = InternetTime.getCurrentDateTime();
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

        return QrCodeStatus.SUCCESS;
    }

    private boolean isBoothValid()
    {
        return mActeinCalParsedResult.getBoothId() == mBoothSettings.getBoothId();
    }

    private QrCodeStatus validateLocation()
    {
        if (mActeinCalParsedResult.getInnerCalendarResult().getLatitude() !=
            mFactoryGeo.getLatitude() ||
            mActeinCalParsedResult.getInnerCalendarResult().getLongitude() !=
            mFactoryGeo.getLongitude())
        {
            return QrCodeStatus.WRONG_LOCATION;
        }
        return QrCodeStatus.SUCCESS;
    }

    private QrCodeSignatureVerifier mSignatureVerifier;
    private QrCodeSettings mQrCodeSettings;
    private BoothSettings mBoothSettings;
    private Geo mFactoryGeo;

    private ActeinCalendarParsedResult mActeinCalParsedResult;
}
