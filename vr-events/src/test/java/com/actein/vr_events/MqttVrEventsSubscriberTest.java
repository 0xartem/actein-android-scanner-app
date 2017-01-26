package com.actein.vr_events;

import android.util.Log;

import com.actein.transport.mqtt.actions.CommonActionListener;
import com.actein.transport.mqtt.interfaces.Subscriber;
import com.actein.vr_events.interfaces.VrEventsException;
import com.actein.vr_events.interfaces.VrEventsHandler;

import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.mockito.Mockito.*;

//@RunWith(MockitoJUnitRunner.class)
@RunWith(PowerMockRunner.class)
@PrepareForTest({Log.class})
public class MqttVrEventsSubscriberTest
{
    @Rule
    public final ExpectedException mThrown = ExpectedException.none();
    @Mock
    private Subscriber mMockSubscriber;
    @Mock
    private VrEventsHandler mMockVrEventsHandler;

    private MqttVrEventsSubscriber mMqttVrEventsSubscriber;

    @Before
    public void setUp() throws Exception
    {
        PowerMockito.mockStatic(Log.class);

        VrBoothInfoProtos.VrBoothInfo vrBoothInfo = VrBoothInfoProtos.VrBoothInfo
                .newBuilder()
                .setId(65)
                .build();

        mMqttVrEventsSubscriber = new MqttVrEventsSubscriber(mMockSubscriber,
                                                             vrBoothInfo,
                                                             mMockVrEventsHandler,
                                                             null);
    }

    @Test
    public void subscribeToAll() throws Exception
    {
        mMqttVrEventsSubscriber.subscribeToAll();
        verify(mMockSubscriber, times(1)).subscribe(eq("factory/booths/65/pc/vr/game/#"),
                                                    any(CommonActionListener.class));
    }

    @Test
    public void unsubscribeFromAll() throws Exception
    {
        mMqttVrEventsSubscriber.unsubscribeFromAll();
        verify(mMockSubscriber, times(1)).unsubscribe(eq("factory/booths/65/pc/vr/game/#"),
                                                      any(CommonActionListener.class));
    }

    @Test
    public void subscribeToStatusEvent() throws Exception
    {
        mMqttVrEventsSubscriber.subscribeToStatusEvent();
        verify(mMockSubscriber, times(1)).subscribe(eq("factory/booths/65/pc/vr/game/status"),
                                                      any(CommonActionListener.class));
    }

    @Test
    public void unsubscribeFromStatusEvent() throws Exception
    {
        mMqttVrEventsSubscriber.unsubscribeFromStatusEvent();
        verify(mMockSubscriber, times(1)).unsubscribe(eq("factory/booths/65/pc/vr/game/status"),
                                                      any(CommonActionListener.class));
    }

    @Test
    public void subscribeToAll_ThrowVrException() throws Exception
    {
        doThrow(new MqttException(MqttException.REASON_CODE_SUBSCRIBE_FAILED))
                .when(mMockSubscriber)
                .subscribe(anyString(), any(CommonActionListener.class));

        mThrown.expect(VrEventsException.class);
        mMqttVrEventsSubscriber.subscribeToAll();
    }

    @Test
    public void unsubscribeFromAll_ThrowVrException() throws Exception
    {
        doThrow(new MqttException(MqttException.REASON_CODE_SUBSCRIBE_FAILED))
                .when(mMockSubscriber)
                .unsubscribe(anyString(), any(CommonActionListener.class));

        mThrown.expect(VrEventsException.class);
        mMqttVrEventsSubscriber.unsubscribeFromAll();
    }

    @Test
    public void subscribeToStatusEvent_ThrowVrException() throws Exception
    {
        doThrow(new MqttException(MqttException.REASON_CODE_SUBSCRIBE_FAILED))
                .when(mMockSubscriber)
                .subscribe(anyString(), any(CommonActionListener.class));

        mThrown.expect(VrEventsException.class);
        mMqttVrEventsSubscriber.subscribeToStatusEvent();
    }

    @Test
    public void unsubscribeFromStatusEvent_ThrowVrException() throws Exception
    {
        doThrow(new MqttException(MqttException.REASON_CODE_SUBSCRIBE_FAILED))
                .when(mMockSubscriber)
                .unsubscribe(anyString(), any(CommonActionListener.class));

        mThrown.expect(VrEventsException.class);
        mMqttVrEventsSubscriber.unsubscribeFromStatusEvent();
    }

    @Test
    public void handleMessage_handleVrGameOnEvent() throws Exception
    {
        mMqttVrEventsSubscriber.handleMessage("factory/booths/65/pc/vr/game/on",
                                              new MqttMessage(VrGameOnProtos.VrGameOnEvent
                                                                      .newBuilder()
                                                                      .build()
                                                                      .toByteArray()));

        verify(mMockVrEventsHandler).handleVrGameOnEvent(any(VrGameOnProtos.VrGameOnEvent.class));
    }

    @Test
    public void handleMessage_handleVrGameOffEvent() throws Exception
    {
        mMqttVrEventsSubscriber.handleMessage("factory/booths/65/pc/vr/game/off",
                                              new MqttMessage(VrGameOffProtos.VrGameOffEvent
                                                                      .newBuilder()
                                                                      .build()
                                                                      .toByteArray()));

        verify(mMockVrEventsHandler).handleVrGameOffEvent(any(VrGameOffProtos.VrGameOffEvent.class));
    }

    @Test
    public void handleMessage_handleVrGameStatusEvent() throws Exception
    {
        mMqttVrEventsSubscriber.handleMessage("factory/booths/65/pc/vr/game/status",
                                              new MqttMessage(VrGameStatusProtos.VrGameStatusEvent
                                                                      .newBuilder()
                                                                      .build()
                                                                      .toByteArray()));

        verify(mMockVrEventsHandler).handleVrGameStatusEvent(any(VrGameStatusProtos.VrGameStatusEvent.class));
    }
}