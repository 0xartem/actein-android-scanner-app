package com.actein.transport.mqtt.listeners;

import android.util.Log;

import com.actein.transport.BuildConfig;
import com.actein.transport.mqtt.interfaces.UINotifier;

import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;

import java.util.Arrays;

public class CommonActionListener implements IMqttActionListener
{
    public CommonActionListener(Action action, UINotifier uiNotifier)
    {
        mAction = action;
        mUiNotifier = uiNotifier;
    }

    @Override
    public void onSuccess(IMqttToken asyncActionToken)
    {
        try
        {
            String message = buildOnSuccessMessage(asyncActionToken);
            if (BuildConfig.DEBUG && mUiNotifier != null)
            {
                mUiNotifier.showToast(message);
            }
            Log.i(TAG, message);
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
            if (BuildConfig.DEBUG && mUiNotifier != null)
            {
                mUiNotifier.showToast(message);
            }
            Log.e(TAG, message, exception);
        }
        catch (Exception ex)
        {
            Log.e(TAG, ex.getMessage(), ex);
        }
    }

    private String buildOnSuccessMessage(IMqttToken asyncActionToken)
    {
        switch (mAction)
        {
        case CONNECT:
            return "MQTT connection succeed";
        case DISCONNECT:
            return "MQTT disconnection succeed";
        case SUBSCRIBE:
            return "Subscription succeed. " + Arrays.toString(asyncActionToken.getTopics());
        case UNSUBSCRIBE:
            return "Unsubscription succeed. " + Arrays.toString(asyncActionToken.getTopics());
        case PUBLISH:
            return "Publication succeed. " + Arrays.toString(asyncActionToken.getTopics());
        default:
            throw new UnsupportedOperationException("Unknown action type");
        }
    }

    private String buildOnFailureMessage(IMqttToken asyncActionToken, Throwable exception)
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
            messageBuilder.append("Subscription failed. ")
                          .append(Arrays.toString(asyncActionToken.getTopics()));
            break;
        case UNSUBSCRIBE:
            messageBuilder.append("Unsubscription failed. ")
                          .append(Arrays.toString(asyncActionToken.getTopics()));
            break;
        case PUBLISH:
            messageBuilder.append("Publication failed. ")
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
    private UINotifier mUiNotifier;
    private static final String TAG = CommonActionListener.class.getSimpleName();
}
