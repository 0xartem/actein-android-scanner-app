package com.actein.mvp;

import android.content.DialogInterface;

public interface ActivityView extends ContextOwner
{
    void showToast(String message);
    void showToast(String message, int duration);
    void showErrorDialog(String message);
    void showInfoDialog(String message);
    void showInfoDialog(String message,
                        DialogInterface.OnClickListener okListener,
                        DialogInterface.OnCancelListener cancelListener);
}
