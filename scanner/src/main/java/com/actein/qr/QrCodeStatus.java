package com.actein.qr;

public enum QrCodeStatus
{
    QR_CODE_INVALID,
    QR_CODE_NOT_STARTED_YET,
    QR_CODE_EXPIRED,
    WRONG_LOCATION,
    DIGITAL_SIGNATURE_INVALID,
    SUCCESS;

    public static boolean isSuccess(QrCodeStatus status)
    {
        return status == SUCCESS;
    }
}
