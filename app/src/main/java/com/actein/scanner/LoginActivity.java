package com.actein.scanner;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.actein.scanner.booth.VRBoothsInfo;
import com.actein.scanner.utils.StringUtils;
import com.google.zxing.client.android.CaptureActivity;
import com.google.zxing.integration.android.IntentIntegrator;

public class LoginActivity extends Activity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mServerAddressEditText = (EditText) findViewById(R.id.serverAddressEditText);
        mPhilipsHueBridgeAddressEditText = (EditText) findViewById(R.id.philipsHueBridgeAddressEditText);
        mBoothsSpinner = (Spinner) findViewById(R.id.boothsSpinner);
        mOkButton = (Button) findViewById(R.id.okButton);

        VRBoothsInfo vrBoothsInfo = new VRBoothsInfo(getString(R.string.vrBoothText));
        ArrayAdapter<CharSequence> spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, vrBoothsInfo.getBoothsNames());
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mBoothsSpinner.setAdapter(spinnerAdapter);

        mOkButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                if (!StringUtils.isNetworkAddressValid(mServerAddressEditText.getText().toString().trim()))
                {
                    Toast.makeText(getApplicationContext(), R.string.toastEmptyServerAddress, Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!StringUtils.isNetworkAddressValid(mPhilipsHueBridgeAddressEditText.getText().toString()))
                {
                    Toast.makeText(getApplicationContext(), R.string.toastEmptyPhilipsHueBridge, Toast.LENGTH_SHORT).show();
                    return;
                }

                IntentIntegrator intentIntegrator = new IntentIntegrator(LoginActivity.this);
                intentIntegrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
                intentIntegrator.setPrompt(getResources().getText(R.string.qrCodeScanPrompt).toString());
                Intent scanIntent = intentIntegrator.createScanIntent();
                startActivity(scanIntent);
            }
        });
    }

    private EditText mServerAddressEditText;
    private EditText mPhilipsHueBridgeAddressEditText;
    private Button mOkButton;
    private Spinner mBoothsSpinner;
}
