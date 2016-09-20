package com.actein.scanner;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.actein.scanner.utils.Preferences;
import com.google.zxing.client.android.CaptureActivity;
import com.google.zxing.integration.android.IntentIntegrator;

public class LaunchActivity extends Activity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        if (!Preferences.previouslyStarted(getBaseContext()))
        {
            startActivity(new Intent(LaunchActivity.this, LoginActivity.class));
        }
        else
        {
            IntentIntegrator intentIntegrator = new IntentIntegrator(LaunchActivity.this);
            intentIntegrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
            intentIntegrator.setPrompt(getResources().getText(R.string.qrCodeScanPrompt).toString());
            Intent scanIntent = intentIntegrator.createScanIntent();
            startActivity(scanIntent);
        }
    }
}
