package com.actein.zxing;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;

import com.actein.transport.mqtt.Connection;
import com.actein.vr_events.MqttVrEventsManager;
import com.actein.zxing.data.Preferences;
import com.google.zxing.client.android.R;
import com.google.zxing.client.android.result.ResultHandler;
import com.google.zxing.client.result.CalendarParsedResult;
import com.google.zxing.client.result.ParsedResult;
import com.google.zxing.client.result.ParsedResultType;

public class QrCodeValidationTask extends AsyncTask<Void, String, QrCodeStatus>
{

    public QrCodeValidationTask(
            Activity activity,
            QrCodeProcessingCallback callback,
            ResultHandler parsedResultHandler,
            Bitmap barCode
            )
    {
        mActivity = activity;
        mCallback = callback;
        mParsedResultHandler = parsedResultHandler;
        mBarCode = barCode;

        mQrCodeSettings = new QrCodeSettings(PreferenceManager.getDefaultSharedPreferences(activity));

        mProgressDialog = new ProgressDialog(activity);
        mProgressDialog.setMessage(activity.getString(R.string.progress_dlg_loading_msg));
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setCancelable(false);
    }

    @Override
    protected void onPreExecute()
    {
        mProgressDialog.show();
    }

    @Override
    protected void onProgressUpdate(String... progressMessages)
    {
        mProgressDialog.setMessage(progressMessages[0]);
    }

    @Override
    protected void onPostExecute(QrCodeStatus status)
    {
        if (mProgressDialog.isShowing())
        {
            mProgressDialog.dismiss();
        }

        mCallback.onQrCodeValidated(status, mParsedResultHandler, mBarCode);
    }

    @Override
    protected QrCodeStatus doInBackground(Void... params)
    {
        try
        {
            publishProgress(mActivity.getString(R.string.progress_dlg_qr_validation_msg));

            ParsedResult parsedResult = mParsedResultHandler.getResult();
            if (parsedResult.getType() != ParsedResultType.CALENDAR)
            {
                return QrCodeStatus.QR_CODE_INVALID;
            }

            QrCodeValidator qrCodeValidator = new QrCodeValidator((CalendarParsedResult) parsedResult,
                                                                  mQrCodeSettings);

            publishProgress(mActivity.getString(R.string.progress_dlg_turn_vr_on_msg));
            VrGameSwitcher switcher = new VrGameSwitcher(mActivity.getApplicationContext());
            try
            {
                switcher.turnGameOn();
            }
            finally
            {
                switcher.close();
            }
            if (switcher.getCloseException() != null)
            {
                throw switcher.getCloseException();
            }

            return qrCodeValidator.getQrCodeStatus();
        }
        catch (Exception ex)
        {
            Log.e(TAG, ex.getMessage(), ex);
        }
        return QrCodeStatus.QR_CODE_INVALID;
    }

    private Activity mActivity;
    private QrCodeProcessingCallback mCallback;
    private QrCodeSettings mQrCodeSettings;
    private ResultHandler mParsedResultHandler;
    private Bitmap mBarCode;
    private ProgressDialog mProgressDialog;

    private static final String TAG = QrCodeValidationTask.class.getSimpleName();
}
