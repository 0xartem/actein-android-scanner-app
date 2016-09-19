package com.actein.scanner;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.actein.scanner.utils.Preferences;
import com.google.zxing.client.android.CaptureActivity;

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
            Intent captureIntent = new Intent(LaunchActivity.this, CaptureActivity.class);
            captureIntent.putExtra("SCAN_MODE", "QR_CODE_MODE");
            startActivity(captureIntent);
        }
    }
}
