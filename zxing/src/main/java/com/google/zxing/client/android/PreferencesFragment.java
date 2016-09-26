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
import android.preference.PreferenceFragment;
import android.preference.PreferenceScreen;
import android.text.InputType;
import android.util.Log;
import android.widget.EditText;

import com.actein.zxing.data.Preferences;
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

        PreferenceScreen screenPrefs = getPreferenceScreen();

        /*if (!User.isAdmin(PreferencesFragment.this.getActivity()))
        {
            Preference qrCat = screenPrefs.findPreference(PreferencesActivity.KEY_CAT_QR_SETTINGS);
            screenPrefs.removePreference(qrCat);
            Preference userSettingsCat = screenPrefs.findPreference(
                    PreferencesActivity.KEY_CAT_USER_SETTINGS);
            screenPrefs.removePreference(userSettingsCat);
            Preference actionsCat = screenPrefs.findPreference(PreferencesActivity.KEY_CAT_ACTIONS);
            screenPrefs.removePreference(actionsCat);
            Preference deviceBugCat = screenPrefs.findPreference(
                    PreferencesActivity.KEY_CAT_DEVICE_BUG_WORKAROUNDS);
            screenPrefs.removePreference(deviceBugCat);
        }*/

        Preference changeUserPref = screenPrefs.findPreference(PreferencesActivity.KEY_CHANGE_USER);
        changeUserPref.setOnPreferenceClickListener(new UserChangeListener());
    }

    @Override
    public void onStart()
    {
        super.onStart();

        PreferenceScreen screenPrefs = getPreferenceScreen();
        Preference changeUserPref = screenPrefs.findPreference(PreferencesActivity.KEY_CHANGE_USER);

        if (User.isAdmin(PreferencesFragment.this.getActivity()))
        {
            changeUserPref.setTitle(R.string.preferences_change_user_user);
        }
        else
        {
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
                builder.setView(input);

                builder.setPositiveButton(R.string.button_ok, new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        try
                        {
                            if (User.isAdminPasswordCorrect(mActivity, input.getText().toString()))
                            {
                                User.changeUser(mActivity);
                                runCaptureActivity();
                            }
                            else
                            {
                                new AlertDialog.Builder(mActivity)
                                        .setTitle(R.string.msg_error)
                                        .setMessage(R.string.preferences_password_incorrect_msg)
                                        .show();
                            }
                        }
                        catch (Exception ex)
                        {
                            Log.e(TAG, ex.getMessage(), ex);
                        }
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

    private final static String TAG = PreferencesFragment.class.getSimpleName();
}
