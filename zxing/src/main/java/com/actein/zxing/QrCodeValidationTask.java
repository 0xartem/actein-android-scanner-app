package com.actein.zxing;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;

import com.google.zxing.client.android.R;
import com.google.zxing.client.android.result.ResultHandler;
import com.google.zxing.client.result.CalendarParsedResult;
import com.google.zxing.client.result.ParsedResult;
import com.google.zxing.client.result.ParsedResultType;

public class QrCodeValidationTask extends AsyncTask<Void, Void, QrCodeStatus> {

    public QrCodeValidationTask(
            Context context,
            QrCodeProcessingCallback callback,
            ResultHandler parsedResultHandler,
            Bitmap barCode) {
        mCallback = callback;
        mParsedResultHandler = parsedResultHandler;
        mBarCode = barCode;

        mProgressDialog = new ProgressDialog(context);
        mProgressDialog.setMessage(context.getString(R.string.progress_dialog_message));
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setCancelable(false);
    }

    @Override
    protected void onPreExecute() {
        mProgressDialog.show();
    }

    @Override
    protected void onPostExecute(QrCodeStatus status) {

        if (mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }

        mCallback.onQrCodeValidated(status, mParsedResultHandler, mBarCode);
    }

    @Override
    protected QrCodeStatus doInBackground(Void... params) {
        try {
            ParsedResult parsedResult = mParsedResultHandler.getResult();
            if (parsedResult.getType() != ParsedResultType.CALENDAR) {
                return QrCodeStatus.QR_CODE_INVALID;
            }

            QrCodeValidator qrCodeValidator = new QrCodeValidator(
                    (CalendarParsedResult) parsedResult,
                    new QrCodeSettings(false, false)
            );

            return qrCodeValidator.getQrCodeStatus();
        } catch (Exception ex) {
            Log.e(TAG, ex.getMessage());
        }
        return QrCodeStatus.QR_CODE_INVALID;
    }

    private QrCodeProcessingCallback mCallback;
    private ResultHandler mParsedResultHandler;
    private Bitmap mBarCode;
    private ProgressDialog mProgressDialog;

    private static final String TAG = QrCodeValidationTask.class.getSimpleName();
}
