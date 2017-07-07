package com.actein.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.widget.Toast;

import com.actein.mvp.view.ActivityView;
import com.actein.scanner.R;

public class BaseActivity extends Activity implements ActivityView
{
    // ActivityView implementation
    @Override
    public void showToast(String message)
    {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showToast(String message, int duration)
    {
        Toast.makeText(getApplicationContext(), message, duration).show();
    }

    @Override
    public void showErrorDialog(String message)
    {
        new AlertDialog.Builder(this).setTitle(getString(R.string.msg_error))
                                     .setMessage(message)
                                     .setPositiveButton(R.string.button_ok, null)
                                     .show();
    }

    @Override
    public void showInfoDialog(String message)
    {
        this.showInfoDialog(message, null, null);
    }

    @Override
    public void showInfoDialog(String message,
                               DialogInterface.OnClickListener okListener,
                               DialogInterface.OnCancelListener cancelListener)
    {
        new AlertDialog.Builder(this).setTitle(getString(R.string.msg_info))
                                     .setMessage(message)
                                     .setPositiveButton(R.string.button_ok, okListener)
                                     .setOnCancelListener(cancelListener)
                                     .show();
    }

    // ContextOwner implementation
    @Override
    public Context getActivityContext()
    {
        return this;
    }

    @Override
    public Context getApplicationContext()
    {
        return super.getApplicationContext();
    }
}
