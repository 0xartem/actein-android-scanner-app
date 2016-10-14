package com.actein.transport.mqtt.actions;

import android.util.Log;

import com.actein.transport.mqtt.interfaces.ActionStatusObserver;

import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;

import java.util.Arrays;

public class CommonActionListener implements IMqttActionListener
{
    public CommonActionListener(Action action, ActionStatusObserver actionStatusObserver)
    {
        mAction = action;
        mActionStatusObserver = actionStatusObserver;
    }

    @Override
    public void onSuccess(IMqttToken asyncActionToken)
    {
        try
        {
            String message = buildOnSuccessMessage(asyncActionToken);
            Log.i(TAG, message);
            if (mActionStatusObserver != null)
            {
                mActionStatusObserver.onActionSuccess(mAction, message);
            }
        }
        catch (Exception ex)
        {
            Log.e(TAG, ex.getMessage(), ex);
        }
    }

    @Override
    public void onFailure(IMqttToken asyncActionToken, Throwable exception)
    {
        try
        {
            String message = buildOnFailureMessage(asyncActionToken, exception);
            Log.e(TAG, message, exception);
            if (mActionStatusObserver != null)
            {
                mActionStatusObserver.onActionFailure(mAction, message);
            }
        }
        catch (Exception ex)
        {
            Log.e(TAG, ex.getMessage(), ex);
        }
    }

    protected String buildOnSuccessMessage(IMqttToken asyncActionToken)
    {
        switch (mAction)
        {
        case CONNECT:
            return "MQTT connection succeed";
        case DISCONNECT:
            return "MQTT disconnection succeed";
        case SUBSCRIBE:
            return "Subscription succeed. Topics: " + Arrays.toString(asyncActionToken.getTopics());
        case UNSUBSCRIBE:
            return "Unsubscription succeed. Topics: " + Arrays.toString(asyncActionToken.getTopics());
        case PUBLISH:
            return "Publication succeed. Topics: " + Arrays.toString(asyncActionToken.getTopics());
        default:
            throw new UnsupportedOperationException("Unknown action type");
        }
    }

    protected String buildOnFailureMessage(IMqttToken asyncActionToken, Throwable exception)
    {
        StringBuilder messageBuilder = new StringBuilder();
        switch (mAction)
        {
        case CONNECT:
            messageBuilder.append("MQTT connection failed");
            break;
        case DISCONNECT:
            messageBuilder.append("MQTT disconnection failed");
            break;
        case SUBSCRIBE:
            messageBuilder.append("Subscription failed. Topics: ")
                          .append(Arrays.toString(asyncActionToken.getTopics()));
            break;
        case UNSUBSCRIBE:
            messageBuilder.append("Unsubscription failed. Topics: ")
                          .append(Arrays.toString(asyncActionToken.getTopics()));
            break;
        case PUBLISH:
            messageBuilder.append("Publication failed. Topics: ")
                          .append(Arrays.toString(asyncActionToken.getTopics()));
            break;
        default:
            throw new UnsupportedOperationException("Unknown action type");
        }

        if (exception != null)
        {
            messageBuilder.append(". Error: ").append(exception.getMessage());
        }

        return messageBuilder.toString();
    }

    private Action mAction;
    private ActionStatusObserver mActionStatusObserver;
    private static final String TAG = CommonActionListener.class.getSimpleName();
}
