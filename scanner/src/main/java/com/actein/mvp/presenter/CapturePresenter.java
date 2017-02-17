package com.actein.mvp.presenter;

import android.graphics.Bitmap;

import com.google.zxing.client.android.result.ResultHandler;

public interface CapturePresenter extends Presenter
{
    void onHandleDecodeResult(ResultHandler resultHandler,
                              Bitmap barcode);
}
