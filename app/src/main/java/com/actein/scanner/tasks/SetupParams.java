package com.actein.scanner.tasks;

public class SetupParams
{
    public SetupParams(String serverUri, String philipsHueUri, String password)
    {
        mServerUri = serverUri;
        mPhilipsHueUri = philipsHueUri;
        mPassword = password;
    }

    public String getServerUri()
    {
        return mServerUri;
    }

    public String getPhilipsHueUri()
    {
        return mPhilipsHueUri;
    }

    public String getPassword()
    {
        return mPassword;
    }

    private String mServerUri;
    private String mPhilipsHueUri;
    private String mPassword;
}
