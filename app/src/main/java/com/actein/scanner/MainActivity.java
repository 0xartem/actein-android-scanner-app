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

import com.actein.scanner.Booth.VRBoothsInfo;
import com.actein.scanner.Utils.StringUtils;

import java.util.regex.Pattern;

public class MainActivity extends Activity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mServerAddressEditText = (EditText) findViewById(R.id.serverAddressEditText);
        mPhilipsHueBridgeAddressEditText = (EditText) findViewById(R.id.philipsHueBridgeAddressEditText);
        mBoothsSpinner = (Spinner) findViewById(R.id.boothsSpinner);
        mOkButton = (Button) findViewById(R.id.okButton);

        VRBoothsInfo vrBoothsInfo = new VRBoothsInfo(getResources().getText(R.string.vrBoothText));
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

                Intent intent = new Intent("com.google.zxing.client.android.SCAN");
                intent.putExtra("SCAN_MODE", "QR_CODE_MODE");
                startActivity(intent);
                //startActivityForResult(intent, 0);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (requestCode == 0)
        {
            if (resultCode == RESULT_OK)
            {
                String contents = getIntent().getStringExtra("SCAN_RESULT");
                String format = getIntent().getStringExtra("SCAN_RESULT_FORMAT");
            }
            else if (resultCode == RESULT_CANCELED)
            {
                ;
            }
        }
    }

    private EditText mServerAddressEditText;
    private EditText mPhilipsHueBridgeAddressEditText;
    private Button mOkButton;
    private Spinner mBoothsSpinner;
}
