package com.actein.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import com.actein.android.utils.Base64Utils;
import com.actein.scanner.R;
import com.actein.data.Preferences;
import com.actein.utils.security.HashAlgorithm;
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

            Preferences.setBrokerUri(mActivity, setupParams.getBrokerUri());
            Preferences.setPhilipsHueUri(mActivity, setupParams.getPhilipsHueUri());

            HashAlgorithm hashAlgorithm = new HashAlgorithm();
            Preferences.setAdminPwdHash(
                    mActivity,
                    // use trim() as workaround for android Issue 159799
                    Base64Utils.hashStringToBase64(setupParams.getPassword(), hashAlgorithm).trim()
                    );
            Preferences.setIsAdminUser(mActivity, true);
            Preferences.setBoothId(mActivity, setupParams.getBoothId());

            IntentIntegrator intentIntegrator = new IntentIntegrator(mActivity);
            intentIntegrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
            return intentIntegrator.createScanIntent();
        }
        catch (Exception ex)
        {
            Log.e(TAG, ex.toString(), ex);
        }
        return null;
    }

    private Activity mActivity;
    private ProgressDialog mProgressDialog;
    private static final String TAG = SetupAsyncTask.class.getSimpleName();
}
