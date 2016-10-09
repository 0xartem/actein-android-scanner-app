package com.actein.transport.mqtt.actions;

import android.util.Log;

import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;

import java.util.Arrays;

public class CommonLogActionListener implements IMqttActionListener
{
    @Override
    public void onSuccess(IMqttToken asyncActionToken)
    {
        try
        {
            String logMessage = "Action with message id: " +
                                asyncActionToken.getMessageId() +
                                " succeed." +
                                " Topics: " +
                                Arrays.toString(asyncActionToken.getTopics());

            Log.i(TAG, logMessage);
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
        catch (Exception ex)
        {
            Log.e(TAG, ex.getMessage(), ex);
        }
    }

    private static final String TAG = CommonLogActionListener.class.getSimpleName();
}
