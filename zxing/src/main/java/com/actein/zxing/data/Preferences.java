package com.actein.zxing.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class Preferences
{
    public static boolean appPreviouslyStarted(Context context)
    {
        return getSharedPrefs(context).contains(BROKER_URI);
    }

    public static void setBrokerUri(Context context, String brokerUri)
    {
        getSharedPrefs(context)
                .edit()
                .putString(BROKER_URI, brokerUri)
                .apply();
    }

    public static boolean containsBrokerUri(Context context)
    {
        return getSharedPrefs(context).contains(BROKER_URI);
    }

    public static String getBrokerUri(Context context)
    {
        if (!containsBrokerUri(context))
        {
            throw new AssertionError(BROKER_URI + " can not be empty");
        }
        return getSharedPrefs(context).getString(BROKER_URI, "");
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

    public static void setBoothId(Context context, int boothId)
    {
        getSharedPrefs(context).edit().putInt(BOOTH_ID, boothId).apply();
    }

    public static int getBoothId(Context context)
    {
        return getSharedPrefs(context).getInt(BOOTH_ID, 0);
    }

    private static SharedPreferences getSharedPrefs(Context context)
    {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    private static final String ADMIN_PASSWORD_HASH = "admin_password_hash";
    private static final String IS_ADMIN_USER = "is_admin_user";
    private static final String BROKER_URI = "broker_uri";
    private static final String PHILIPS_HUE_URI = "philips_hue_uri";
    private static final String BOOTH_ID = "booth_id";
}
