package com.actein.utils;

public class Geo
{
    public Geo(double latitude, double longitude)
    {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public double getLatitude()
    {
        return latitude;
    }

    public double getLongitude()
    {
        return longitude;
    }

    private final double latitude;
    private final double longitude;
}
