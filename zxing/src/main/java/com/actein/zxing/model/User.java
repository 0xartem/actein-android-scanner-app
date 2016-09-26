package com.actein.zxing.model;

import android.content.Context;
import android.preference.Preference;

import com.actein.zxing.data.Preferences;
import com.actein.zxing.security.HashAlgorithm;
import com.actein.zxing.security.HashAlgorithmException;

public class User
{
    public static boolean isAdmin(Context context)
    {
        return Preferences.isAdminUser(context);
    }

    public static boolean isAdminPasswordCorrect(Context context, String password)
            throws HashAlgorithmException
    {
        if (password != null)
        {
            HashAlgorithm hashAlgorithm = new HashAlgorithm();
            String savedPwdHashBase64 = Preferences.getAdminPwdHash(context);
            // use trim() as workaround for android Issue 159799
            String pwdHashBase64 = hashAlgorithm.hashStrToBase64(password).trim();
            return savedPwdHashBase64.equals(pwdHashBase64);
        }
        return false;
    }

    public static void changeUser(Context context)
    {
        Preferences.setIsAdminUser(context, !Preferences.isAdminUser(context));
    }
}
