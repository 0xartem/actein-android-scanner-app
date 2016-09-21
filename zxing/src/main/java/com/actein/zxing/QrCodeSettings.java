package com.actein.zxing;

public class QrCodeSettings {

    public QrCodeSettings(boolean allowStartBeforeAppointment, boolean allowStartAfterExpiration) {
        mAllowStartBeforeAppointment = allowStartBeforeAppointment;
        mAllowStartAfterExpiration = allowStartAfterExpiration;
    }

    public boolean isAllowStartBeforeAppointment() {
        return mAllowStartBeforeAppointment;
    }

    public boolean isAllowStartAfterExpiration() {
        return mAllowStartAfterExpiration;
    }

    private boolean mAllowStartBeforeAppointment;
    private boolean mAllowStartAfterExpiration;
}
