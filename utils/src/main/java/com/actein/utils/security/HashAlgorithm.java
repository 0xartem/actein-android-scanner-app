package com.actein.utils.security;

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
            throw new HashAlgorithmException(ex.toString(), ex);
        }
    }

    public byte[] hashData(byte[] data)
    {
        return msgDigest.digest(data);
    }

    private MessageDigest msgDigest;
}
