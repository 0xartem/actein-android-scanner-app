package com.actein.helpers;

import android.os.CountDownTimer;
import android.os.Handler;

import com.actein.mvp.model.VrStation;
import com.actein.mvp.model.VrStationsModel;

public class VrStationTimer
{
    public VrStationTimer(VrStationsModel vrStationsModel)
    {
        mVrStationModel = vrStationsModel;
    }

    public void start(final VrStation vrStation)
    {
        synchronized (locker)
        {
            mGameCountDownTimer = new CountDownTimer(vrStation.getTime() * 1000, 1000)
            {
                public void onTick(long millisUntilFinished)
                {
                    final long secondsLeft = millisUntilFinished / 1000;
                    vrStation.setTime(secondsLeft);
                }

                public void onFinish()
                {
                    vrStation.setTime(0);
                    if (mVrStationModel.getObserver() != null)
                        mVrStationModel.getObserver().onVrStationUpdated();
                }
            }.start();

            mRunning = true;
        }
    }

    public boolean isRunning()
    {
        synchronized (locker)
        {
            return mRunning;
        }
    }

    public synchronized void stop()
    {
        synchronized (locker)
        {
            if (mGameCountDownTimer != null)
            {
                new Handler().post(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        synchronized (locker)
                        {
                            mGameCountDownTimer.cancel();
                            mGameCountDownTimer.onFinish();
                            mGameCountDownTimer = null;
                            mRunning = false;
                        }
                    }
                });
            }
        }
    }

    private final Object locker = new Object();
    private CountDownTimer mGameCountDownTimer;
    private VrStationsModel mVrStationModel;
    private boolean mRunning = false;
}
