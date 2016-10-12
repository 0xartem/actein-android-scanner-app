package com.actein.zxing.qr;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;

import com.actein.vr_events.VrGameProtos;
import com.actein.zxing.model.ConnectionModel;
import com.google.zxing.client.android.R;
import com.google.zxing.client.android.result.ResultHandler;
import com.google.zxing.client.result.ActeinCalendarParsedResult;
import com.google.zxing.client.result.ParsedResultType;

public class QrCodeProcessingTask extends AsyncTask<Void, String, QrCodeStatus>
{

    public QrCodeProcessingTask(
            Context context,
            QrCodeProcessingCallback callback,
            ResultHandler parsedResultHandler,
            Bitmap barCode,
            ConnectionModel connectionModel
            )
    {
        mContext = context;
        mCallback = callback;
        mParsedResultHandler = parsedResultHandler;
        mBarCode = barCode;
        mConnectionModel = connectionModel;

        mQrCodeSettings = new QrCodeSettings(PreferenceManager.getDefaultSharedPreferences(context));

        mProgressDialog = new ProgressDialog(context);
        mProgressDialog.setMessage(context.getString(R.string.progress_dlg_loading_msg));
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
            publishProgress(mContext.getString(R.string.progress_dlg_qr_validation_msg));

            ActeinCalendarParsedResult result = (ActeinCalendarParsedResult) mParsedResultHandler.getResult();
            if (result.getType() != ParsedResultType.ACTEIN_CALENDAR)
            {
                return QrCodeStatus.QR_CODE_INVALID;
            }

            QrCodeValidator qrCodeValidator = new QrCodeValidator(
                    result,
                    mQrCodeSettings,
                    mConnectionModel.getBoothSettings()
            );

            QrCodeStatus status = qrCodeValidator.validateQrCode();
            if (status == QrCodeStatus.SUCCESS)
            {
                publishProgress(mContext.getString(R.string.progress_dlg_turn_vr_on_msg));

                VrGameProtos.VrGame vrGame = VrGameProtos.VrGame
                        .newBuilder()
                        .setGameName(result.getGame())
                        .setGameDurationSeconds(result.getDurationSeconds())
                        .build();

                mConnectionModel.getVrEventsManager()
                                .getPublisher()
                                .publishVrGameOnEvent(vrGame, mConnectionModel);
            }

            return status;
        }
        catch (Exception ex)
        {
            Log.e(TAG, ex.getMessage(), ex);
        }
        return QrCodeStatus.QR_CODE_INVALID;
    }

    private Context mContext;
    private QrCodeProcessingCallback mCallback;
    private QrCodeSettings mQrCodeSettings;
    private ResultHandler mParsedResultHandler;
    private Bitmap mBarCode;
    private ProgressDialog mProgressDialog;

    private ConnectionModel mConnectionModel;

    private static final String TAG = QrCodeProcessingTask.class.getSimpleName();
}
