package com.actein.mvp.model;

import android.util.Log;

import com.actein.transport.mqtt.Connection;
import com.actein.transport.mqtt.MqttSubscriberCallback;
import com.actein.transport.mqtt.OnlineStatusProtos;
import com.actein.transport.mqtt.actions.Action;
import com.actein.transport.mqtt.interfaces.Publisher;
import com.actein.transport.mqtt.interfaces.Subscriber;
import com.actein.vr_events.VrGameOffProtos;
import com.actein.vr_events.VrGameOnProtos;
import com.google.protobuf.MessageLite;

import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttAsyncClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import static org.powermock.api.mockito.PowerMockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest({Log.class})
public class ConnectionModelTest
{
    @Mock
    private Connection mMockConnection;
    @Mock
    private IMqttAsyncClient mMockMqttClient;
    @Mock
    private Subscriber mMockSubscriber;
    @Mock
    private Publisher mMockPublisher;
    @Mock
    private ConnectionModelObserver mMockModelObserver;

    private ConnectionModel mConnectionModel;

    @Before
    public void setUp() throws Exception
    {
        PowerMockito.mockStatic(Log.class);

        when(mMockConnection.getSubscriber()).thenReturn(mMockSubscriber);
        when(mMockConnection.getPublisher()).thenReturn(mMockPublisher);
        when(mMockConnection.getClient()).thenReturn(mMockMqttClient);

        mConnectionModel = ConnectionModel.createInstance(mMockConnection,
                                                          mMockModelObserver,
                                                          "clientId");
    }

    @After
    public void tearDown() throws Exception
    {

    }

    @Test
    public void isConnected_True() throws Exception
    {
        when(mMockMqttClient.isConnected()).thenReturn(true);
        assertTrue(mConnectionModel.isConnected());
    }

    @Test
    public void isConnected_False() throws Exception
    {
        when(mMockMqttClient.isConnected()).thenReturn(false);
        assertFalse(mConnectionModel.isConnected());
    }

    @Test
    public void publishGameOffEvent() throws Exception
    {
        mConnectionModel.publishGameOffEvent(34);

        ArgumentCaptor<VrGameOffProtos.VrGameOffEvent> argument =
                ArgumentCaptor.forClass(VrGameOffProtos.VrGameOffEvent.class);

        verify(mMockPublisher).publish(eq("factory/booths/34/pc/vr/game/off"),
                                       argument.capture(),
                                       any(IMqttActionListener.class),
                                       eq(false));

        assertEquals(argument.getValue().getVrBoothInfo().getId(), 34);
    }

    @Test
    public void publishGameOffEvent_OnError() throws Exception
    {
        doThrow(new MqttException(MqttException.REASON_CODE_CLIENT_EXCEPTION))
                .when(mMockPublisher)
                .publish(anyString(),
                         any(MessageLite.class),
                         any(IMqttActionListener.class),
                         eq(false));

        mConnectionModel.publishGameOffEvent(34);
        verify(mMockModelObserver).onError(anyString());
    }

    @Test
    public void publishGameOnEvent() throws Exception
    {
        mConnectionModel.publishGameOnEvent(34, "Far Cry", 234, 60000, true);

        ArgumentCaptor<VrGameOnProtos.VrGameOnEvent> argument =
                ArgumentCaptor.forClass(VrGameOnProtos.VrGameOnEvent.class);

        verify(mMockPublisher).publish(eq("factory/booths/34/pc/vr/game/on"),
                                       argument.capture(),
                                       any(IMqttActionListener.class),
                                       eq(false));

        assertEquals(argument.getValue().getGame().getGameName(), "Far Cry");
        assertEquals(argument.getValue().getGame().getSteamGameId(), 234);
        assertEquals(argument.getValue().getGame().getGameDurationSeconds(), 60000);
        assertTrue(argument.getValue().getGame().getRunTutorial());
        assertEquals(argument.getValue().getVrBoothInfo().getId(), 34);
    }

