package com.actein.qr;

import android.content.Context;

import com.actein.data.QrCodeSettings;
import com.actein.android.utils.InternetTime;
import com.actein.utils.DateTimeUtils;
import com.google.zxing.client.result.ActeinCalendarParsedResult;

import java.util.Date;

class QrCodeValidator
{
    QrCodeValidator(Context context,
                    ActeinCalendarParsedResult acteinCalParsedResult,
                    QrCodeSettings qrCodeSettings)
    {
        mActeinCalParsedResult = acteinCalParsedResult;
        mQrCodeSettings = qrCodeSettings;
        //TODO: disable temp: mFactoryGeo = new Geo(59.046717, 10.055840);
        mSignatureVerifier = new QrCodeSignatureVerifier(context, acteinCalParsedResult);
    }

    QrCodeStatus validateQrCode()
    {
        if (!isParsedResultDataValid())
            return QrCodeStatus.QR_CODE_INVALID;

        //TODO: disable temp
        /*QrCodeStatus status = mSignatureVerifier.verify();
        if (!QrCodeStatus.isSuccess(status))
            return status;*/

        QrCodeStatus status = validateDateTime();
        if (!QrCodeStatus.isSuccess(status))
            return status;

        //TODO: disable temp:
        /*status = validateLocation();
        if (!QrCodeStatus.isSuccess(status))
            return status;*/

        return status;
    }

    private boolean isParsedResultDataValid()
    {
        for (int boothId : mActeinCalParsedResult.getBoothIds())
        {
            if (boothId <= 0)
                return false;
        }

        return (mActeinCalParsedResult.getVersion() > 0 &&
                mActeinCalParsedResult.getBid() != null &&
                mActeinCalParsedResult.getEquipment() != null &&
                mActeinCalParsedResult.getGameName() != null &&
                mActeinCalParsedResult.getSteamGameId() >= 0 &&
                mActeinCalParsedResult.getInnerCalendarResult().getStart() != null &&
                mActeinCalParsedResult.getInnerCalendarResult().getEnd() != null);
    }

    private QrCodeStatus validateDateTime()
    {
        Date now = InternetTime.getCurrentDateTime();
        Date startMinus15Min = DateTimeUtils.dateTimeMinus15Minutes(
                mActeinCalParsedResult.getInnerCalendarResult().getStart()
                );

        if (!mQrCodeSettings.isAllowEarlyQrCodes() && now.before(startMinus15Min))
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
    //TODO: disable temp: private Geo mFactoryGeo;

    private ActeinCalendarParsedResult mActeinCalParsedResult;
}
