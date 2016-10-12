package com.actein.scanner.activity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.actein.scanner.R;
import com.actein.scanner.booth.VrBoothSelector;
import com.actein.scanner.tasks.*;
import com.actein.scanner.utils.StringUtils;

public class SetupActivity extends Activity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);

        mServerAddressEditText = (EditText) findViewById(R.id.serverAddressEditText);
        mPhilipsHueBridgeAddressEditText = (EditText) findViewById(R.id.philipsHueBridgeAddressEditText);
        mAdminPasswordEditText = (EditText) findViewById(R.id.adminPasswordEditText);
        mBoothsSpinner = (Spinner) findViewById(R.id.boothsSpinner);
        Button okButton = (Button) findViewById(R.id.okButton);

        final VrBoothSelector vrBoothSelector = new VrBoothSelector(getString(R.string.vrBoothText));
        ArrayAdapter<CharSequence> spinnerAdapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item,
                vrBoothSelector.getBoothsNames()
        );
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mBoothsSpinner.setAdapter(spinnerAdapter);

        okButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                try
                {
                    String serverUri = mServerAddressEditText.getText().toString().trim();
                    if (!StringUtils.isNetworkAddressValid(serverUri))
                    {
                        Toast.makeText(getApplicationContext(),
                                       R.string.toastIncorrectServerAddress,
                                       Toast.LENGTH_SHORT).show();
                        return;
                    }

                    String philipsHueUri = mPhilipsHueBridgeAddressEditText.getText().toString().trim();
                    if (!StringUtils.isNetworkAddressValid(philipsHueUri))
                    {
                        Toast.makeText(getApplicationContext(),
                                       R.string.toastIncorrectPhilipsHueBridge,
                                       Toast.LENGTH_SHORT).show();
                        return;
                    }

                    String password = mAdminPasswordEditText.getText().toString();
                    if (password.isEmpty())
                    {
                        Toast.makeText(getApplicationContext(),
                                       R.string.toastEmptyAdminPassword,
                                       Toast.LENGTH_SHORT).show();
                        return;
                    }

                    String boothIdStr = mBoothsSpinner.getSelectedItem().toString();
                    int boothId = vrBoothSelector.getBoothId(boothIdStr);
                    if (boothId == 0)
                    {
                        Toast.makeText(getApplicationContext(),
                                       R.string.toastInvalidBoothId,
                                       Toast.LENGTH_LONG).show();
                        return;
                    }

                    new SetupAsyncTask(SetupActivity.this)
                            .execute(new SetupParams(serverUri, philipsHueUri, password, boothId));
                }
                catch (Exception ex)
                {
                    Log.e(TAG, ex.getMessage(), ex);
                }
            }
        });
    }

    private static final String TAG = SetupActivity.class.getSimpleName();

    private EditText mServerAddressEditText;
    private EditText mPhilipsHueBridgeAddressEditText;
    private EditText mAdminPasswordEditText;
    private Spinner mBoothsSpinner;
}
