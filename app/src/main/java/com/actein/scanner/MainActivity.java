package com.actein.scanner;

import android.app.Activity;
//import android.support.v7.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

import com.actein.scanner.Booth.VRBoothsInfo;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity { //AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Button okButton = (Button) findViewById(R.id.ok_button);
        final Spinner boothsSpinner = (Spinner) findViewById(R.id.booths_spinner);


        m_vrBoothsInfo = new VRBoothsInfo(getResources().getText(R.string.vr_booth_text));
        ArrayAdapter<CharSequence> spinnerAdapter = new ArrayAdapter<CharSequence>(this, android.R.layout.simple_spinner_item, m_vrBoothsInfo.getBoothsNames());
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        boothsSpinner.setAdapter(spinnerAdapter);

        okButton.setOnClickListener(new Button.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent("com.google.zxing.client.android.SCAN");
                intent.putExtra("SCAN_MODE", "QR_CODE_MODE");
                startActivityForResult(intent, 0);
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

    private VRBoothsInfo m_vrBoothsInfo;
}
