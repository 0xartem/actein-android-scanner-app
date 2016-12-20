package com.actein.zxing.model;

import com.actein.vr_events.VrGameStatusProtos;

public interface ConnectionModelObserver
{
    void onConnected(String message);
    void onDisconnected(String message);
    void onSubscribed(String message);
    void onUnsubscribed(String message);
    void onPublished(String message);

    void onPcOnline();
    void onPcOffline(boolean sendingRequest);
    void onConnectionLost(boolean showErrorMsg);
    void onError(String message);
    void onInfo(String message);

    void onVrEventStatusReceived(VrGameStatusProtos.VrGameStatus status, String message);
}
