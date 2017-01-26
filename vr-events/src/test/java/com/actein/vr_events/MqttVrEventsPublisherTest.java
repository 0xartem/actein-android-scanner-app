package com.actein.vr_events;

import android.util.Log;

import com.actein.transport.mqtt.interfaces.Publisher;
import com.actein.transport.mqtt.interfaces.Subscriber;
import com.actein.vr_events.interfaces.VrEventsException;
import com.google.protobuf.MessageLite;

import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(PowerMockRunner.class)
@PrepareForTest({Log.class})
public class MqttVrEventsPublisherTest
{
    @Rule
    public final ExpectedException mThrown = ExpectedException.none();
    @Mock
    private Publisher mMockPublisher;

    private MqttVrEventsPublisher mMqttVrEventsPublisher;

    @Before
    public void setUp() throws Exception
    {
        PowerMockito.mockStatic(Log.class);

        VrBoothInfoProtos.VrBoothInfo vrBoothInfo = VrBoothInfoProtos.VrBoothInfo
                .newBuilder()
                .setId(10)
                .build();

        mMqttVrEventsPublisher = new MqttVrEventsPublisher(mMockPublisher, vrBoothInfo, null);
    }

    @Test
    public void publishVrGameOnEvent() throws Exception
    {
        mMqttVrEventsPublisher.publishVrGameOnEvent(VrGameProtos.VrGame.getDefaultInstance());
        verify(mMockPublisher).publish(eq("factory/booths/10/pc/vr/game/on"),
                                       any(VrGameOnProtos.VrGameOnEvent.class),
                                       any(IMqttActionListener.class),
                                       eq(false));
    }

    @Test
    public void publishVrGameOffEvent() throws Exception
    {
        mMqttVrEventsPublisher.publishVrGameOffEvent();
        verify(mMockPublisher).publish(eq("factory/booths/10/pc/vr/game/off"),
                                       any(VrGameOffProtos.VrGameOffEvent.class),
                                       any(IMqttActionListener.class),
                                       eq(false));
    }

    @Test
    public void publishVrGameStatusEvent() throws Exception
    {
        mMqttVrEventsPublisher.publishVrGameStatusEvent(VrGameStatusProtos.VrGameStatus.GAME_ON);

        ArgumentCaptor<VrGameStatusProtos.VrGameStatusEvent> argument =
                ArgumentCaptor.forClass(VrGameStatusProtos.VrGameStatusEvent.class);

        verify(mMockPublisher).publish(eq("factory/booths/10/pc/vr/game/status"),
                                       argument.capture(),
                                       any(IMqttActionListener.class));

        assertEquals(argument.getValue().getStatus(), VrGameStatusProtos.VrGameStatus.GAME_ON);
        assertFalse(argument.getValue().hasError());
    }

    @Test
    public void publishVrGameStatusEvent_Error() throws Exception
    {
        mMqttVrEventsPublisher.publishVrGameStatusEvent(
                VrGameStatusProtos.VrGameStatus.GAME_OFF,
                VrGameErrorProtos.VrGameError
                        .newBuilder()
                        .setErrorCode(VrGameErrorProtos.VrGameErrorCode.CANNOT_START_STEAM_VR).build()
                        );

        ArgumentCaptor<VrGameStatusProtos.VrGameStatusEvent> argument =
                ArgumentCaptor.forClass(VrGameStatusProtos.VrGameStatusEvent.class);

        verify(mMockPublisher).publish(eq("factory/booths/10/pc/vr/game/status"),
                                       argument.capture(),
                                       any(IMqttActionListener.class));

        assertEquals(argument.getValue().getStatus(), VrGameStatusProtos.VrGameStatus.GAME_OFF);
        assertTrue(argument.getValue().hasError());
        assertEquals(argument.getValue().getError().getErrorCode(),
                     VrGameErrorProtos.VrGameErrorCode.CANNOT_START_STEAM_VR);
    }

    @Test
    public void publishVrGameOnEvent_ThrowVrException() throws Exception
    {
        doThrow(new MqttException(MqttException.REASON_CODE_CLIENT_EXCEPTION))
                .when(mMockPublisher)
                .publish(anyString(),
                         any(MessageLite.class),
                         any(IMqttActionListener.class),
                         eq(false));

        mThrown.expect(VrEventsException.class);
        mMqttVrEventsPublisher.publishVrGameOnEvent(VrGameProtos.VrGame.getDefaultInstance());
    }

    @Test
    public void publishVrGameOffEvent_ThrowVrException() throws Exception
    {
        doThrow(new MqttException(MqttException.REASON_CODE_CLIENT_EXCEPTION))
                .when(mMockPublisher)
                .publish(anyString(),
                         any(MessageLite.class),
                         any(IMqttActionListener.class),
                         eq(false));

        mThrown.expect(VrEventsException.class);
        mMqttVrEventsPublisher.publishVrGameOffEvent();
    }

    @Test
    public void publishVrGameStatusEvent_ThrowVrException() throws Exception
    {
        doThrow(new MqttException(MqttException.REASON_CODE_CLIENT_EXCEPTION))
                .when(mMockPublisher)
                .publish(anyString(),
                         any(MessageLite.class),
                         any(IMqttActionListener.class));

        mThrown.expect(VrEventsException.class);
        mMqttVrEventsPublisher.publishVrGameStatusEvent(VrGameStatusProtos.VrGameStatus.GAME_ON);
    }

}