package com.actein.zxing.qr;

import android.content.Context;
import android.util.Base64;
import android.util.Log;

import com.actein.utils.security.DigitalSignatureAlgorithm;
import com.actein.utils.security.DigitalSignatureException;
import com.google.zxing.client.android.R;
import com.google.zxing.client.result.ActeinCalendarParsedResult;

class QrCodeSignatureVerifier
{
    QrCodeSignatureVerifier(Context context, ActeinCalendarParsedResult parsedResult)
    {
        mContext = context;
        mParsedResult = parsedResult;
    }

    QrCodeStatus verify()
    {
        try
        {
            String publicKeyBase64 = mContext.getString(
                    R.string.qr_code_public_key_base_64
            );
            byte[] rawPublicKey = Base64.decode(publicKeyBase64, Base64.DEFAULT);
            byte[] rawSignature = Base64.decode(mParsedResult.getSignature(), Base64.DEFAULT);

            DigitalSignatureAlgorithm algorithm = new DigitalSignatureAlgorithm(rawPublicKey);
            if (!algorithm.verifyData(mParsedResult.getSignedData(), rawSignature))
            {
                return QrCodeStatus.DIGITAL_SIGNATURE_INVALID;
            }
            return QrCodeStatus.SUCCESS;
        }
        catch (DigitalSignatureException ex)
        {
            Log.e(TAG, ex.getMessage(), ex);
        }
        return QrCodeStatus.DIGITAL_SIGNATURE_INVALID;
    }

    private Context mContext;
    private ActeinCalendarParsedResult mParsedResult;

    private static String TAG = QrCodeSignatureVerifier.class.getSimpleName();
}
