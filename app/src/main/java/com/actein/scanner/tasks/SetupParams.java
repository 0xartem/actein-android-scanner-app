package com.actein.scanner.tasks;

public class SetupParams
{
    public SetupParams(String serverUri, String philipsHueUri, String password, int boothId)
    {
        mServerUri = serverUri;
        mPhilipsHueUri = philipsHueUri;
        mPassword = password;
        mBoothId = boothId;
    }

    String getServerUri()
    {
        return mServerUri;
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

    private String mServerUri;
    private String mPhilipsHueUri;
    private String mPassword;
    private int mBoothId;
}
