package com.actein.transport.mqtt.interfaces;

import com.actein.transport.mqtt.actions.Action;

public interface ActionStatusObserver
{
    void onActionSuccess(Action action, String message);
    void onActionFailure(Action action, String message);
}
