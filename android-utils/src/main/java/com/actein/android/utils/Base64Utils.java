package com.actein.android.utils;

import java.nio.charset.Charset;

import android.util.Base64;

import com.actein.utils.security.HashAlgorithm;

public class Base64Utils
{
    public static String hashStringToBase64(String string, HashAlgorithm hashAlgorithm)
    {
        byte[] rawHash = hashAlgorithm.hashData(string.getBytes(Charset.forName("UTF-8")));
        byte[] strHashBase64 = Base64.encode(rawHash, Base64.DEFAULT);
        return new String(strHashBase64, Charset.forName("US-ASCII"));
    }
}
