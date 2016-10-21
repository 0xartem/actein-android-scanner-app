package com.actein.zxing.qr;

import android.graphics.Bitmap;

import com.google.zxing.client.android.result.ResultHandler;

public interface QrCodeProcessingCallback
{
    void onQrCodeValidated(QrCodeProcessingResult result, ResultHandler resultHandler, Bitmap barCode);
}
