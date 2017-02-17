package com.actein.activity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.actein.scanner.R;
import com.actein.android.utils.StringUtils;

public class SetupActivity extends Activity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);

        mBrokerAddressEditText = (EditText) findViewById(R.id.brokerAddressEditText);
        mPhilipsHueBridgeAddressEditText = (EditText) findViewById(R.id.philipsHueBridgeAddressEditText);
        mAdminPasswordEditText = (EditText) findViewById(R.id.adminPasswordEditText);
        Button okButton = (Button) findViewById(R.id.okButton);

        okButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                try
                {
                    String brokerUri = mBrokerAddressEditText.getText().toString().trim();
                    if (!StringUtils.isNetworkAddressValid(brokerUri))
                    {
                        Toast.makeText(getApplicationContext(),
                                       R.string.toastIncorrectBrokerAddress,
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
                            .execute(new SetupParams(brokerUri, philipsHueUri, password));
                }
                catch (Exception ex)
                {
                    Log.e(TAG, ex.toString(), ex);
                    Toast.makeText(getApplicationContext(), ex.toString(), Toast.LENGTH_SHORT)
                         .show();
                }
            }
        });
    }

    private static final String TAG = SetupActivity.class.getSimpleName();

    private EditText mBrokerAddressEditText;
    private EditText mPhilipsHueBridgeAddressEditText;
    private EditText mAdminPasswordEditText;
}
