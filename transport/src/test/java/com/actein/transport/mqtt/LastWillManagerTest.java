package com.actein.transport.mqtt;

import com.actein.transport.mqtt.actions.Action;
import com.actein.transport.mqtt.actions.CommonActionListener;
import com.actein.transport.mqtt.interfaces.Publisher;
import com.actein.transport.mqtt.interfaces.Subscriber;

import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class LastWillManagerTest
{
    @Mock
    private Connection mMockConnection;
    @Mock
    private Publisher mMockPublisher;
    @Mock
    private Subscriber mMockSubscriber;

    @Test
    public void start() throws Exception
    {
        when(mMockConnection.getSubscriber()).thenReturn(mMockSubscriber);
        when(mMockConnection.getPublisher()).thenReturn(mMockPublisher);

        LastWillManager lastWillManager = new LastWillManager(mMockConnection, null, null, 25);

        lastWillManager.start();

        verify(mMockSubscriber, times(1)).subscribe(eq("factory/booths/25/pc/status"),
                                                    any(CommonActionListener.class));

        OnlineStatusProtos.OnlineStatusEvent event = OnlineStatusProtos.OnlineStatusEvent
                .newBuilder()
                .setStatus(OnlineStatusProtos.OnlineStatus.ONLINE)
                .build();

        verify(mMockPublisher, times(1)).publish(eq("factory/booths/25/embDevice/status"),
                                                 eq(event),
                                                 any(CommonActionListener.class));

    }

    @Test
    public void stop() throws Exception
    {
        when(mMockConnection.getSubscriber()).thenReturn(mMockSubscriber);
        when(mMockConnection.getPublisher()).thenReturn(mMockPublisher);

        LastWillManager lastWillManager = new LastWillManager(mMockConnection, null, null, 21);

        lastWillManager.stop();

        verify(mMockSubscriber, times(1)).unsubscribe(eq("factory/booths/21/pc/status"),
                                                      any(CommonActionListener.class));

        OnlineStatusProtos.OnlineStatusEvent event = OnlineStatusProtos.OnlineStatusEvent
                .newBuilder()
                .setStatus(OnlineStatusProtos.OnlineStatus.OFFLINE)
                .build();

        verify(mMockPublisher, times(1)).publish(eq("factory/booths/21/embDevice/status"),
                                                 eq(event),
                                                 any(CommonActionListener.class));
    }
}