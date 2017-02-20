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
                vrStation.setTime(millisUntilFinished / 1000);
                if (mVrStationModel.getObserver() != null)
                    mVrStationModel.getObserver().onVrStationUpdated();
            }

            public void onFinish()
            {
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
                    if (mVrStationModel.getObserver() != null)
                        mVrStationModel.getObserver().onVrStationUpdated();
                }
            });
        }
    }

    private CountDownTimer mGameCountDownTimer;
    private VrStationsModel mVrStationModel;
}
