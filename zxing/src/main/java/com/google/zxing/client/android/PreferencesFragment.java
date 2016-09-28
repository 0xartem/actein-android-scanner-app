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

import android.app.AlertDialog;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceCategory;
import android.preference.PreferenceFragment;
import android.preference.PreferenceScreen;
import android.util.Log;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import com.actein.zxing.model.User;
import com.google.zxing.client.android.custom.ValidatedEditTextPreference;

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
        mAdminPwdPref = (ValidatedEditTextPreference) findPreference(PreferencesActivity.KEY_CHANGE_ADMIN_PWD);
        mActionsCat = (PreferenceCategory) findPreference(PreferencesActivity.KEY_CAT_ACTIONS);
        mDeviceBugCat = (PreferenceCategory) findPreference(PreferencesActivity.KEY_CAT_DEVICE_BUG_WORKAROUNDS);

        Preference changeUserPref = findPreference(PreferencesActivity.KEY_CHANGE_USER);
        changeUserPref.setOnPreferenceClickListener(new UserChangeListener(getActivity()));

        mAdminPwdPref.setOnPreferenceChangeListener(mAdminPwdListener);
        mAdminPwdPref.getEditText().setOnEditorActionListener(mAdminPwdListener);
        mAdminPwdPref.setOnDialogClosedListener(mAdminPwdListener);
    }

    @Override
    public void onStart()
    {
        super.onStart();

        PreferenceScreen screenPrefs = getPreferenceScreen();
        Preference changeUserPref = findPreference(PreferencesActivity.KEY_CHANGE_USER);

        if (User.isAdmin(getActivity()))
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

    private class AdminPwdChangeListener implements
            Preference.OnPreferenceChangeListener,
            TextView.OnEditorActionListener,
            ValidatedEditTextPreference.OnDialogClosedListener
    {
        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event)
        {
            if (actionId == EditorInfo.IME_ACTION_DONE ||
                ((event.getKeyCode() == KeyEvent.KEYCODE_ENTER) &&
                 (event.getAction() == KeyEvent.ACTION_DOWN)))
            {
                return ((AlertDialog) mAdminPwdPref.getDialog())
                        .getButton(AlertDialog.BUTTON_POSITIVE)
                        .callOnClick();
            }
            return false;
        }

        @Override
        public boolean onPreferenceChange(Preference preference, Object newValue)
        {
            try
            {
                if (!User.changeAdminPassword(getActivity(), (String) newValue))
                {
                    new AlertDialog.Builder(getActivity())
                            .setTitle(R.string.msg_error)
                            .setMessage(R.string.preferences_same_password_msg)
                            .setNeutralButton(R.string.button_ok, null)
                            .show();
                }
                return true;
            }
            catch (Exception ex)
            {
                Log.e(TAG, ex.getMessage(), ex);
            }
            return false;
        }

        @Override
        public void onDialogClosed(boolean positiveResult)
        {
            mAdminPwdPref.setText("");
        }
    }

    private PreferenceCategory mQrCat = null;
    private PreferenceCategory mUserSettingsCat = null;
    private ValidatedEditTextPreference mAdminPwdPref = null;
    private PreferenceCategory mActionsCat = null;
    private PreferenceCategory mDeviceBugCat = null;
    private AdminPwdChangeListener mAdminPwdListener = new AdminPwdChangeListener();

    private final static String TAG = PreferencesFragment.class.getSimpleName();
}
