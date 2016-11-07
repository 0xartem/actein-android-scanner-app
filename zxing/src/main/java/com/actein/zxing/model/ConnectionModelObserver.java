package com.actein.zxing.model;

public interface ConnectionModelObserver
{
    void onConnected(String message);
    void onDisconnected(String message);
    void onSubscribed(String message);
    void onUnsubscribed(String message);
    void onPublished(String message);

    void onConnectionLost();
    void onError(String message);

    void onVrEventReceived(String message);
}
