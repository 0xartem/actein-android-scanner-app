package com.actein.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.actein.data.Preferences;
import com.actein.service.ConnectionModelService;
import com.google.zxing.integration.android.IntentIntegrator;

public class LaunchActivity extends Activity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        if (!Preferences.appPreviouslyStarted(LaunchActivity.this))
        {
            startActivity(new Intent(LaunchActivity.this, SetupActivity.class));
        }
        else
        {
            Intent serviceIntent = new Intent(this, ConnectionModelService.class);
            startService(serviceIntent);

            IntentIntegrator intentIntegrator = new IntentIntegrator(LaunchActivity.this);
            intentIntegrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
            Intent scanIntent = intentIntegrator.createScanIntent();
            startActivity(scanIntent);
        }
    }
}
