package com.actein.activity;

public class SetupParams
{
    public SetupParams(String serverUri, String philipsHueUri, String password, int boothId)
    {
        mBrokerUri = serverUri;
        mPhilipsHueUri = philipsHueUri;
        mPassword = password;
        mBoothId = boothId;
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

    int getBoothId()
    {
        return mBoothId;
    }

    private String mBrokerUri;
    private String mPhilipsHueUri;
    private String mPassword;
    private int mBoothId;
}
