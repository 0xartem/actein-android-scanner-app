package com.google.zxing.client.android;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.view.Surface;

class OrientationManager
{
    OrientationManager(Activity activity)
    {
        mActivity = activity;
    }

    void setOrientation(boolean disableAutoOrientation)
    {
        if (disableAutoOrientation)
        {
            //noinspection ResourceType
            mActivity.setRequestedOrientation(getCurrentOrientation());
        }
    }

    private int getCurrentOrientation()
    {
        int rotation = mActivity.getWindowManager().getDefaultDisplay().getRotation();
        if (mActivity.getResources().getConfiguration().orientation ==
            Configuration.ORIENTATION_LANDSCAPE)
        {
            switch (rotation)
            {
            case Surface.ROTATION_0:
            case Surface.ROTATION_90:
                return ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
            default:
                return ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE;
            }
        }
        else
        {
            switch (rotation)
            {
            case Surface.ROTATION_0:
            case Surface.ROTATION_270:
                return ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
            default:
                return ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT;
            }
        }
    }

    private Activity mActivity;
}
