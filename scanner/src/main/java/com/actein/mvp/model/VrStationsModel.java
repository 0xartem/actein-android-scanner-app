package com.actein.mvp.model;


import android.util.Log;

import com.actein.event.PcOfflineEvent;
import com.actein.event.PcOnlineEvent;
import com.actein.transport.mqtt.OnlineStatusProtos;
import com.actein.transport.mqtt.interfaces.PcOnlineStatusHandler;
import com.actein.vr_events.VrBoothInfoProtos;
import com.actein.vr_events.VrGameOffProtos;
import com.actein.vr_events.VrGameOnProtos;
import com.actein.vr_events.VrGameStatusProtos;
import com.actein.vr_events.interfaces.VrEventsHandler;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

public class VrStationsModel implements Model, VrEventsHandler, PcOnlineStatusHandler
{
    private VrStationsModel()
    {
    }

    public static VrStationsModel getInstance()
    {
        if (mSelf == null)
            mSelf = new VrStationsModel();
        return mSelf;
    }

    public synchronized void setObserver(VrStationsModelObserver vrStationsModelObserver)
    {
        mVrStationModelObserver = vrStationsModelObserver;
    }

    synchronized VrStationsModelObserver getObserver()
    {
        return mVrStationModelObserver;
    }

    @Override
    public void onCreate()
    {
    }

    @Override
    public void onDestroy(boolean isChangingConfiguration)
    {
    }

    @Override
    public synchronized void handleVrGameOnEvent(VrBoothInfoProtos.VrBoothInfo boothInfo,
                                                 VrGameOnProtos.VrGameOnEvent event)
    {
        Log.i(TAG, "The Game ON event received. Booth: " + boothInfo.getId());

        VrStation vrStation = ensureVrStationAdded(boothInfo.getId());
        vrStation.setExperience(event.getGame().getGameName());
        vrStation.setTime(event.getGame().getGameDurationSeconds());

        if (mVrStationModelObserver != null)
            mVrStationModelObserver.onVrStationUpdated();
    }

    @Override
    public synchronized void handleVrGameOffEvent(VrBoothInfoProtos.VrBoothInfo boothInfo,
                                                  VrGameOffProtos.VrGameOffEvent event)
    {
        Log.i(TAG, "The Game OFF event received. Booth: " + boothInfo.getId());

        ensureVrStationAdded(boothInfo.getId());

        if (mVrStationModelObserver != null)
            mVrStationModelObserver.onVrStationUpdated();
    }

    @Override
    public synchronized void handleVrGameStatusEvent(VrBoothInfoProtos.VrBoothInfo boothInfo,
                                                     VrGameStatusProtos.VrGameStatusEvent event)
    {
        StringBuilder messageBuilder = new StringBuilder();
        messageBuilder.append("The status event received: ").append(event.getStatus().toString())
                      .append("; Booth: ").append(boothInfo.getId());
        if (!event.hasError())
        {
            Log.i(TAG, messageBuilder.toString());
        }
        else
        {
            messageBuilder.append("; Error code: ").append(event.getError().getErrorCode().toString())
                          .append("; Error message: ").append(event.getError().getErrorMessage());

            Log.e(TAG, messageBuilder.toString());
            if (mVrStationModelObserver != null)
            {
                mVrStationModelObserver.onError(event.getError().getErrorMessage());
            }
        }

        VrStation vrStation = ensureVrStationAdded(boothInfo.getId());
        vrStation.setVrGameStatus(event.getStatus());

        if (mVrStationModelObserver != null)
            mVrStationModelObserver.onVrStationUpdated();
    }

    @Override
    public synchronized void onPcOnlineStatusChanged(int boothId, OnlineStatusProtos.OnlineStatus status)
    {
        Log.i(TAG, "PC online status changed: " + status.toString() + boothId);
        if (status == OnlineStatusProtos.OnlineStatus.OFFLINE)
        {
            EventBus.getDefault().post(new PcOfflineEvent(boothId, false));
        }
        else if (status == OnlineStatusProtos.OnlineStatus.ONLINE)
        {
            EventBus.getDefault().post(new PcOnlineEvent(boothId));
        }

        VrStation vrStation = ensureVrStationAdded(boothId);
        vrStation.setPcOnlineStatus(status);

        if (mVrStationModelObserver != null)
            mVrStationModelObserver.onVrStationUpdated();
    }

    public synchronized VrStation ensureVrStationAdded(int boothId)
    {
        VrStation vrStation = findVrStation(boothId);
        if (vrStation == null)
        {
            vrStation = new VrStation(boothId, this);
            mVrStations.add(vrStation);
            vrStation.setEquipment("HTC Vive");
        }
        return vrStation;
    }

    public synchronized VrStation findVrStation(int boothId)
    {
        for (VrStation vrStation : mVrStations)
        {
            if (vrStation.getBoothId() == boothId)
            {
                return vrStation;
            }
        }
        return null;
    }

    public synchronized List<VrStation> getVrStationsList()
    {
        return mVrStations;
    }

    private List<VrStation> mVrStations = new ArrayList<>();
    private VrStationsModelObserver mVrStationModelObserver;

    private static final String TAG = VrStationsModel.class.getSimpleName();
    private static VrStationsModel mSelf;
}
