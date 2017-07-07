package com.actein.mvp.model;

public interface ConnectionModelObserver extends CommonObserver
{
    void onConnected(String message);
    void onDisconnected(String message);
    void onSubscribed(String message);
    void onUnsubscribed(String message);
    void onPublished(String message);

    void onConnectionLost(boolean showErrorMsg);
}
