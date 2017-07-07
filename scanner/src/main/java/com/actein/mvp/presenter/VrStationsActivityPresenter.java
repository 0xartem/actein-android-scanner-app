package com.actein.mvp.presenter;

import android.content.Intent;

import com.actein.adapter.VrStationsTableDataAdapter;
import com.actein.common.Intents;
import com.actein.event.ConnectionLostEvent;
import com.actein.event.ErrorEvent;
import com.actein.event.InfoEvent;
import com.actein.event.TurnGameOnEvent;
import com.actein.mvp.model.VrStation;
import com.actein.mvp.model.VrStationsModel;
import com.actein.mvp.model.VrStationsModelObserver;
import com.actein.mvp.view.VrStationsView;
import com.actein.scanner.R;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

public class VrStationsActivityPresenter implements VrStationsPresenter, VrStationsModelObserver
{
    public VrStationsActivityPresenter(VrStationsView vrStationsView)
    {
        mVrStationsView = vrStationsView;
    }

    @Override
    public synchronized VrStationsTableDataAdapter createDataAdapter()
    {
        mDataAdapter = new VrStationsTableDataAdapter(mVrStationsView,
                                                      mVrStationsView.getActivityContext(),
                                                      VrStationsModel.getInstance().getVrStationsList());
        return mDataAdapter;
    }

    @Override
    public void onGameStartActivityResult(Intent intent)
    {
        int boothId = intent.getIntExtra(Intents.StartGame.BOOTH_ID, 0);
        VrStation vrStation = VrStationsModel.getInstance().findVrStation(boothId);
        if (vrStation != null)
        {
            EventBus.getDefault()
                    .post(new TurnGameOnEvent(
                            vrStation,
                            intent.getStringExtra(Intents.StartGame.GAME_NAME),
                            intent.getLongExtra(Intents.StartGame.GAME_STEAM_ID, 0),
                            intent.getLongExtra(Intents.StartGame.DURATION_SECONDS, 0),
                            intent.getBooleanExtra(Intents.StartGame.RUN_TUTORIAL,true)
                            ));
        }
    }

    @Override
    public synchronized void onVrStationUpdated()
    {
        mDataAdapter.notifyDataSetChanged();
    }

    @Override
    public void onCreate()
    {
        VrStationsModel.getInstance().onCreate();
        VrStationsModel.getInstance().setObserver(this);
        mEventReceiver = new EventReceiver();
        EventBus.getDefault().register(mEventReceiver);
    }

    @Override
    public void onDestroy(boolean isChangingConfiguration)
    {
        EventBus.getDefault().unregister(mEventReceiver);
        VrStationsModel.getInstance().setObserver(null);
        VrStationsModel.getInstance().onDestroy(isChangingConfiguration);
    }

    @Override
    public void onError(String message)
    {
        mVrStationsView.showErrorDialog(message);
    }

    @Override
    public void onInfo(String message)
    {
        mVrStationsView.showInfoDialog(message);
    }

    private VrStationsTableDataAdapter mDataAdapter;
    private VrStationsView mVrStationsView;
    private EventReceiver mEventReceiver;

    private class EventReceiver
    {
        @Subscribe
        public void onConnecionLostEventReceived(ConnectionLostEvent event)
        {
            if (event.showErrorMessage)
            {
                onError(mVrStationsView.getActivityContext().getString(R.string.msg_connection_lost));
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
