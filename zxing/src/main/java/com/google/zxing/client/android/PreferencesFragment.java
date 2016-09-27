/*
 * Copyright (C) 2013 ZXing authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.zxing.client.android;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceCategory;
import android.preference.PreferenceFragment;
import android.preference.PreferenceScreen;
import android.text.InputType;
import android.util.Log;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.actein.zxing.model.User;
import com.google.zxing.integration.android.IntentIntegrator;

/**
 * Implements support for barcode scanning preferences.
 *
 * @see PreferencesActivity
 */
public final class PreferencesFragment extends PreferenceFragment
{

    @Override
    public void onCreate(Bundle icicle)
    {
        super.onCreate(icicle);

        addPreferencesFromResource(R.xml.preferences);

        mQrCat = (PreferenceCategory) findPreference(PreferencesActivity.KEY_CAT_QR_SETTINGS);
        mUserSettingsCat = (PreferenceCategory) findPreference(PreferencesActivity.KEY_CAT_USER_SETTINGS);
        mAdminPwdPref = findPreference(PreferencesActivity.KEY_CHANGE_ADMIN_PWD);
        mActionsCat = (PreferenceCategory) findPreference(PreferencesActivity.KEY_CAT_ACTIONS);
        mDeviceBugCat = (PreferenceCategory) findPreference(PreferencesActivity.KEY_CAT_DEVICE_BUG_WORKAROUNDS);

        Preference changeUserPref = findPreference(PreferencesActivity.KEY_CHANGE_USER);
        changeUserPref.setOnPreferenceClickListener(new UserChangeListener());
    }

    @Override
    public void onStart()
    {
        super.onStart();

        PreferenceScreen screenPrefs = getPreferenceScreen();
        Preference changeUserPref = findPreference(PreferencesActivity.KEY_CHANGE_USER);

        if (User.isAdmin(PreferencesFragment.this.getActivity()))
        {
            screenPrefs.addPreference(mQrCat);
            mUserSettingsCat.addPreference(mAdminPwdPref);
            screenPrefs.addPreference(mActionsCat);
            screenPrefs.addPreference(mDeviceBugCat);

            changeUserPref.setTitle(R.string.preferences_change_user_user);
        }
        else
        {
            screenPrefs.removePreference(mQrCat);
            mUserSettingsCat.removePreference(mAdminPwdPref);
            screenPrefs.removePreference(mActionsCat);
            screenPrefs.removePreference(mDeviceBugCat);

            changeUserPref.setTitle(R.string.preferences_change_user_admin);
        }
    }

    private class UserChangeListener implements Preference.OnPreferenceClickListener
    {
        UserChangeListener()
        {
            mActivity = PreferencesFragment.this.getActivity();
        }

        @Override
        public boolean onPreferenceClick(Preference preference)
        {
            if (User.isAdmin(mActivity))
            {
                User.changeUser(mActivity);
                runCaptureActivity();
            }
            else
            {
                AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
                builder.setTitle(R.string.preferences_enter_admin_pwd_msg_title);

                final EditText input = new EditText(PreferencesFragment.this.getActivity());
                input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                input.setOnEditorActionListener(new TextView.OnEditorActionListener()
                {
                    @Override
                    public boolean onEditorAction(TextView v, int actionId, KeyEvent event)
                    {
                        if (actionId == EditorInfo.IME_ACTION_DONE ||
                            ((event.getKeyCode() == KeyEvent.KEYCODE_ENTER) &&
                             (event.getAction() == KeyEvent.ACTION_DOWN)))
                        {
                            onPasswordEntered(v.getText().toString());
                            return true;
                        }
                        return false;
                    }
                });

                builder.setView(input);

                builder.setPositiveButton(R.string.button_ok, new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        onPasswordEntered(input.getText().toString());
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
                builder.show();
            }

            return true;
        }

        private void onPasswordEntered(String password)
        {
            try
            {
                if (User.isAdminPasswordCorrect(mActivity, password))
                {
                    User.changeUser(mActivity);
                    runCaptureActivity();
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

        private void runCaptureActivity()
        {
            IntentIntegrator intentIntegrator = new IntentIntegrator(mActivity);
            intentIntegrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
            intentIntegrator.setPrompt(getString(R.string.msg_default_status));
            Intent scanIntent = intentIntegrator.createScanIntent();
            startActivity(scanIntent);
        }

        private Activity mActivity;
    }

    PreferenceCategory mQrCat = null;
    PreferenceCategory mUserSettingsCat = null;
    Preference mAdminPwdPref = null;
    PreferenceCategory mActionsCat = null;
    PreferenceCategory mDeviceBugCat = null;

    private final static String TAG = PreferencesFragment.class.getSimpleName();
}
