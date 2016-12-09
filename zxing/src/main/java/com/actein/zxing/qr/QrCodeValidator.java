package com.actein.zxing.qr;

import android.content.Context;

import com.actein.zxing.model.BoothSettings;
import com.actein.android.utils.InternetTime;
import com.actein.utils.DateTimeUtils;
import com.google.zxing.client.result.ActeinCalendarParsedResult;

import java.util.Date;

class QrCodeValidator
{
    QrCodeValidator(Context context,
                    ActeinCalendarParsedResult acteinCalParsedResult,
                    QrCodeSettings qrCodeSettings,
                    BoothSettings boothSettings)
    {
        mActeinCalParsedResult = acteinCalParsedResult;
        mQrCodeSettings = qrCodeSettings;
        mBoothSettings = boothSettings;
        //TODO: disable temp: mFactoryGeo = new Geo(59.046717, 10.055840);
        mSignatureVerifier = new QrCodeSignatureVerifier(context, acteinCalParsedResult);
    }

    QrCodeStatus validateQrCode()
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

        //TODO: disable temp:
        /*status = validateLocation();
        if (!QrCodeStatus.isSuccess(status))
            return status;*/

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
        Date startMinus5Min = DateTimeUtils.dateTimeMinus5Minutes(
                mActeinCalParsedResult.getInnerCalendarResult().getStart()
                );

        if (!mQrCodeSettings.isAllowEarlyQrCodes() && now.before(startMinus5Min))
        {
            return QrCodeStatus.QR_CODE_NOT_STARTED_YET;
        }

        if (!mQrCodeSettings.isAllowExpiredQrCodes() &&
            now.after(mActeinCalParsedResult.getInnerCalendarResult().getEnd()))
        {
            return QrCodeStatus.QR_CODE_EXPIRED;
        }

        return QrCodeStatus.SUCCESS;
    }

    private boolean isBoothValid()
    {
        return mActeinCalParsedResult.getBoothId() == mBoothSettings.getBoothId();
    }

    //TODO: disable temp:
    /*private QrCodeStatus validateLocation()
    {
        if (mActeinCalParsedResult.getInnerCalendarResult().getLatitude() !=
            mFactoryGeo.getLatitude() ||
            mActeinCalParsedResult.getInnerCalendarResult().getLongitude() !=
            mFactoryGeo.getLongitude())
        {
            return QrCodeStatus.WRONG_LOCATION;
        }
        return QrCodeStatus.SUCCESS;
    }*/

    private QrCodeSignatureVerifier mSignatureVerifier;
    private QrCodeSettings mQrCodeSettings;
    private BoothSettings mBoothSettings;
    //TODO: disable temp: private Geo mFactoryGeo;

    private ActeinCalendarParsedResult mActeinCalParsedResult;
}
