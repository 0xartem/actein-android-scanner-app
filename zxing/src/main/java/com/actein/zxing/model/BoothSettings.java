package com.actein.zxing.model;

import android.content.Context;

import com.actein.zxing.data.Preferences;

public class BoothSettings
{
    public BoothSettings(Context context)
    {
        mContext = context;
    }

    public int getBoothId()
    {
        return Preferences.getBoothId(mContext);
    }

    private Context mContext;
}
