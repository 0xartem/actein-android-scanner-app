package com.actein.zxing.presenter;

import android.content.pm.ApplicationInfo;
import android.graphics.Bitmap;

import com.actein.mvp.ActivityView;
import com.actein.mvp.Presenter;
import com.actein.zxing.model.ConnectionModelObserver;
import com.actein.zxing.model.ConnectionModel;
import com.actein.zxing.qr.QrCodeProcessingCallback;
import com.actein.zxing.qr.QrCodeProcessingTask;
import com.google.zxing.client.android.R;
import com.google.zxing.client.android.result.ResultHandler;

public class CaptureActivityPresenter implements Presenter, ConnectionModelObserver
{
    public CaptureActivityPresenter(ActivityView activityView)
    {
        mActivityView = activityView;
        mConnectionModel = new ConnectionModel(activityView, this);
    }

    public void onHandleDecodeResult(
            QrCodeProcessingCallback callback,
            ResultHandler resultHandler,
            Bitmap barcode
            )
    {
        new QrCodeProcessingTask(
                mActivityView.getActivityContext(),
                callback,
                resultHandler,
                barcode,
                mConnectionModel).execute();
    }

    @Override
    public void onCreate()
    {
        mConnectionModel.onCreate();
    }

    @Override
    public void onDestroy(boolean isChangingConfiguration)
    {
        mConnectionModel.onDestroy(isChangingConfiguration);
    }


    // ConnectionModelObserver implementation
    @Override
    public void onConnected(String message)
    {
        if (isDebug())
        {
            mActivityView.showToast(message);
        }
    }

    @Override
    public void onDisconnected(String message)
    {
        if (isDebug())
        {
            mActivityView.showToast(message);
        }
    }

    @Override
    public void onSubscribed(String message)
    {
        if (isDebug())
        {
            mActivityView.showToast(message);
        }
    }

    @Override
    public void onUnsubscribed(String message)
    {
        if (isDebug())
        {
            mActivityView.showToast(message);
        }
    }

    @Override
    public void onPublished(String message)
    {
        if (isDebug())
        {
            mActivityView.showToast(message);
        }
    }

    @Override
    public void onConnectionLost()
    {
        mActivityView.showErrorDialog(
                mActivityView.getActivityContext().getString(R.string.msg_connection_lost)
                );
    }

    @Override
    public void onError(String message)
    {
        mActivityView.showErrorDialog(message);
    }

    private boolean isDebug()
    {
        return (mActivityView.getApplicationContext().getApplicationInfo().flags &
                ApplicationInfo.FLAG_DEBUGGABLE) != 0;
    }

    private ActivityView mActivityView;
    private ConnectionModel mConnectionModel;
}
