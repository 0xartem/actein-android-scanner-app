package com.actein.transport.mqtt;

import com.actein.transport.mqtt.actions.CommonActionListener;
import com.actein.transport.mqtt.interfaces.Publisher;
import com.actein.transport.mqtt.interfaces.Subscriber;
import com.google.protobuf.MessageLite;

import org.eclipse.paho.client.mqttv3.MqttException;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class LastWillManagerTest
{
    @Rule
    public final ExpectedException mThrown = ExpectedException.none();

    @Mock
    private Connection mMockConnection;
    @Mock
    private Publisher mMockPublisher;
    @Mock
    private Subscriber mMockSubscriber;

    private LastWillManager mLastWillManager;

    @Before
    public void setUp() throws Exception
    {
        when(mMockConnection.getSubscriber()).thenReturn(mMockSubscriber);
        when(mMockConnection.getPublisher()).thenReturn(mMockPublisher);

        mLastWillManager = new LastWillManager(mMockConnection, null, "clientId");
    }

    @Test
    public void start() throws Exception
    {
        mLastWillManager.start();

        verify(mMockSubscriber, times(1)).subscribe(eq("factory/booths/+/pc/status"),
                                                    any(CommonActionListener.class));

        OnlineStatusProtos.OnlineStatusEvent event = OnlineStatusProtos.OnlineStatusEvent
                .newBuilder()
                .setStatus(OnlineStatusProtos.OnlineStatus.ONLINE)
                .build();

        verify(mMockPublisher, times(1)).publish(eq("factory/embDevice/clientId/status"),
                                                 eq(event),
                                                 any(CommonActionListener.class));

    }

    @Test
    public void start_Subscribe_ThrowMqttException() throws Exception
    {
        doThrow(new MqttException(MqttException.REASON_CODE_CONNECTION_LOST))
                .when(mMockSubscriber)
                .subscribe(anyString(), any(CommonActionListener.class));


        mThrown.expect(MqttException.class);
        mLastWillManager.start();
    }

    @Test
    public void start_Publish_ThrowMqttException() throws Exception
    {
        doThrow(new MqttException(MqttException.REASON_CODE_CONNECTION_LOST))
                .when(mMockPublisher)
                .publish(anyString(), any(MessageLite.class), any(CommonActionListener.class));


        mThrown.expect(MqttException.class);
        mLastWillManager.start();
    }

    @Test
    public void stop() throws Exception
    {
        mLastWillManager.stop();

        verify(mMockSubscriber, times(1)).unsubscribe(eq("factory/booths/+/pc/status"),
                                                      any(CommonActionListener.class));

        OnlineStatusProtos.OnlineStatusEvent event = OnlineStatusProtos.OnlineStatusEvent
                .newBuilder()
                .setStatus(OnlineStatusProtos.OnlineStatus.OFFLINE)
                .build();

        verify(mMockPublisher, times(1)).publish(eq("factory/embDevice/clientId/status"),
                                                 eq(event),
                                                 any(CommonActionListener.class));
    }

    @Test
    public void stop_Subscribe_ThrowMqttException() throws Exception
    {
        doThrow(new MqttException(MqttException.REASON_CODE_CONNECTION_LOST))
                .when(mMockSubscriber)
                .unsubscribe(anyString(), any(CommonActionListener.class));


        mThrown.expect(MqttException.class);
        mLastWillManager.stop();
    }

    @Test
    public void stop_Publish_ThrowMqttException() throws Exception
    {
        doThrow(new MqttException(MqttException.REASON_CODE_CONNECTION_LOST))
                .when(mMockPublisher)
                .publish(anyString(), any(MessageLite.class), any(CommonActionListener.class));


        mThrown.expect(MqttException.class);
        mLastWillManager.stop();
    }
}