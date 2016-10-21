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
        if (mBoothId == -1)
            mBoothId = Preferences.getBoothId(mContext);
        return mBoothId;
    }

    private Context mContext;
    private int mBoothId = -1;
}
