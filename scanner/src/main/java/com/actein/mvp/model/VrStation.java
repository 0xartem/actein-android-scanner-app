package com.actein.mvp.model;


import android.os.CountDownTimer;
import android.os.Handler;

import com.actein.scanner.R;
import com.actein.transport.mqtt.OnlineStatusProtos;
import com.actein.vr_events.VrGameStatusProtos;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class VrStation
{
    public VrStation(int boothId, VrStationsModel vrStationsModel)
    {
        mVrStationsModel = vrStationsModel;
        mBoothId = boothId;
    }

    public synchronized boolean isGameRunning()
    {
        return mVrGameStatus == VrGameStatusProtos.VrGameStatus.GAME_ON ||
               mVrGameStatus == VrGameStatusProtos.VrGameStatus.TUTORIAL_ON;
    }

    public synchronized boolean isGameStopped()
    {
        return mVrGameStatus == VrGameStatusProtos.VrGameStatus.GAME_OFF;
    }

    public synchronized boolean isOnline()
    {
        return mPcOnlineStatus == OnlineStatusProtos.OnlineStatus.ONLINE;
    }

    public synchronized void setBoothId(final int boothId)
    {
        mBoothId = boothId;
    }

    public synchronized int getBoothId()
    {
        return mBoothId;
    }

    public synchronized void setEquipment(final String equipment)
    {
        mEquipment = equipment;
    }

    public synchronized String getEquipment()
    {
        return mEquipment;
    }

    public synchronized void setVrGameStatus(final VrGameStatusProtos.VrGameStatus status)
    {
        mVrGameStatus = status;
        if (isGameStopped())
        {
            setExperience("");
            setTime(0);
        }
        updateCountDownView();
    }

    public synchronized int getRunningIcon()
    {
        if (isGameRunning())
        {
            return R.drawable.stop;
        }
        else if (isGameStopped())
        {
            return R.drawable.start;
        }
        else
        {
            return R.layout.start_game_progress_bar;
        }
    }

    public synchronized void setPcOnlineStatus(OnlineStatusProtos.OnlineStatus status)
    {
        mPcOnlineStatus = status;
    }

    public synchronized int getOnlineIcon()
    {
        if (mPcOnlineStatus == OnlineStatusProtos.OnlineStatus.ONLINE)
        {
            return R.drawable.monitor_blue;
        }
        else
        {
            return R.drawable.monitor_grey;
        }
    }

    public synchronized void setExperience(final String experience)
    {
        mExperience = experience;
    }

    public synchronized String getExperience()
    {
        return mExperience;
    }

    public synchronized void setTime(final long time)
    {
        mTime = time;
    }

    public synchronized long getTime()
    {
        return mTime;
    }

    public synchronized String getTimeStr()
    {
        DateFormat formatter = new SimpleDateFormat("HH:mm:ss");
        formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
        return formatter.format(new Date(mTime * 1000));
    }

    private void updateCountDownView()
    {
        if (isGameRunning())
        {
            mGameCountDownTimer = new CountDownTimer(mTime * 1000, 1000)
            {
                public void onTick(long millisUntilFinished)
                {
                    setTime(millisUntilFinished / 1000);
                    if (mVrStationsModel.getObserver() != null)
                        mVrStationsModel.getObserver().onVrStationUpdated();
                }

                public void onFinish()
                {
                    if (mVrStationsModel.getObserver() != null)
                        mVrStationsModel.getObserver().onVrStationUpdated();
                }
            }.start();
        }
        else if (isGameStopped() && mGameCountDownTimer != null)
        {
            new Handler().post(new Runnable()
            {
                @Override
                public void run()
                {
                    mGameCountDownTimer.cancel();
                    if (mVrStationsModel.getObserver() != null)
                        mVrStationsModel.getObserver().onVrStationUpdated();
                }
            });
        }
    }

    private VrStationsModel mVrStationsModel;

    private int mBoothId;
    private String mEquipment = "";
    private String mExperience = "";
    private long mTime;
    private CountDownTimer mGameCountDownTimer;

    private VrGameStatusProtos.VrGameStatus mVrGameStatus = VrGameStatusProtos.VrGameStatus.UNKNOWN;
    private OnlineStatusProtos.OnlineStatus mPcOnlineStatus = OnlineStatusProtos.OnlineStatus.UNKNOWN;
}
