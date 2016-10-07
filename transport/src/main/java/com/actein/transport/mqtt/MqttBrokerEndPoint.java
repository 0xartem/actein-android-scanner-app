package com.actein.transport.mqtt;

public class MqttBrokerEndPoint
{
    public MqttBrokerEndPoint(String brokerHost)
    {
        this(brokerHost, STANDARD_MQTT_PORT, false);
    }

    public MqttBrokerEndPoint(String brokerHost, int port, boolean tlsEndpoint)
    {
        mBrokerHost = brokerHost;
        mPort = port;
        mTlsEndpoint = tlsEndpoint;
    }

    public String getBrokerHost()
    {
        return mBrokerHost;
    }

    public int getPort()
    {
        return mPort;
    }

    public boolean isTlsEndPoint()
    {
        return mTlsEndpoint;
    }

    public String getEndpointUri()
    {
        if (mTlsEndpoint)
        {
            return "ssl://" + mBrokerHost + ":" + mPort;
        }
        return "tcp://" + mBrokerHost + ":" + mPort;
    }

    public static final int STANDARD_MQTT_PORT = 1883;
    public static final int STANDARD_MQTT_SSL_PORT = 8883;

    private String mBrokerHost;
    private boolean mTlsEndpoint;
    private int mPort = STANDARD_MQTT_PORT;
}
