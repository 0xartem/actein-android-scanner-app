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
        mMqttPublisher = new MqttPublisher(mClient);
        mMqttSubscriber = new MqttSubscriber(mClient);
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

    public void connect() throws MqttException
    {
        IMqttToken token = mClient.connect(mConnectOptions);
        token.setActionCallback(new IMqttActionListener()
        {
            @Override
            public void onSuccess(IMqttToken asyncActionToken)
            {
                Toast.makeText(mContext, "Connect success", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(IMqttToken asyncActionToken, Throwable exception)
            {
                Toast.makeText(mContext, "Connect failure", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void disconnect() throws MqttException
    {
        IMqttToken token = mClient.disconnect();
        token.setActionCallback(new IMqttActionListener()
        {
            @Override
            public void onSuccess(IMqttToken asyncActionToken)
            {
                Toast.makeText(mContext, "Disconnect success", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(IMqttToken asyncActionToken, Throwable exception)
            {
                Toast.makeText(mContext, "Disconnect failure", Toast.LENGTH_LONG).show();
            }
        });
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
