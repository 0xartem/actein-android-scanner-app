package com.actein.android.utils;

import android.util.Patterns;

public class StringUtils
{
    public static boolean isNetworkAddressValid(String address)
    {
        return (!address.isEmpty() && (Patterns.IP_ADDRESS.matcher(address).matches() ||
                                       Patterns.DOMAIN_NAME.matcher(address).matches()));
    }
}
