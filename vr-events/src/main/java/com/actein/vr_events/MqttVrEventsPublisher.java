package com.actein.vr_events;

import com.actein.transport.mqtt.interfaces.ActionStatusObserver;
import com.actein.transport.mqtt.interfaces.Publisher;
import com.actein.transport.mqtt.actions.Action;
import com.actein.transport.mqtt.actions.CommonActionListener;
import com.actein.vr_events.interfaces.VrEventsException;
import com.actein.vr_events.interfaces.VrEventsPublisher;

import org.eclipse.paho.client.mqttv3.MqttException;

public class MqttVrEventsPublisher implements VrEventsPublisher
{
    public MqttVrEventsPublisher(Publisher publisher)
    {
        mPublisher = publisher;
    }

    @Override
    public void publishVrGameOnEvent(ActionStatusObserver actionStatusObserver)
            throws VrEventsException
    {
        try
        {
            VrGameOnProtos.VrGameOnEvent event = VrGameOnProtos.VrGameOnEvent.newBuilder().build();
            mPublisher.publish(
                    VrTopics.VR_PC_TURN_GAME_ON,
                    event,
                    new CommonActionListener(Action.PUBLISH, actionStatusObserver)
                    );
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
            VrGameOffProtos.VrGameOffEvent event = VrGameOffProtos.VrGameOffEvent.newBuilder().build();
            mPublisher.publish(
                    VrTopics.VR_PC_TURN_GAME_OFF,
                    event,
                    new CommonActionListener(Action.PUBLISH, actionStatusObserver)
                    );
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
            mPublisher.publish(
                    VrTopics.VR_PC_GAME_STATUS,
                    event,
                    new CommonActionListener(Action.PUBLISH, actionStatusObserver)
                    );
        }
        catch (MqttException ex)
        {
            throw new VrEventsException("Can not publish vr game status event", ex);
        }
    }

    private Publisher mPublisher;
}
