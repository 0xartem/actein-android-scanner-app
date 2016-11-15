package com.actein.zxing.presenter;

import android.content.pm.ApplicationInfo;
import android.graphics.Bitmap;

import com.actein.vr_events.VrGameStatusProtos;
import com.actein.zxing.view.CaptureView;
import com.actein.zxing.model.ConnectionModelObserver;
import com.actein.zxing.model.ConnectionModel;
import com.actein.zxing.qr.QrCodeProcessingCallback;
import com.actein.zxing.qr.QrCodeProcessingTask;
import com.google.zxing.client.android.R;
import com.google.zxing.client.android.result.ResultHandler;

public class CaptureActivityPresenter implements CapturePresenter, ConnectionModelObserver
{
    public CaptureActivityPresenter(CaptureView captureView)
    {
        mCaptureView = captureView;
        mConnectionModel = new ConnectionModel(captureView, this);
    }

    @Override
    public void onHandleDecodeResult(
            QrCodeProcessingCallback callback,
            ResultHandler resultHandler,
            Bitmap barcode
            )
    {
        new QrCodeProcessingTask(
                mCaptureView.getActivityContext(),
                callback,
                resultHandler,
                barcode,
                mConnectionModel).execute();
    }

    @Override
    public void turnGameOff()
    {
        mConnectionModel.publishGameOffEvent();
    }

    @Override
    public void turnGameOn(String gameName, long steamGameId, long durationSeconds)
    {
        mConnectionModel.publishGameOnEvent(gameName, steamGameId, durationSeconds);
    }

    @Override
    public synchronized boolean isGameTurnedOn()
    {
        return mCurStatus == VrGameStatusProtos.VrGameStatus.GAME_ON;
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
            mCaptureView.showToast(message);
        }
    }

    @Override
    public void onDisconnected(String message)
    {
        if (isDebug())
        {
            mCaptureView.showToast(message);
        }
    }

    @Override
    public void onSubscribed(String message)
    {
        if (isDebug())
        {
            mCaptureView.showToast(message);
        }
    }

    @Override
    public void onUnsubscribed(String message)
    {
        if (isDebug())
        {
            mCaptureView.showToast(message);
        }
    }

    @Override
    public void onPublished(String message)
    {
        if (isDebug())
        {
            mCaptureView.showToast(message);
        }
    }

    @Override
    public void onConnectionLost()
    {
        mCaptureView.showErrorDialog(
                mCaptureView.getActivityContext().getString(R.string.msg_connection_lost)
                );
    }

    @Override
    public synchronized void onVrEventStatusReceived(VrGameStatusProtos.VrGameStatus status,
                                                     String message)
    {
        mCurStatus = status;
        if (isDebug())
        {
            mCaptureView.showToast(message);
        }
        mCaptureView.onGameStateChanged(status == VrGameStatusProtos.VrGameStatus.GAME_ON);
    }

    @Override
    public void onError(String message)
    {
        mCaptureView.showErrorDialog(message);
    }

    private boolean isDebug()
    {
        return (mCaptureView.getApplicationContext().getApplicationInfo().flags &
                ApplicationInfo.FLAG_DEBUGGABLE) != 0;
    }

    private CaptureView mCaptureView;
    private ConnectionModel mConnectionModel;
    private VrGameStatusProtos.VrGameStatus mCurStatus = VrGameStatusProtos.VrGameStatus.UNKNOWN;
}
