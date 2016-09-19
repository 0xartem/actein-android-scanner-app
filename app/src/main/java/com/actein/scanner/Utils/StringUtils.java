package com.actein.scanner.utils;

import android.util.Patterns;

public class StringUtils
{
    public static boolean isNetworkAddressValid(String address)
    {
        if (address.length() > 0
                && (Patterns.IP_ADDRESS.matcher(address).matches()
                || Patterns.DOMAIN_NAME.matcher(address).matches()))
        {
            return true;
        }
        return false;
    }
}
