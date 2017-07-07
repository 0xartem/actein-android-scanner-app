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

    public synchronized void start(final VrStation vrStation)
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
    }

    public synchronized void stop()
    {
        if (mGameCountDownTimer != null)
        {
            new Handler().post(new Runnable()
            {
                @Override
                public void run()
                {
                    mGameCountDownTimer.cancel();
                    mGameCountDownTimer.onFinish();
                }
            });
        }
    }

    private CountDownTimer mGameCountDownTimer;
    private VrStationsModel mVrStationModel;
}
