package com.actein.mvp.model;

import android.util.Log;

import com.actein.scanner.R;
import com.actein.transport.mqtt.OnlineStatusProtos;
import com.actein.vr_events.VrBoothInfoProtos;
import com.actein.vr_events.VrGameErrorProtos;
import com.actein.vr_events.VrGameOffProtos;
import com.actein.vr_events.VrGameOnProtos;
import com.actein.vr_events.VrGameProtos;
import com.actein.vr_events.VrGameStatusProtos;

import org.greenrobot.eventbus.EventBus;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.junit.Assert.*;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest({Log.class, EventBus.class})
public class VrStationsModelTest
{
    @Mock
    VrStationsModelObserver mMockVrStationModelObserver;
    @Mock
    EventBus mMockEventBus;

    @Before
    public void setUp() throws Exception
    {
        PowerMockito.mockStatic(Log.class);
        PowerMockito.mockStatic(EventBus.class);
        when(EventBus.getDefault()).thenReturn(mMockEventBus);

        VrStationsModel.getInstance().setObserver(mMockVrStationModelObserver);
    }

    @Test
    public void handleVrGameOnEvent() throws Exception
    {
        VrBoothInfoProtos.VrBoothInfo vrBoothInfo =
                VrBoothInfoProtos.VrBoothInfo.newBuilder().setId(43).build();

        VrGameProtos.VrGame vrGame = VrGameProtos.VrGame.newBuilder()
                                                        .setGameName("Fruit Ninja")
                                                        .setSteamGameId(56)
                                                        .setGameDurationSeconds(300)
                                                        .setRunTutorial(true)
                                                        .build();

        VrGameOnProtos.VrGameOnEvent vrGameOnEvent =
                VrGameOnProtos.VrGameOnEvent.newBuilder()
                                            .setGame(vrGame)
                                            .setVrBoothInfo(vrBoothInfo)
                                            .build();

        VrStationsModel.getInstance().handleVrGameOnEvent(vrBoothInfo, vrGameOnEvent);

        VrStation vrStation = VrStationsModel.getInstance().findVrStation(43);

        assertEquals(43, vrStation.getBoothId());
        assertEquals("Fruit Ninja", vrStation.getExperience());
        assertEquals(300, vrStation.getTime());
        verify(mMockVrStationModelObserver).onVrStationUpdated();
    }

    @Test
    public void handleVrGameOffEvent() throws Exception
    {
        VrBoothInfoProtos.VrBoothInfo vrBoothInfo =
                VrBoothInfoProtos.VrBoothInfo.newBuilder().setId(22).build();

        VrGameOffProtos.VrGameOffEvent vrGameOffEvent =
                VrGameOffProtos.VrGameOffEvent.newBuilder().setVrBoothInfo(vrBoothInfo).build();

        VrStationsModel.getInstance().handleVrGameOffEvent(vrBoothInfo, vrGameOffEvent);

        VrStation vrStation = VrStationsModel.getInstance().findVrStation(22);

        assertEquals(22, vrStation.getBoothId());
        assertEquals("", vrStation.getExperience());
        assertEquals(0, vrStation.getTime());
        verify(mMockVrStationModelObserver).onVrStationUpdated();
    }

    @Test
    public void handleVrGameStatusEvent() throws Exception
    {
        VrBoothInfoProtos.VrBoothInfo vrBoothInfo =
                VrBoothInfoProtos.VrBoothInfo.newBuilder().setId(42).build();

        VrGameStatusProtos.VrGameStatusEvent vrGameStatusEvent =
                VrGameStatusProtos.VrGameStatusEvent.newBuilder()
                                                    .setStatus(VrGameStatusProtos.VrGameStatus.GAME_OFF)
                                                    .build();

        VrStationsModel.getInstance().handleVrGameStatusEvent(vrBoothInfo, vrGameStatusEvent);

        VrStation vrStation = VrStationsModel.getInstance().findVrStation(42);

        assertEquals(42, vrStation.getBoothId());
        assertEquals(R.drawable.start, vrStation.getRunningIcon());
        assertFalse(vrStation.isGameRunning());
        assertTrue(vrStation.isGameStopped());
        verify(mMockVrStationModelObserver).onVrStationUpdated();
    }

    @Test
    public void handleVrGameStatusEvent_Error() throws Exception
    {
        VrBoothInfoProtos.VrBoothInfo vrBoothInfo =
                VrBoothInfoProtos.VrBoothInfo.newBuilder().setId(11).build();

        VrGameErrorProtos.VrGameError error =
                VrGameErrorProtos.VrGameError.newBuilder()
                                             .setErrorCode(VrGameErrorProtos.VrGameErrorCode.CANNOT_START_STEAM_VR)
                                             .setErrorMessage("message")
                                             .build();

        VrGameStatusProtos.VrGameStatusEvent vrGameStatusEvent =
                VrGameStatusProtos.VrGameStatusEvent.newBuilder()
                                                    .setStatus(VrGameStatusProtos.VrGameStatus.GAME_OFF)
                                                    .setError(error)
                                                    .build();

        VrStationsModel.getInstance().handleVrGameStatusEvent(vrBoothInfo, vrGameStatusEvent);

        VrStation vrStation = VrStationsModel.getInstance().findVrStation(11);

        assertEquals(11, vrStation.getBoothId());
        assertEquals(R.drawable.start, vrStation.getRunningIcon());
        assertFalse(vrStation.isGameRunning());
        assertTrue(vrStation.isGameStopped());

        verify(mMockVrStationModelObserver).onError("message");
        verify(mMockVrStationModelObserver).onVrStationUpdated();
    }

    @Test
    public void onPcOnlineStatusChanged() throws Exception
    {
        VrStationsModel.getInstance().onPcOnlineStatusChanged(15,
                                                              OnlineStatusProtos.OnlineStatus.ONLINE);

        VrStation vrStation = VrStationsModel.getInstance().findVrStation(15);

        assertEquals(15, vrStation.getBoothId());
        assertEquals(R.drawable.monitor_blue, vrStation.getOnlineIcon());
    }

    @Test
    public void ensureVrStationAdded() throws Exception
    {
        VrStation vrStation = VrStationsModel.getInstance().findVrStation(99);
        assertEquals(null, vrStation);

        VrStationsModel.getInstance().ensureVrStationAdded(99);
        vrStation = VrStationsModel.getInstance().findVrStation(99);
        assertEquals(99, vrStation.getBoothId());
    }
}