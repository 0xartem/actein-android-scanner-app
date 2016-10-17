package com.actein.vr_events;

import com.actein.transport.mqtt.actions.ActionStatusObserver;
import com.actein.transport.mqtt.interfaces.Publisher;
import com.actein.transport.mqtt.actions.Action;
import com.actein.transport.mqtt.actions.CommonActionListener;
import com.actein.vr_events.interfaces.VrEventsException;
import com.actein.vr_events.interfaces.VrEventsPublisher;
import com.actein.vr_events.topics.VrTopicBuilder;

import org.eclipse.paho.client.mqttv3.MqttException;

class MqttVrEventsPublisher implements VrEventsPublisher
{
    MqttVrEventsPublisher(Publisher publisher, VrBoothInfoProtos.VrBoothInfo vrBoothInfo)
    {
        mPublisher = publisher;
        mVrBoothInfo = vrBoothInfo;
    }

    @Override
    public void publishVrGameOnEvent(VrGameProtos.VrGame vrGame,
                                     ActionStatusObserver actionStatusObserver)
            throws VrEventsException
    {
        try
        {
            VrGameOnProtos.VrGameOnEvent event = VrGameOnProtos.VrGameOnEvent
                    .newBuilder()
                    .setVrBoothInfo(mVrBoothInfo)
                    .setGame(vrGame)
                    .build();

            String topic = new VrTopicBuilder().setToGameOn()
                                               .setBoothId(mVrBoothInfo.getId())
                                               .build();
            mPublisher.publish(topic,
                               event,
                               new CommonActionListener(Action.PUBLISH, actionStatusObserver));
        }
        catch (MqttException ex)
        {
            throw new VrEventsException("Can not publish vr game turn off event", ex);
        }
    }

    @Override
    public void publishVrGameOffEvent(ActionStatusObserver actionStatusObserver)
            throws VrEventsException
    {
        try
        {
            VrGameOffProtos.VrGameOffEvent event = VrGameOffProtos.VrGameOffEvent
                    .newBuilder()
                    .setVrBoothInfo(mVrBoothInfo)
                    .build();

            String topic = new VrTopicBuilder().setToGameOff()
                                               .setBoothId(mVrBoothInfo.getId())
                                               .build();
            mPublisher.publish(topic,
                               event,
                               new CommonActionListener(Action.PUBLISH, actionStatusObserver));
        }
        catch (MqttException ex)
        {
            throw new VrEventsException("Can not publish vr game turn on event", ex);
        }
    }

    @Override
    public void publishVrGameStatusEvent(VrGameStatusProtos.VrGameStatus status,
                                         ActionStatusObserver actionStatusObserver)
            throws VrEventsException
    {
        try
        {
            VrGameStatusProtos.VrGameStatusEvent event = VrGameStatusProtos.VrGameStatusEvent
                    .newBuilder()
                    .setStatus(status)
                    .build();

            String topic = new VrTopicBuilder().setToGameStatus()
                                               .setBoothId(mVrBoothInfo.getId())
                                               .build();
            mPublisher.publish(topic,
                               event,
                               new CommonActionListener(Action.PUBLISH, actionStatusObserver));
        }
        catch (MqttException ex)
        {
            throw new VrEventsException("Can not publish vr game status event", ex);
        }
    }

    private Publisher mPublisher;
    private VrBoothInfoProtos.VrBoothInfo mVrBoothInfo;
}
