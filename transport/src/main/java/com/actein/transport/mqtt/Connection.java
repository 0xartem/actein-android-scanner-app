package com.actein.transport.mqtt;

import android.content.Context;
import android.widget.Toast;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;

public class Connection
{
    private Connection(
            Context context,
            String brokerHost,
            int port,
            String clientId,
            boolean tlsConnection
            )
    {
        mContext = context;
        mBrokerHost = brokerHost;
        mPort = port;
        mClientId = clientId;
        mTlsConnection = tlsConnection;

        String uri;
        if (tlsConnection)
        {
            uri = "ssl://" + brokerHost + ":" + port;
        }
        else
        {
            uri = "tcp://" + brokerHost + ":" + port;
        }

        mClient = new MqttAndroidClient(context, uri, clientId);
        mConnectOptions = new MqttConnectOptions();
        mMqttPublisher = new MqttPublisher(mClient);
        mMqttSubscriber = new MqttSubscriber(mClient);
    }

    public static Connection createInstance(Context context, String brokerHost, int port)
    {
        return new Connection(context, brokerHost, port, MqttClient.generateClientId(), false);
    }

    public static Connection createInstance(
            Context context,
            String brokerHost,
            int port,
            String clientId,
            boolean tlsConnection
            )
    {
        return new Connection(context, brokerHost, port, clientId, tlsConnection);
    }

    public void connect() throws MqttException
    {
        IMqttToken token = mClient.connect(mConnectOptions);
        token.setActionCallback(null);
    }

    public void disconnect(IMqttActionListener actionListener) throws MqttException
    {
        IMqttToken token = mClient.disconnect();
        token.setActionCallback(actionListener);
    }

    public MqttPublisher getPublisher()
    {
        return mMqttPublisher;
    }

    public MqttSubscriber getSubscriber()
    {
        return mMqttSubscriber;
    }

    private Context mContext = null;
    private MqttAndroidClient mClient = null;
    private MqttConnectOptions mConnectOptions = null;
    private String mClientId = null;
    private String mBrokerHost = null;
    private int mPort = 0;
    private boolean mTlsConnection = false;

    private MqttPublisher mMqttPublisher;
    private MqttSubscriber mMqttSubscriber;

}
