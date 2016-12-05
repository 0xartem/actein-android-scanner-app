package com.actein.transport.mqtt.interfaces;

public interface ConnectionObserver
{
    void onConnectionLost();
    void onConnected();
    void onReconnected();
}
