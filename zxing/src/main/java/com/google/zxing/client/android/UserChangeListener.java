package com.google.zxing.client.android;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.preference.Preference;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.actein.zxing.model.User;
import com.google.zxing.integration.android.IntentIntegrator;

class UserChangeListener implements Preference.OnPreferenceClickListener
{
    UserChangeListener(Activity activity)
    {
        mActivity = activity;
    }

    @Override
    public boolean onPreferenceClick(Preference preference)
    {
        try
        {
            if (User.isAdmin(mActivity))
            {
                User.changeUser(mActivity);
                mActivity.finish();
            }
            else
            {
                AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
                builder.setTitle(R.string.preferences_enter_admin_pwd_msg_title);

                final EditText inputPwd = new EditText(mActivity);
                inputPwd.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);

                builder.setView(inputPwd);

                builder.setPositiveButton(R.string.button_ok, new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        onPasswordEntered(inputPwd.getText().toString());
                    }
                });
                builder.setNegativeButton(R.string.button_cancel, new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        dialog.cancel();
                    }
                });

                final AlertDialog dialog = builder.create();

                inputPwd.addTextChangedListener(new TextWatcher()
                {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after)
                    {
                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count)
                    {
                    }

                    @Override
                    public void afterTextChanged(Editable s)
                    {
                        Button okBtn = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                        okBtn.setEnabled(!inputPwd.getText().toString().isEmpty());
                    }
                });

                inputPwd.setOnEditorActionListener(new TextView.OnEditorActionListener()
                {
                    @Override
                    public boolean onEditorAction(TextView v, int actionId, KeyEvent event)
                    {
                        if (actionId == EditorInfo.IME_ACTION_DONE ||
                            ((event.getKeyCode() == KeyEvent.KEYCODE_ENTER) &&
                             (event.getAction() == KeyEvent.ACTION_DOWN)))
                        {
                            dialog.getButton(AlertDialog.BUTTON_POSITIVE).callOnClick();
                            return true;
                        }
                        return false;
                    }
                });

                dialog.show();
                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
            }
            return true;
        }
        catch (Exception ex)
        {
            Log.e(TAG, ex.getMessage(), ex);
        }
        return false;
    }

    private void onPasswordEntered(String password)
    {
        try
        {
            if (User.isAdminPasswordCorrect(mActivity, password))
            {
                User.changeUser(mActivity);
                mActivity.finish();
            }
            else
            {
                new AlertDialog.Builder(mActivity)
                        .setTitle(R.string.msg_error)
                        .setMessage(R.string.preferences_password_incorrect_msg)
                        .setNeutralButton(R.string.button_ok, null)
                        .show();
            }
        }
        catch (Exception ex)
        {
            Log.e(TAG, ex.getMessage(), ex);
        }
    }

    private Activity mActivity;
    private final static String TAG = UserChangeListener.class.getSimpleName();
}
