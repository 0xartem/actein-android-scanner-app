package com.actein.zxing.model;

import android.content.Context;

import com.actein.zxing.data.Preferences;
import com.actein.android.utils.security.HashAlgorithm;
import com.actein.android.utils.security.HashAlgorithmException;

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
            String savedPwdHashBase64 = Preferences.getAdminPwdHash(context);
            HashAlgorithm hashAlgorithm = new HashAlgorithm();
            // use trim() as workaround for android Issue 159799
            String pwdHashBase64 = hashAlgorithm.hashStrToBase64(password).trim();
            return savedPwdHashBase64.equals(pwdHashBase64);
        }
        return false;
    }

    public static boolean changeAdminPassword(Context context, String password)
            throws HashAlgorithmException
    {
        if (password != null)
        {
            String savedPwdHashBase64 = Preferences.getAdminPwdHash(context);
            HashAlgorithm hashAlgorithm = new HashAlgorithm();
            // use trim() as workaround for android Issue 159799
            String pwdHashBase64 = hashAlgorithm.hashStrToBase64(password).trim();
            if (!savedPwdHashBase64.equals(pwdHashBase64))
            {
                Preferences.setAdminPwdHash(context, pwdHashBase64);
                return true;
            }
        }
        return false;
    }

    public static void changeUser(Context context)
    {
        Preferences.setIsAdminUser(context, !Preferences.isAdminUser(context));
    }
}
