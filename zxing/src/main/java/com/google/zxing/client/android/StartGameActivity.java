package com.google.zxing.client.android;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.Spinner;

import com.actein.mvp.ContextOwner;
import com.actein.zxing.data.GamesInfo;

public class StartGameActivity extends Activity implements ContextOwner
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_game);

        final NumberPicker secondsPicker = (NumberPicker) findViewById(R.id.seconds_number_picker);
        secondsPicker.setMinValue(MIN_PICK_VALUE);
        secondsPicker.setMaxValue(SECS_IN_MIN);

        final NumberPicker minutesPicker = (NumberPicker) findViewById(R.id.minutes_number_picker);
        minutesPicker.setMinValue(MIN_PICK_VALUE);
        minutesPicker.setMaxValue(MINS_IN_HOUR);

        final NumberPicker hoursPicker = (NumberPicker) findViewById(R.id.hours_number_picker);
        hoursPicker.setMinValue(MIN_PICK_VALUE);
        hoursPicker.setMaxValue(HOURS_IN_DAY);

        final Spinner gamesListSpinner = (Spinner) findViewById(R.id.games_spinner);

        final GamesInfo gamesInfo = new GamesInfo(this);
        ArrayAdapter<CharSequence> spinnerAdapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item,
                gamesInfo.getGamesNames()
        );
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        gamesListSpinner.setAdapter(spinnerAdapter);

        Button startButton = (Button) findViewById(R.id.game_start_button);
        startButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                try
                {
                    String gameStr = gamesListSpinner.getSelectedItem().toString();
                    long steamGameId = gamesInfo.getSteamId(gameStr);
                    int secondsInHours = hoursPicker.getValue() * MINS_IN_HOUR * SECS_IN_MIN;
                    int secondsInMins = minutesPicker.getValue() * SECS_IN_MIN;
                    int seconds = secondsPicker.getValue();
                    long durationSeconds = secondsInHours + secondsInMins + seconds;

                    Intent resultIntent = new Intent();
                    resultIntent.putExtra(Intents.StartGame.GAME_NAME, gameStr);
                    resultIntent.putExtra(Intents.StartGame.GAME_STEAM_ID, steamGameId);
                    resultIntent.putExtra(Intents.StartGame.DURATION_SECONDS, durationSeconds);
                    setResult(RESULT_OK, resultIntent);
                    finish();
                }
                catch (Exception ex)
                {
                    Log.e(TAG, ex.getMessage(), ex);
                    new AlertDialog.Builder(getActivityContext())
                            .setTitle(getString(R.string.msg_error))
                            .setMessage(ex.getMessage())
                            .setPositiveButton(R.string.button_ok, null)
                            .show();
                }
            }
        });
    }

    @Override
    public Context getActivityContext()
    {
        return this;
    }

    @Override
    public Context getApplicationContext()
    {
        return super.getApplicationContext();
    }

    private static final int MIN_PICK_VALUE = 0;
    private static final int SECS_IN_MIN = 60;
    private static final int MINS_IN_HOUR = 60;
    private static final int HOURS_IN_DAY = 24;

    private static final String TAG = StartGameActivity.class.getSimpleName();
}
