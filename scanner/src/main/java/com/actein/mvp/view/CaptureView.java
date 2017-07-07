package com.actein.mvp.view;

import android.graphics.Bitmap;

import com.actein.qr.QrCodeProcessingResult;
import com.google.zxing.client.android.result.ResultHandler;

public interface CaptureView extends ActivityView
{
    void restartPreviewAfterDelay(long delayMS);
    void processScanningResult(QrCodeProcessingResult result,
                               ResultHandler resultHandler,
                               Bitmap barCode);
}
