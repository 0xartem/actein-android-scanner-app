package com.actein.transport.mqtt.interfaces;

public interface UINotifier
{
    void showToast(String message);
    void showToast(String message, int duration);
    void onConnectionLost();
}
