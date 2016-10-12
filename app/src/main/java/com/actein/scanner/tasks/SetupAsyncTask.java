package com.actein.scanner.tasks;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import com.actein.scanner.R;
import com.actein.zxing.data.Preferences;
import com.actein.zxing.security.HashAlgorithm;
import com.google.zxing.integration.android.IntentIntegrator;

public class SetupAsyncTask extends AsyncTask<SetupParams, Void, Intent>
{


    public SetupAsyncTask(Activity activity)
    {
        mActivity = activity;
        mProgressDialog = new ProgressDialog(activity);
        mProgressDialog.setMessage(activity.getString(R.string.progressDialogMessage));
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setCancelable(false);
    }

    @Override
    protected void onPreExecute()
    {
        mProgressDialog.show();
    }

    @Override
    protected void onPostExecute(Intent scanIntent)
    {
        if (mProgressDialog.isShowing())
        {
            mProgressDialog.dismiss();
        }
        mActivity.startActivity(scanIntent);
    }

    @Override
    protected Intent doInBackground(SetupParams... params)
    {
        try
        {
            SetupParams setupParams = params[0];

            Preferences.setServerUri(mActivity, setupParams.getServerUri());
            Preferences.setPhilipsHueUri(mActivity, setupParams.getPhilipsHueUri());

            HashAlgorithm hashAlgorithm = new HashAlgorithm();
            Preferences.setAdminPwdHash(
                    mActivity,
                    // use trim() as workaround for android Issue 159799
                    hashAlgorithm.hashStrToBase64(setupParams.getPassword()).trim()
                    );
            Preferences.setIsAdminUser(mActivity, true);
            Preferences.setBoothId(mActivity, setupParams.getBoothId());

            IntentIntegrator intentIntegrator = new IntentIntegrator(mActivity);
            intentIntegrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
            intentIntegrator.setPrompt(mActivity.getString(R.string.qrCodeScanPrompt));
            return intentIntegrator.createScanIntent();
        }
        catch (Exception ex)
        {
            Log.e(TAG, ex.getMessage(), ex);
        }
        return null;
    }

    private Activity mActivity;
    private ProgressDialog mProgressDialog;
    private static final String TAG = SetupAsyncTask.class.getSimpleName();
}
