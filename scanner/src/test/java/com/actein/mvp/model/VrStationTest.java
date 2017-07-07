package com.actein.mvp.model;

import com.actein.helpers.VrStationTimer;
import com.actein.scanner.R;
import com.actein.transport.mqtt.OnlineStatusProtos;
import com.actein.vr_events.VrGameStatusProtos;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.junit.Assert.*;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@RunWith(PowerMockRunner.class)
public class VrStationTest
{
    @Mock
    private VrStationTimer mMockVrStationTimer;

    @Test
    public void setVrGameStatus_STARTING_TUTORIAL() throws Exception
    {
        VrStation vrStation = new VrStation(22, mMockVrStationTimer);
        vrStation.setVrGameStatus(VrGameStatusProtos.VrGameStatus.STARTING_TUTORIAL);

        assertEquals(R.layout.start_game_progress_bar, vrStation.getRunningIcon());
        assertFalse(vrStation.isGameRunning());
        assertFalse(vrStation.isGameStopped());

        verify(mMockVrStationTimer, never()).start(vrStation);
        verify(mMockVrStationTimer, never()).stop();
    }

    @Test
    public void setVrGameStatus_TUTORIAL_ON() throws Exception
    {
        VrStation vrStation = new VrStation(11, mMockVrStationTimer);
        vrStation.setVrGameStatus(VrGameStatusProtos.VrGameStatus.TUTORIAL_ON);

        assertEquals(R.drawable.stop, vrStation.getRunningIcon());
        assertTrue(vrStation.isGameRunning());
        assertFalse(vrStation.isGameStopped());

        verify(mMockVrStationTimer).start(vrStation);
        verify(mMockVrStationTimer, never()).stop();
    }

    @Test
    public void setVrGameStatus_STARTING_GAME() throws Exception
    {
        VrStation vrStation = new VrStation(12, mMockVrStationTimer);
        vrStation.setVrGameStatus(VrGameStatusProtos.VrGameStatus.STARTING_GAME);

        assertEquals(R.layout.start_game_progress_bar, vrStation.getRunningIcon());
        assertFalse(vrStation.isGameRunning());
        assertFalse(vrStation.isGameStopped());

        verify(mMockVrStationTimer, never()).start(vrStation);
        verify(mMockVrStationTimer, never()).stop();
    }

    @Test
    public void setVrGameStatus_GAME_ON() throws Exception
    {
        VrStation vrStation = new VrStation(11, mMockVrStationTimer);
        vrStation.setVrGameStatus(VrGameStatusProtos.VrGameStatus.GAME_ON);

        assertEquals(R.drawable.stop, vrStation.getRunningIcon());
        assertTrue(vrStation.isGameRunning());
        assertFalse(vrStation.isGameStopped());

        verify(mMockVrStationTimer).start(vrStation);
        verify(mMockVrStationTimer, never()).stop();
    }

    @Test
    public void setVrGameStatus_STOPPING_GAME() throws Exception
    {
        VrStation vrStation = new VrStation(12, mMockVrStationTimer);
        vrStation.setVrGameStatus(VrGameStatusProtos.VrGameStatus.STOPPING_GAME);

        assertEquals(R.layout.start_game_progress_bar, vrStation.getRunningIcon());
        assertFalse(vrStation.isGameRunning());
        assertFalse(vrStation.isGameStopped());

        verify(mMockVrStationTimer, never()).start(vrStation);
        verify(mMockVrStationTimer, never()).stop();
    }

    @Test
    public void setVrGameStatus_GAME_OFF() throws Exception
    {
        VrStation vrStation = new VrStation(11, mMockVrStationTimer);

        vrStation.setExperience("Arizona Sunshine");
        vrStation.setTime(3600);
        assertEquals("Arizona Sunshine", vrStation.getExperience());
        assertEquals(3600, vrStation.getTime());

        vrStation.setVrGameStatus(VrGameStatusProtos.VrGameStatus.GAME_OFF);

        assertEquals("", vrStation.getExperience());
        assertEquals(0, vrStation.getTime());

        assertEquals(R.drawable.start, vrStation.getRunningIcon());
        assertFalse(vrStation.isGameRunning());
        assertTrue(vrStation.isGameStopped());

        verify(mMockVrStationTimer, never()).start(vrStation);
        verify(mMockVrStationTimer).stop();
    }

    @Test
    public void isOnline_UNKNOWN() throws Exception
    {
        VrStation vrStation = new VrStation(11, mMockVrStationTimer);
        assertFalse(vrStation.isOnline());
        assertEquals(R.drawable.monitor_grey, vrStation.getOnlineIcon());
    }

    @Test
    public void isOnline_ONLINE() throws Exception
    {
        VrStation vrStation = new VrStation(11, mMockVrStationTimer);
        vrStation.setPcOnlineStatus(OnlineStatusProtos.OnlineStatus.ONLINE);
        assertTrue(vrStation.isOnline());
        assertEquals(R.drawable.monitor_blue, vrStation.getOnlineIcon());
    }

    @Test
    public void isOnline_OFFLINE() throws Exception
    {
        VrStation vrStation = new VrStation(11, mMockVrStationTimer);
        vrStation.setPcOnlineStatus(OnlineStatusProtos.OnlineStatus.OFFLINE);
        assertFalse(vrStation.isOnline());
        assertEquals(R.drawable.monitor_grey, vrStation.getOnlineIcon());
    }

    @Test
    public void setBoothId_getBoothId() throws Exception
    {
        VrStation vrStation = new VrStation(11, mMockVrStationTimer);
        vrStation.setBoothId(33);
        assertEquals(33, vrStation.getBoothId());
    }

    @Test
    public void setEquipment_getEquipment() throws Exception
    {
        VrStation vrStation = new VrStation(11, mMockVrStationTimer);
        vrStation.setEquipment("HTC Vive");
        assertEquals("HTC Vive", vrStation.getEquipment());
    }

    @Test
    public void setExperience_getExperience() throws Exception
    {
        VrStation vrStation = new VrStation(11, mMockVrStationTimer);
        vrStation.setExperience("Arizona Sunshine");
        assertEquals("Arizona Sunshine", vrStation.getExperience());
    }

    @Test
    public void setTime_getTime_300() throws Exception
    {
        VrStation vrStation = new VrStation(11, mMockVrStationTimer);
        vrStation.setTime(300);
        assertEquals(300, vrStation.getTime());
        assertEquals("00:05:00", vrStation.getTimeStr());
    }

    @Test
    public void setTime_getTime_7200() throws Exception
    {
        VrStation vrStation = new VrStation(11, mMockVrStationTimer);
        vrStation.setTime(7200);
        assertEquals(7200, vrStation.getTime());
        assertEquals("02:00:00", vrStation.getTimeStr());
    }
}