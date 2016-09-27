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
import com.actein.scanner.booth.VRBoothsInfo;
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
        mOkButton = (Button) findViewById(R.id.okButton);

        VRBoothsInfo vrBoothsInfo = new VRBoothsInfo(getString(R.string.vrBoothText));
        ArrayAdapter<CharSequence> spinnerAdapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item,
                vrBoothsInfo.getBoothsNames()
        );
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mBoothsSpinner.setAdapter(spinnerAdapter);

        mOkButton.setOnClickListener(new Button.OnClickListener() {
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

                    new SetupAsyncTask(SetupActivity.this)
                            .execute(new SetupParams(serverUri, philipsHueUri, password));
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
    private Button mOkButton;
    private Spinner mBoothsSpinner;
}
