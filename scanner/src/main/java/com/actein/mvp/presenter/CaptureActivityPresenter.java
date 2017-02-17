package com.actein.mvp.presenter;

import android.content.DialogInterface;
import android.graphics.Bitmap;

import com.actein.event.ConnectionLostEvent;
import com.actein.event.ErrorEvent;
import com.actein.event.InfoEvent;
import com.actein.event.PcOfflineEvent;
import com.actein.mvp.model.CommonObserver;
import com.actein.qr.QrCodeProcessingResult;
import com.actein.mvp.view.CaptureView;
import com.actein.scanner.R;
import com.actein.qr.QrCodeProcessingCallback;
import com.actein.qr.QrCodeProcessingTask;
import com.google.zxing.client.android.result.ResultHandler;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

public class CaptureActivityPresenter implements CapturePresenter,
        QrCodeProcessingCallback,
        CommonObserver
{
    public CaptureActivityPresenter(CaptureView captureView)
    {
        mCaptureView = captureView;
    }

    @Override
    public synchronized void onHandleDecodeResult(ResultHandler resultHandler,
                                                  Bitmap barcode)
    {
        new QrCodeProcessingTask(mCaptureView.getActivityContext(),
                                 this,
                                 resultHandler,
                                 barcode).execute();
    }

    @Override
    public void onQrCodeValidated(QrCodeProcessingResult result,
                                  ResultHandler resultHandler,
                                  Bitmap barCode)
    {
        if (result.getVrStation().isGameRunning())
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
        else
        {
            mCaptureView.processScanningResult(result, resultHandler, barCode);
        }
    }

    @Override
    public void onCreate()
    {
        mEventReceiver = new EventReceiver();
        EventBus.getDefault().register(mEventReceiver);
    }

    @Override
    public void onDestroy(boolean isChangingConfiguration)
    {
        EventBus.getDefault().unregister(mEventReceiver);
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

    private CaptureView mCaptureView;
    private EventReceiver mEventReceiver;

    private class EventReceiver
    {
        @Subscribe
        public void onConnecionLostEventReceived(ConnectionLostEvent event)
        {
            if (event.showErrorMessage)
            {
                mCaptureView.showErrorDialog(
                        mCaptureView.getActivityContext().getString(R.string.msg_connection_lost)
                        );
            }
        }

        @Subscribe
        public void onPcOfflineEventReceived(PcOfflineEvent event)
        {
            if (event.showErrorMessage)
            {
                mCaptureView.showErrorDialog(
                        mCaptureView.getActivityContext().getString(R.string.msg_request_pc_offline)
                        );
            }
        }

        @Subscribe
        public void onErrorEventReceived(ErrorEvent event)
        {
            onError(event.message);
        }

        @Subscribe
        public void onInfoEventReceived(InfoEvent event)
        {
            onInfo(event.message);
        }
    }
}
