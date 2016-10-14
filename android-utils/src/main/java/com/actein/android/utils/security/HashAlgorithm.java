package com.actein.android.utils.security;

import android.util.Base64;

import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class HashAlgorithm
{
    public HashAlgorithm() throws HashAlgorithmException
    {
        try
        {
            msgDigest = MessageDigest.getInstance("SHA-256");
        }
        catch (NoSuchAlgorithmException ex)
        {
            throw new HashAlgorithmException(ex.getMessage(), ex);
        }
    }

    public byte[] hashData(byte[] data)
    {
        return msgDigest.digest(data);
    }

    public String hashStrToBase64(String str)
    {
        byte[] strHash = hashData(str.getBytes(Charset.forName("UTF-8")));
        byte[] strHashBase64 = Base64.encode(strHash, Base64.DEFAULT);
        return new String(strHashBase64, Charset.forName("US-ASCII"));
    }

    private MessageDigest msgDigest;
}
