package com.actein.zxing;

import android.content.SharedPreferences;

import com.google.zxing.client.android.PreferencesActivity;

public class QrCodeSettings {

    public QrCodeSettings(SharedPreferences prefs) {
        mAllowEarlyQrCodes = prefs.getBoolean(PreferencesActivity.KEY_ALLOW_EARLY_QR_CODES, false);
        mAllowExpiredQrCodes = prefs.getBoolean(PreferencesActivity.KEY_ALLOW_EXPIRED_QR_CODES, false);
    }

    public boolean isAllowEarlyQrCodes() {
        return mAllowEarlyQrCodes;
    }

    public boolean isAllowExpiredQrCodes() {
        return mAllowExpiredQrCodes;
    }

    private boolean mAllowEarlyQrCodes;
    private boolean mAllowExpiredQrCodes;
}
