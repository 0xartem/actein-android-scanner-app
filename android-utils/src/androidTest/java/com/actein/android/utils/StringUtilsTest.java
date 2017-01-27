package com.actein.android.utils;

import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
public class StringUtilsTest
{
    @Test
    public void isNetworkAddressValid_EmptyString() throws Exception
    {
        assertFalse(StringUtils.isNetworkAddressValid(""));
    }

    @Test
    public void isNetworkAddressValid_Ipv4Valid() throws Exception
    {
        assertTrue(StringUtils.isNetworkAddressValid("154.12.84.12"));
    }

    @Test
    public void isNetworkAddressValid_Ipv4Invalid() throws Exception
    {
        assertFalse(StringUtils.isNetworkAddressValid("154.12.84.300"));
    }

    @Test
    public void isNetworkAddressValid_Domain() throws Exception
    {
        assertTrue(StringUtils.isNetworkAddressValid("actein.com"));
    }

    public void isNetworkAddressValid_OrdinaryString() throws Exception
    {
        assertFalse(StringUtils.isNetworkAddressValid("Hello address!"));
    }
}