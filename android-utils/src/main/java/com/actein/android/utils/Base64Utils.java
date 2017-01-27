package com.actein.android.utils;

import java.nio.charset.Charset;

import android.util.Base64;

import com.actein.utils.security.HashAlgorithm;

public class Base64Utils
{
    public static String hashStringToBase64(String string, HashAlgorithm hashAlgorithm)
    {
        if (hashAlgorithm != null)
        {
            byte[] rawHash = hashAlgorithm.hashData(string.getBytes(Charset.forName("UTF-8")));
            return Base64.encodeToString(rawHash, Base64.NO_WRAP);
        }
        return "";
    }
}
