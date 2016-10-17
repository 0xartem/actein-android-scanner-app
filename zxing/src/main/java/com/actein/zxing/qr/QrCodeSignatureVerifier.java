package com.actein.zxing.qr;

import android.util.Base64;
import android.util.Log;

import com.actein.utils.security.DigitalSignatureAlgorithm;
import com.actein.utils.security.DigitalSignatureException;
import com.google.zxing.client.result.ActeinCalendarParsedResult;

class QrCodeSignatureVerifier
{
    QrCodeSignatureVerifier(ActeinCalendarParsedResult parsedResult)
    {
        mParsedResult = parsedResult;
    }

    QrCodeStatus verify()
    {
        try
        {
            byte[] rawPublicKey = Base64.decode(mParsedResult.getPublicKey(), Base64.DEFAULT);
            DigitalSignatureAlgorithm algorithm = new DigitalSignatureAlgorithm(rawPublicKey);
            byte[] rawSignature = Base64.decode(mParsedResult.getSignature(), Base64.DEFAULT);
            algorithm.verifyData(mParsedResult.getSignedData(), rawSignature);
            return QrCodeStatus.SUCCESS;
        }
        catch (DigitalSignatureException ex)
        {
            Log.e(TAG, ex.getMessage(), ex);
        }
        return QrCodeStatus.DIGITAL_SIGNATURE_INVALID;
    }

    private static String TAG = QrCodeSignatureVerifier.class.getSimpleName();
    private ActeinCalendarParsedResult mParsedResult;
}
