package com.actein.zxing.qr;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;

import com.actein.utils.DateTimeUtils;
import com.actein.zxing.model.BoothSettings;
import com.actein.zxing.model.EquipmentType;
import com.actein.zxing.presenter.CaptureActivityPresenter;
import com.google.zxing.client.android.R;
import com.google.zxing.client.android.result.ResultHandler;
import com.google.zxing.client.result.ActeinCalendarParsedResult;
import com.google.zxing.client.result.ParsedResultType;

import java.util.Date;
import java.util.concurrent.TimeUnit;

public class QrCodeProcessingTask extends AsyncTask<Void, String, QrCodeProcessingResult>
{

    public QrCodeProcessingTask(
            Context context,
            QrCodeProcessingCallback callback,
            ResultHandler parsedResultHandler,
            Bitmap barCode,
            CaptureActivityPresenter captureActivityPresenter,
            BoothSettings boothSettings
            )
    {
        mContext = context;
        mCallback = callback;
        mParsedResultHandler = parsedResultHandler;
        mBarCode = barCode;
        mCaptureActivityPresenter = captureActivityPresenter;
        mBoothSettings = boothSettings;

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
    protected void onPostExecute(QrCodeProcessingResult result)
    {
        if (mProgressDialog.isShowing())
        {
            mProgressDialog.dismiss();
        }

        mCallback.onQrCodeValidated(result, mParsedResultHandler, mBarCode);
    }

    @Override
    protected QrCodeProcessingResult doInBackground(Void... params)
    {
        try
        {
            publishProgress(mContext.getString(R.string.progress_dlg_qr_validation_msg));

            ActeinCalendarParsedResult result = (ActeinCalendarParsedResult) mParsedResultHandler.getResult();
            if (result.getType() != ParsedResultType.ACTEIN_CALENDAR)
            {
                return new QrCodeProcessingResult(QrCodeStatus.QR_CODE_INVALID);
            }

            QrCodeValidator qrCodeValidator = new QrCodeValidator(
                    mContext,
                    result,
                    mQrCodeSettings,
                    mBoothSettings
            );

            QrCodeStatus status = qrCodeValidator.validateQrCode();
            if (QrCodeStatus.isSuccess(status))
            {
                EquipmentType equipmentType = EquipmentType.convertToEquipmentType(result.getEquipment());
                if (equipmentType == EquipmentType.HTC_VIVE ||
                    equipmentType == EquipmentType.HTC_VIVE_WITH_SUBPACK)
                {
                    publishProgress(mContext.getString(R.string.progress_dlg_turn_vr_on_msg));

                    mCaptureActivityPresenter.turnGameOn(result.getGameName(),
                                                         result.getSteamGameId(),
                                                         this.adjustGameDuration(result),
                                                         true);
                }
            }

            return new QrCodeProcessingResult(status, result);
        }
        catch (Exception ex)
        {
            Log.e(TAG, ex.toString(), ex);
        }
        return new QrCodeProcessingResult(QrCodeStatus.QR_CODE_INVALID);
    }

    private long adjustGameDuration(ActeinCalendarParsedResult result)
    {
        long threeMinutes = 3 * 60; // time to grab equipment
        long durationSeconds = result.getDurationSeconds() + threeMinutes;
        if (!mQrCodeSettings.isAllowEarlyQrCodes() && !mQrCodeSettings.isAllowExpiredQrCodes())
        {
            Date now = new Date();
            if (now.after(result.getInnerCalendarResult().getStart()))
            {
                long personLateFor = DateTimeUtils.getDateDifference(
                        result.getInnerCalendarResult().getStart(),
                        now,
                        TimeUnit.SECONDS
                        );
                durationSeconds = durationSeconds - personLateFor;
            }
        }
        return durationSeconds;
    }

    private Context mContext;
    private QrCodeProcessingCallback mCallback;
    private QrCodeSettings mQrCodeSettings;
    private ResultHandler mParsedResultHandler;
    private Bitmap mBarCode;
    private ProgressDialog mProgressDialog;

    private CaptureActivityPresenter mCaptureActivityPresenter;
    private BoothSettings mBoothSettings;

    private static final String TAG = QrCodeProcessingTask.class.getSimpleName();
}
