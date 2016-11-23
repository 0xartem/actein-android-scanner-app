package com.actein.transport.mqtt;

import android.content.Context;

import com.actein.transport.mqtt.interfaces.Publisher;
import com.actein.transport.mqtt.interfaces.Subscriber;
import com.actein.transport.mqtt.policies.ConnectionPolicy;
import com.actein.transport.mqtt.policies.PreciseDeliveryConnectionPolicy;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttAsyncClient;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;

public class Connection
{
    private Connection(
            Context context,
            MqttBrokerEndPoint brokerEndPoint,
            MqttClientEndPoint clientEndPoint,
            ConnectionPolicy connectionPolicy
            )
    {
        mBrokerEndPoint = brokerEndPoint;
        mClientEndPoint = clientEndPoint;
        mConnectionPolicy = connectionPolicy;

        mClient = new MqttAndroidClient(
                context,
                mBrokerEndPoint.getEndPointUri(),
                mClientEndPoint.getClientId()
        );

        mSubscriber = new MqttSubscriber(mClient, mConnectionPolicy);
        mPublisher = new MqttPublisher(mClient, mConnectionPolicy);
    }

    public static Connection createInstance(
            Context context,
            MqttBrokerEndPoint brokerEndPoint,
            MqttClientEndPoint clientEndPoint,
            ConnectionPolicy connectionPolicy
            )
    {
        return new Connection(context, brokerEndPoint, clientEndPoint, connectionPolicy);
    }

    public static Connection createInstance(Context context, String brokerHost)
    {
        return new Connection(
                context,
                new MqttBrokerEndPoint(brokerHost),
                new MqttClientEndPoint(),
                new PreciseDeliveryConnectionPolicy()
        );
    }

    public void connect(IMqttActionListener listener) throws MqttException
    {
        MqttConnectOptions options = ConnectOptionsBuilder.buildConnectOptions(mConnectionPolicy);
        IMqttToken token = mClient.connect(options);
        token.setActionCallback(listener);
    }

    public void disconnect(IMqttActionListener actionListener) throws MqttException
    {
        IMqttToken token = mClient.disconnect();
        token.setActionCallback(actionListener);
    }

    public IMqttAsyncClient getClient()
    {
        return mClient;
    }

    public Publisher getPublisher()
    {
        return mPublisher;
    }

    public Subscriber getSubscriber()
    {
        return mSubscriber;
    }

    public MqttBrokerEndPoint getBrokerEndPoint()
    {
        return mBrokerEndPoint;
    }

    public MqttClientEndPoint getClientEndPoint()
    {
        return mClientEndPoint;
    }

    private MqttBrokerEndPoint mBrokerEndPoint;
    private MqttClientEndPoint mClientEndPoint;
    private MqttAndroidClient mClient;
    private MqttSubscriber mSubscriber;
    private MqttPublisher mPublisher;
    private ConnectionPolicy mConnectionPolicy;
}
