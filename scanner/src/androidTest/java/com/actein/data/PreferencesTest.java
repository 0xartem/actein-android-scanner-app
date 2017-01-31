package com.actein.data;

import android.content.Context;
import android.preference.PreferenceManager;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
public class PreferencesTest
{
    private Context mContext;

    @Before
    public void setUp() throws Exception
    {
        mContext = InstrumentationRegistry.getContext();
    }

    @Test
    public void appPreviouslyStarted_False() throws Exception
    {
        assertFalse(Preferences.appPreviouslyStarted(mContext));
    }

    @Test
    public void appPreviouslyStarted_True() throws Exception
    {
        Preferences.setBrokerAddr(mContext, "23.54.12.56");
        assertTrue(Preferences.appPreviouslyStarted(mContext));
    }

    @Test
    public void BrokerAddrTest() throws Exception
    {
        Preferences.setBrokerAddr(mContext, "65.23.64.23");
        assertTrue(Preferences.containsBrokerAddr(mContext));
        assertEquals(Preferences.getBrokerAddr(mContext), "65.23.64.23");
    }

    @Test
    public void PhilipsHueUriTest() throws Exception
    {
        Preferences.setPhilipsHueUri(mContext, "35.83.48.78");
        String resUri = PreferenceManager.getDefaultSharedPreferences(mContext)
                                         .getString("philips_hue_uri", "");
        assertEquals("35.83.48.78", resUri);
    }

    @Test
    public void IsAdminUserTest() throws Exception
    {
        Preferences.setIsAdminUser(mContext, true);
        assertTrue(Preferences.isAdminUser(mContext));
    }

    @Test
    public void AdminPwdHashTest() throws Exception
    {
        Preferences.setAdminPwdHash(mContext, "46c253965501650db8bffddce4bd4f078c82dfb7442de8181a2383ff9d0c495a");
        assertEquals("46c253965501650db8bffddce4bd4f078c82dfb7442de8181a2383ff9d0c495a",
                     Preferences.getAdminPwdHash(mContext));
    }

    @Test
    public void BoothIdTest() throws Exception
    {
        Preferences.setBoothId(mContext, 64);
        assertEquals(64, Preferences.getBoothId(mContext));
    }

}