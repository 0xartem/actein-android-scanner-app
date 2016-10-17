package com.actein.transport.mqtt.actions;

public interface ActionStatusObserver
{
    void onActionSuccess(Action action, String message);
    void onActionFailure(Action action, String message);
}
