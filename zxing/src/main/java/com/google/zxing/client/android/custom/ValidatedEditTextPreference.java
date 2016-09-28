package com.google.zxing.client.android.custom;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.widget.Button;

public class ValidatedEditTextPreference extends EditTextPreference
{
    public ValidatedEditTextPreference(Context ctx, AttributeSet attrs, int defStyle)
    {
        super(ctx, attrs, defStyle);
    }

    public ValidatedEditTextPreference(Context ctx, AttributeSet attrs)
    {
        super(ctx, attrs);
    }

    public interface OnDialogClosedListener
    {
        void onDialogClosed(boolean positiveResult);
    }

    public void setOnDialogClosedListener(OnDialogClosedListener listener)
    {
        mListener = listener;
    }

    /**
     * Return true in order to enable positive button or false to disable it.
     */
    protected boolean onCheckValue(String value)
    {
        return value != null && !value.isEmpty();
    }

    protected void onEditTextChanged()
    {
        boolean enable = onCheckValue(getEditText().getText().toString());
        Dialog dlg = getDialog();
        if (dlg instanceof AlertDialog)
        {
            AlertDialog alertDlg = (AlertDialog)dlg;
            Button btn = alertDlg.getButton(AlertDialog.BUTTON_POSITIVE);
            btn.setEnabled(enable);
        }
    }

    @Override
    protected void onDialogClosed(boolean positiveResult)
    {
        super.onDialogClosed(positiveResult);
        if (mListener != null)
        {
            mListener.onDialogClosed(positiveResult);
        }
    }

    @Override
    protected void showDialog(Bundle state)
    {
        super.showDialog(state);

        getEditText().removeTextChangedListener(mWatcher);
        getEditText().addTextChangedListener(mWatcher);
        onEditTextChanged();
    }

    private class EditTextWatcher implements TextWatcher
    {
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count){}

        @Override
        public void beforeTextChanged(CharSequence s, int start, int before, int count){}

        @Override
        public void afterTextChanged(Editable s)
        {
            onEditTextChanged();
        }
    }
    private EditTextWatcher mWatcher = new EditTextWatcher();
    private OnDialogClosedListener mListener = null;
}
