package com.actein.mvp.presenter;

import android.content.DialogInterface;
import android.content.pm.ApplicationInfo;
import android.graphics.Bitmap;
import android.os.CountDownTimer;
import android.os.Handler;

import com.actein.data.BoothSettings;
import com.actein.data.Preferences;
import com.actein.transport.mqtt.Connection;
import com.actein.transport.mqtt.policies.PreciseDeliveryConnectionPolicy;
import com.actein.vr_events.VrGameStatusProtos;
import com.actein.mvp.view.CaptureView;
import com.actein.mvp.model.ConnectionModelObserver;
import com.actein.mvp.model.ConnectionModel;
import com.actein.scanner.R;
import com.actein.qr.QrCodeProcessingCallback;
import com.actein.qr.QrCodeProcessingTask;
import com.google.zxing.client.android.result.ResultHandler;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class CaptureActivityPresenter implements CapturePresenter, ConnectionModelObserver
{
    public CaptureActivityPresenter(CaptureView captureView)
    {
        mCaptureView = captureView;
        BoothSettings boothSettings = new BoothSettings(captureView.getActivityContext());

        Connection connection = Connection.createInstance(
                captureView.getApplicationContext(),
                Preferences.getBrokerAddr(captureView.getApplicationContext()),
                new PreciseDeliveryConnectionPolicy(boothSettings.getBoothId())
                );

        mConnectionModel = new ConnectionModel(connection, boothSettings, this);
    }

    @Override
    public synchronized void onHandleDecodeResult(QrCodeProcessingCallback callback,
                                                  ResultHandler resultHandler,
                                                  Bitmap barcode)
    {
        if (isGameStopped())
        {
            new QrCodeProcessingTask(mCaptureView.getActivityContext(),
                                     callback,
                                     resultHandler,
                                     barcode,
                                     this,
                                     mConnectionModel.getBoothSettings()).execute();
        }
        else
        {
            mCaptureView.showInfoDialog(mCaptureView.getActivityContext()
                                                    .getResources()
                                                    .getString(R.string.msg_game_already_running),
                                        new DialogInterface.OnClickListener()
                                        {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which)
                                            {
                                                mCaptureView.restartPreviewAfterDelay(0L);
                                            }
                                        },
                                        new DialogInterface.OnCancelListener()
                                        {
                                            @Override
                                            public void onCancel(DialogInterface dialog)
                                            {
                                                mCaptureView.restartPreviewAfterDelay(0L);
                                            }
                                        });
        }
    }

    @Override
    public synchronized void turnGameOff()
    {
        if (!mConnectionModel.isConnected())
        {
            onConnectionLost(true);
        }
        else if (!mConnectionModel.isPcOnline())
        {
            onPcOffline(true);
        }
        else
        {
            mConnectionModel.publishGameOffEvent();
        }
    }

    @Override
    public synchronized void turnGameOn(String gameName,
                           long steamGameId,
                           long durationSeconds,
                           boolean runTutorial)
    {
        if (!mConnectionModel.isConnected())
        {
            onConnectionLost(true);
        }
        else if (!mConnectionModel.isPcOnline())
        {
            onPcOffline(true);
        }
        else
        {
            mConnectionModel.publishGameOnEvent(gameName, steamGameId, durationSeconds, runTutorial);
            mCurrentDurationSeconds = durationSeconds;
        }
    }

    @Override
    public synchronized boolean isGameRunning()
    {
        return mCurStatus == VrGameStatusProtos.VrGameStatus.GAME_ON ||
               mCurStatus == VrGameStatusProtos.VrGameStatus.TUTORIAL_ON;
    }

    @Override
    public synchronized boolean isGameAboutToStart()
    {
        return mCurStatus == VrGameStatusProtos.VrGameStatus.STARTING_GAME ||
               mCurStatus == VrGameStatusProtos.VrGameStatus.STARTING_TUTORIAL;
    }

    @Override
    public synchronized boolean isGameAboutToStop()
    {
        return mCurStatus == VrGameStatusProtos.VrGameStatus.STOPPING_GAME;
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
    public void onPcOnline()
    {
        mCaptureView.changePcOnlineStatus(true);
    }

    @Override
    public void onPcOffline(boolean sendingRequest)
    {
        updateStarStopGameView();
        if (sendingRequest)
        {
            mCaptureView.showErrorDialog(
                    mCaptureView.getActivityContext().getString(R.string.msg_request_pc_offline)
                    );
        }
        else
        {
            mCaptureView.changePcOnlineStatus(false);
        }
    }

    @Override
    public void updateOnlineStatus()
    {
        mCaptureView.changePcOnlineStatus(mConnectionModel.isPcOnline());
    }

    @Override
    public void onConnectionLost(boolean showErrorMsg)
    {
        updateStarStopGameView();
        if (showErrorMsg)
        {
            mCaptureView.showErrorDialog(
                    mCaptureView.getActivityContext().getString(R.string.msg_connection_lost)
                    );
        }
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

        updateCountDownView();
        updateStarStopGameView();
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

    @Override
    public void updateStarStopGameView()
    {
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

    private void updateCountDownView()
    {
        if (isGameRunning())
        {
            mGameCountDownTimer = new CountDownTimer(mCurrentDurationSeconds * 1000, 1000)
            {
                public void onTick(long millisUntilFinished)
                {
                    mCurrentDurationSeconds = millisUntilFinished / 1000;
                    DateFormat formatter = new SimpleDateFormat("HH:mm:ss");
                    formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
                    String timeLeft = formatter.format(new Date(millisUntilFinished));
                    mCaptureView.onCountDownTick(timeLeft);
                }

                public void onFinish()
                {
                    mCaptureView.onCountDownFinish();
                }
            }.start();
            mCaptureView.onCountDownStart();
        }
        else if (isGameStopped() && mGameCountDownTimer != null)
        {
            new Handler().post(new Runnable()
            {
                @Override
                public void run()
                {
                    mGameCountDownTimer.cancel();
                    mCaptureView.onCountDownFinish();
                    mCurrentDurationSeconds = 0;
                }
            });
        }
    }

    private boolean isDebug()
    {
        return (mCaptureView.getApplicationContext().getApplicationInfo().flags &
                ApplicationInfo.FLAG_DEBUGGABLE) != 0;
    }

    private CaptureView mCaptureView;
    private ConnectionModel mConnectionModel;
    private VrGameStatusProtos.VrGameStatus mCurStatus = VrGameStatusProtos.VrGameStatus.UNKNOWN;

    private CountDownTimer mGameCountDownTimer = null;
    private long mCurrentDurationSeconds = 0;
}
