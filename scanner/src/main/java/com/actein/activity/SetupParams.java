package com.actein.activity;

public class SetupParams
{
    public SetupParams(String serverUri, String philipsHueUri, String password)
    {
        mBrokerUri = serverUri;
        mPhilipsHueUri = philipsHueUri;
        mPassword = password;
    }

    String getBrokerUri()
    {
        return mBrokerUri;
    }

    String getPhilipsHueUri()
    {
        return mPhilipsHueUri;
    }

    String getPassword()
    {
        return mPassword;
    }

    private String mBrokerUri;
    private String mPhilipsHueUri;
    private String mPassword;
}
