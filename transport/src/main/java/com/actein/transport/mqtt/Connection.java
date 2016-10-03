package com.actein.transport.mqtt;

import android.content.Context;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;

public class Connection
{

    private Connection(
            Context context,
            String brokerHost,
            int port,
            boolean tlsConnection,
            String clientId
            )
    {
        mContext = context;
        mBrokerHost = brokerHost;
        mPort = port;
        mTlsConnection = tlsConnection;
        mClientId = clientId;

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
    }

    public static Connection createInstance(Context context, String brokerHost, int port)
    {
        return new Connection(context, brokerHost, port, false, MqttClient.generateClientId());
    }

    public static Connection createInstance(
            Context context,
            String brokerHost,
            int port,
            boolean tlsConnection,
            String clientId
            )
    {
        return new Connection(context, brokerHost, port, tlsConnection, clientId);
    }

    public void connect(IMqttActionListener listener) throws MqttException
    {
        mClient.connect(mConnectOptions, listener);
    }

    public void disconnect() throws MqttException
    {
        mClient.disconnect();
    }

    private Context mContext = null;
    private MqttAndroidClient mClient = null;
    private MqttConnectOptions mConnectOptions = null;
    private String mClientId = null;
    private String mBrokerHost = null;
    private int mPort = 0;
    private boolean mTlsConnection = false;

}
