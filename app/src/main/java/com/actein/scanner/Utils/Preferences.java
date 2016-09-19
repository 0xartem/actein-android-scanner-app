package com.actein.scanner.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class Preferences
{
    public static boolean previouslyStarted(Context context)
    {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        boolean previouslyStarted = preferences.getBoolean(PREVIOUSLY_STARTED, false);
        if (!previouslyStarted)
        {
            preferences.edit().putBoolean(PREVIOUSLY_STARTED, true).apply();
        }
        return previouslyStarted;
    }

    private static final String PREVIOUSLY_STARTED = "previously_started";
}
