package com.actein.zxing.presenter;

import android.graphics.Bitmap;

import com.actein.mvp.Presenter;
import com.actein.zxing.qr.QrCodeProcessingCallback;
import com.google.zxing.client.android.result.ResultHandler;

public interface CapturePresenter extends Presenter
{
    void onHandleDecodeResult(QrCodeProcessingCallback callback,
                              ResultHandler resultHandler,
                              Bitmap barcode);
    void turnGameOnOff(boolean state);
}
