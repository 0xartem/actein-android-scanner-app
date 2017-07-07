package com.actein.vr_events;

import android.util.Log;

import com.actein.transport.mqtt.actions.Action;
import com.actein.transport.mqtt.actions.ActionStatusObserver;
import com.actein.transport.mqtt.actions.CommonActionListener;
import com.actein.transport.mqtt.interfaces.Publisher;
import com.actein.vr_events.interfaces.VrEventsException;
import com.actein.vr_events.interfaces.VrEventsPublisher;
import com.actein.vr_events.topics.VrTopicBuilder;

import org.eclipse.paho.client.mqttv3.MqttException;

class MqttVrEventsPublisher implements VrEventsPublisher
{
    MqttVrEventsPublisher(Publisher publisher,
                          ActionStatusObserver actionObserver)
    {
        mPublisher = publisher;
        mPublishListener = new CommonActionListener(Action.PUBLISH, actionObserver);
    }

    @Override
    public void publishVrGameOnEvent(VrBoothInfoProtos.VrBoothInfo vrBoothInfo,
                                     VrGameProtos.VrGame vrGame) throws VrEventsException
    {
        try
        {
            VrGameOnProtos.VrGameOnEvent event = VrGameOnProtos.VrGameOnEvent
                    .newBuilder()
                    .setVrBoothInfo(vrBoothInfo)
                    .setGame(vrGame)
                    .build();

            String topic = new VrTopicBuilder().setToGameOn()
                                               .setBoothId(vrBoothInfo.getId())
                                               .build();

            mPublisher.publish(topic, event, mPublishListener, false);
        }
        catch (MqttException ex)
        {
            Log.e(TAG, ex.toString(), ex);
            throw new VrEventsException("Can not publish vr game turn off event", ex);
        }
    }

    @Override
    public void publishVrGameOffEvent(VrBoothInfoProtos.VrBoothInfo vrBoothInfo) throws VrEventsException
    {
        try
        {
            VrGameOffProtos.VrGameOffEvent event = VrGameOffProtos.VrGameOffEvent
                    .newBuilder()
                    .setVrBoothInfo(vrBoothInfo)
                    .build();

            String topic = new VrTopicBuilder().setToGameOff()
                                               .setBoothId(vrBoothInfo.getId())
                                               .build();

            mPublisher.publish(topic, event, mPublishListener, false);
        }
        catch (MqttException ex)
        {
            Log.e(TAG, ex.toString(), ex);
            throw new VrEventsException("Can not publish vr game turn on event", ex);
        }
    }

    @Override
    public void publishVrGameStatusEvent(VrBoothInfoProtos.VrBoothInfo vrBoothInfo,
                                         VrGameStatusProtos.VrGameStatus status) throws VrEventsException
    {
        publishVrGameStatusEvent(vrBoothInfo, status, null);
    }

    @Override
    public void publishVrGameStatusEvent(VrBoothInfoProtos.VrBoothInfo vrBoothInfo,
                                         VrGameStatusProtos.VrGameStatus status,
                                         VrGameErrorProtos.VrGameError error) throws VrEventsException
    {
        try
        {
            VrGameStatusProtos.VrGameStatusEvent.Builder builder =
                    VrGameStatusProtos.VrGameStatusEvent.newBuilder();

            builder.setStatus(status);
            if (error != null)
            {
                builder.setError(error);
            }

            String topic = new VrTopicBuilder().setToGameStatus()
                                               .setBoothId(vrBoothInfo.getId())
                                               .build();

            mPublisher.publish(topic, builder.build(), mPublishListener);
        }
        catch (MqttException ex)
        {
            Log.e(TAG, ex.toString(), ex);
            throw new VrEventsException("Can not publish vr game status event", ex);
        }
    }

    private Publisher mPublisher;
    private CommonActionListener mPublishListener;

    private static String TAG = MqttVrEventsPublisher.class.getSimpleName();
}
