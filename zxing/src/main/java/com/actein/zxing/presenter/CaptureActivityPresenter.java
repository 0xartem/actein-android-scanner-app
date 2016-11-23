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
    public synchronized void onHandleDecodeResult(
            QrCodeProcessingCallback callback,
            ResultHandler resultHandler,
            Bitmap barcode
            )
    {
        if (mCurStatus != VrGameStatusProtos.VrGameStatus.GAME_ON)
        {
            new QrCodeProcessingTask(mCaptureView.getActivityContext(),
                                     callback,
                                     resultHandler,
                                     barcode,
                                     mConnectionModel).execute();
        }
        else
        {
            onInfo(mCaptureView.getActivityContext()
                               .getResources()
                               .getString(R.string.msg_game_already_running));
        }
    }

    @Override
    public void turnGameOff()
    {
        if (mConnectionModel.isConnected())
        {
            mConnectionModel.publishGameOffEvent();
        }
        else
        {
            onConnectionLost();
        }
    }

    @Override
    public void turnGameOn(String gameName,
                           long steamGameId,
                           long durationSeconds,
                           boolean runTutorial)
    {
        if (mConnectionModel.isConnected())
        {
            mConnectionModel.publishGameOnEvent(gameName, steamGameId, durationSeconds, runTutorial);
        }
        else
        {
            onConnectionLost();
        }
    }

    @Override
    public synchronized boolean isGameRunning()
    {
        return mCurStatus == VrGameStatusProtos.VrGameStatus.GAME_ON ||
               mCurStatus == VrGameStatusProtos.VrGameStatus.TUTORIAL_ON;
    }

    @Override
    public synchronized boolean isGameStopped()
    {
        return mCurStatus == VrGameStatusProtos.VrGameStatus.GAME_OFF;
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

        if (isGameRunning())
        {
            mCaptureView.onGameRunning();
        }
        else if (isGameStopped())
        {
            mCaptureView.onGameStopped();
        }
        else
        {
            mCaptureView.onGameLoading();
        }
    }

    @Override
    public void onError(String message)
    {
        mCaptureView.showErrorDialog(message);
    }

    @Override
    public void onInfo(String message)
    {
        mCaptureView.showInfoDialog(message);
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
