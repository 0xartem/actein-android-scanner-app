package com.actein.android.utils;

import android.support.test.runner.AndroidJUnit4;

import com.actein.utils.security.HashAlgorithm;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
public class Base64UtilsTest
{
    @Test
    public void hashStringToBase64_Password() throws Exception
    {
        String hash = Base64Utils.hashStringToBase64("TRhdE32425!%$", new HashAlgorithm());
        assertEquals(hash, "7knPVrbpY+IwFOoktkG1QTbs8M1cQ4Ute2+n+9j2Jcw=");
    }

    @Test
    public void hashStringToBase64_Empty() throws Exception
    {
        String hash = Base64Utils.hashStringToBase64("", new HashAlgorithm());
        assertEquals(hash, "47DEQpj8HBSa+/TImW+5JCeuQeRkm5NMpJWZG3hSuFU=");
    }

    @Test
    public void hashStringToBase64_NullHashAlgorithm() throws Exception
    {
        String hash = Base64Utils.hashStringToBase64("3213g3sdfsd@#!%", null);
        assertEquals(hash, "");
    }
}