    @Test
    public void publishGameOnEvent_OnError() throws Exception
    {
        doThrow(new MqttException(MqttException.REASON_CODE_CLIENT_EXCEPTION))
                .when(mMockPublisher)
                .publish(anyString(),
                         any(MessageLite.class),
                         any(IMqttActionListener.class),
                         eq(false));

        mConnectionModel.publishGameOnEvent(34, "Far Cry", 234, 60000, true);
        verify(mMockModelObserver).onError(anyString());
    }

    @Test
    public void onCreate() throws Exception
    {
        mConnectionModel.onCreate();

        verify(mMockSubscriber).setupCallback(any(MqttSubscriberCallback.class));
        verify(mMockConnection).connect(any(IMqttActionListener.class));
    }

    @Test
    public void onDestroy_AfterConnected() throws Exception
    {
        when(mMockMqttClient.isConnected()).thenReturn(true);
        mConnectionModel.onConnected();

        mConnectionModel.onDestroy(false);

        verify(mMockSubscriber).unsubscribe(eq("factory/booths/+/pc/status"),
                                            any(IMqttActionListener.class));

        ArgumentCaptor<OnlineStatusProtos.OnlineStatusEvent> argument =
                ArgumentCaptor.forClass(OnlineStatusProtos.OnlineStatusEvent.class);

        verify(mMockPublisher, times(2)).publish(eq("factory/embDevice/clientId/status"),
                                                 argument.capture(),
                                                 any(IMqttActionListener.class));

        assertEquals(argument.getValue().getStatus(), OnlineStatusProtos.OnlineStatus.OFFLINE);

        verify(mMockSubscriber).unsubscribe(eq("factory/booths/+/pc/vr/game/status"),
                                            any(IMqttActionListener.class));

        verify(mMockConnection).disconnect(any(IMqttActionListener.class));
    }

    @Test
    public void onDestroy_NotConnected() throws Exception
    {
        when(mMockMqttClient.isConnected()).thenReturn(false);

        mConnectionModel.onDestroy(false);

        verify(mMockSubscriber, never()).unsubscribe(eq("factory/booths/+/pc/status"),
                                                     any(IMqttActionListener.class));

        verify(mMockPublisher, never()).publish(eq("factory/embDevice/clientId/status"),
                                                any(OnlineStatusProtos.OnlineStatusEvent.class),
                                                any(IMqttActionListener.class));

        verify(mMockSubscriber, never()).unsubscribe(eq("factory/booths/+/pc/vr/game/status"),
                                                     any(IMqttActionListener.class));

        verify(mMockConnection, never()).disconnect(any(IMqttActionListener.class));
    }

    @Test
    public void onDestroy_Connected_ChangingConfig() throws Exception
    {
        when(mMockMqttClient.isConnected()).thenReturn(true);
        mConnectionModel.onConnected();

        mConnectionModel.onDestroy(true);

        verify(mMockSubscriber, never()).unsubscribe(eq("factory/booths/+/pc/status"),
                                                     any(IMqttActionListener.class));

        verify(mMockSubscriber, never()).unsubscribe(eq("factory/booths/+/pc/vr/game/status"),
                                                     any(IMqttActionListener.class));

        verify(mMockConnection, never()).disconnect(any(IMqttActionListener.class));
    }

    @Test
    public void onConnectionLost() throws Exception
    {
        mConnectionModel.onConnectionLost();
        verify(mMockModelObserver).onConnectionLost(false);
    }

    @Test
    public void onConnected() throws Exception
    {
        mConnectionModel.onConnected();

        ArgumentCaptor<OnlineStatusProtos.OnlineStatusEvent> argument =
                ArgumentCaptor.forClass(OnlineStatusProtos.OnlineStatusEvent.class);

        verify(mMockSubscriber).subscribe(eq("factory/booths/+/pc/status"),
                                          any(IMqttActionListener.class));

        verify(mMockPublisher).publish(eq("factory/embDevice/clientId/status"),
                                       argument.capture(),
                                       any(IMqttActionListener.class));
        assertEquals(argument.getValue().getStatus(), OnlineStatusProtos.OnlineStatus.ONLINE);

        verify(mMockSubscriber).subscribe(eq("factory/booths/+/pc/vr/game/status"),
                                          any(IMqttActionListener.class));
    }

