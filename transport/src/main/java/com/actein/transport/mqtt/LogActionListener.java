package com.actein.transport.mqtt;

import android.util.Log;

import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;

import java.util.Arrays;

public class LogActionListener implements IMqttActionListener
{
    public void onSuccess(IMqttToken asyncActionToken)
    {
        String logMessage = "Action with message id: " +
                            asyncActionToken.getMessageId() +
                            " succeed." +
                            " Topics: " +
                            Arrays.toString(asyncActionToken.getTopics());

        Log.i(TAG, logMessage);
    }

    public void onFailure(IMqttToken asyncActionToken, Throwable exception)
    {
        StringBuilder logMessage = new StringBuilder();
        logMessage.append("Action with message id: ")
                  .append(asyncActionToken.getMessageId())
                  .append(" failed.")
                  .append(" Topics: ")
                  .append(Arrays.toString(asyncActionToken.getTopics()));

        if (exception != null)
        {
            logMessage.append("Error: ").append(exception.getMessage());
        }
        Log.e(TAG, logMessage.toString(), exception);
    }

    private static String TAG = LogActionListener.class.getSimpleName();
}
