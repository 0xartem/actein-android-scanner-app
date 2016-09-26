package com.actein.zxing.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class Preferences
{
    public static boolean appPreviouslyStarted(Context context)
    {
        SharedPreferences prefs = getSharedPrefs(context);
        boolean previouslyStarted = prefs.getBoolean(PREVIOUSLY_STARTED, false);
        if (!previouslyStarted)
        {
            prefs.edit().putBoolean(PREVIOUSLY_STARTED, true).apply();
        }
        return previouslyStarted;
    }

    public static void setServerUri(Context context, String serverUri)
    {
        getSharedPrefs(context)
                .edit()
                .putString(SERVER_URI, serverUri)
                .apply();
    }

    public static void setPhilipsHueUri(Context context, String philipsHueUri)
    {
        getSharedPrefs(context)
                .edit()
                .putString(PHILIPS_HUE_URI, philipsHueUri)
                .apply();
    }

    public static void setIsAdminUser(Context context, boolean value)
    {
        getSharedPrefs(context)
                .edit()
                .putBoolean(IS_ADMIN_USER, value)
                .apply();
    }

    public static boolean isAdminUser(Context context)
    {
        return getSharedPrefs(context).getBoolean(IS_ADMIN_USER, false);
    }

    public static void setAdminPwdHash(Context context, String hashBase64)
    {
        getSharedPrefs(context)
                .edit()
                .putString(ADMIN_PASSWORD_HASH, hashBase64)
                .apply();
    }

    public static String getAdminPwdHash(Context context)
    {
        return getSharedPrefs(context).getString(ADMIN_PASSWORD_HASH, "");
    }

    private static SharedPreferences getSharedPrefs(Context context)
    {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    private static final String PREVIOUSLY_STARTED = "previously_started";
    private static final String ADMIN_PASSWORD_HASH = "admin_password_hash";
    private static final String IS_ADMIN_USER = "is_admin_user";
    private static final String SERVER_URI = "server_uri";
    private static final String PHILIPS_HUE_URI = "philips_hue_uri";
}