    @Test
    public void onReconnected() throws Exception
    {
        mConnectionModel.onReconnected();

        ArgumentCaptor<OnlineStatusProtos.OnlineStatusEvent> argument =
                ArgumentCaptor.forClass(OnlineStatusProtos.OnlineStatusEvent.class);

        verify(mMockSubscriber).subscribe(eq("factory/booths/+/pc/status"),
                                          any(IMqttActionListener.class));

        verify(mMockPublisher).publish(eq("factory/embDevice/clientId/status"),
                                       argument.capture(),
                                       any(IMqttActionListener.class));
        assertEquals(argument.getValue().getStatus(), OnlineStatusProtos.OnlineStatus.ONLINE);

        verify(mMockSubscriber).subscribe(eq("factory/booths/+/pc/vr/game/status"),
                                          any(IMqttActionListener.class));

        verify(mMockModelObserver).onConnected("MQTT reconnection succeed");
    }

    @Test
    public void onActionSuccess_CONNECT() throws Exception
    {
        mConnectionModel.onActionSuccess(Action.CONNECT, "CONNECT");
        verify(mMockModelObserver).onConnected("CONNECT");
    }

    @Test
    public void onActionSuccess_DISCONNECT() throws Exception
    {
        mConnectionModel.onActionSuccess(Action.DISCONNECT, "DISCONNECT");
        verify(mMockModelObserver).onDisconnected("DISCONNECT");
    }

    @Test
    public void onActionSuccess_UNSUBSCRIBE() throws Exception
    {
        mConnectionModel.onActionSuccess(Action.UNSUBSCRIBE, "UNSUBSCRIBE");
        verify(mMockModelObserver).onUnsubscribed("UNSUBSCRIBE");
    }

    @Test
    public void onActionSuccess_SUBSCRIBE() throws Exception
    {
        mConnectionModel.onActionSuccess(Action.SUBSCRIBE, "SUBSCRIBE");
        verify(mMockModelObserver).onSubscribed("SUBSCRIBE");
    }

    @Test
    public void onActionSuccess_PUBLISH() throws Exception
    {
        mConnectionModel.onActionSuccess(Action.PUBLISH, "PUBLISH");
        verify(mMockModelObserver).onPublished("PUBLISH");
    }

    @Test
    public void onActionFailure_CONNECT() throws Exception
    {
        mConnectionModel.onActionFailure(Action.CONNECT, "CONNECT");
        verify(mMockModelObserver).onConnectionLost(false);
    }

    @Test
    public void onActionFailure_DISCONNECT() throws Exception
    {
        mConnectionModel.onActionFailure(Action.DISCONNECT, "DISCONNECT");
        verify(mMockModelObserver).onError("DISCONNECT");
    }

    @Test
    public void onActionFailure_UNSUBSCRIBE() throws Exception
    {
        mConnectionModel.onActionFailure(Action.UNSUBSCRIBE, "UNSUBSCRIBE");
        verify(mMockModelObserver).onError("UNSUBSCRIBE");
    }

    @Test
    public void onActionFailure_SUBSCRIBE() throws Exception
    {
        mConnectionModel.onActionFailure(Action.SUBSCRIBE, "SUBSCRIBE");
        verify(mMockModelObserver).onError("SUBSCRIBE");
    }

    @Test
    public void onActionFailure_PUBLISH() throws Exception
    {
        mConnectionModel.onActionFailure(Action.PUBLISH, "PUBLISH");
        verify(mMockModelObserver).onError("PUBLISH");
    }
}