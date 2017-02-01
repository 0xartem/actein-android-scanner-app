package com.actein.activity;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.filters.LargeTest;
import android.support.test.runner.AndroidJUnit4;

import com.actein.android.utils.Base64Utils;
import com.actein.data.Preferences;
import com.actein.scanner.R;
import com.actein.utils.security.HashAlgorithm;
import com.google.zxing.client.android.CaptureActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.action.ViewActions.clearText;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.*;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.assertion.ViewAssertions.matches;

import static org.hamcrest.core.AllOf.allOf;

import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class SetupActivityTest
{
    @Rule
    public IntentsTestRule<SetupActivity> mActivityRule = new IntentsTestRule<>(SetupActivity.class);

    @Test
    public void SetupDataTest() throws Exception
    {
        onView(withId(R.id.brokerAddressEditText)).perform(clearText())
                                                  .perform(typeText("87.12.85.73"))
                                                  .check(matches(isDisplayed()));

        onView(withId(R.id.philipsHueBridgeAddressEditText)).perform(typeText("82.73.15.85"))
                                                            .check(matches(isDisplayed()));

        onView(withId(R.id.adminPasswordEditText)).perform(typeText("123456!"))
                                                  .check(matches(isDisplayed()));

        onView(withId(R.id.okButton)).perform(click());

        intended(allOf(hasComponent(CaptureActivity.class.getName()),
                       hasExtra("SCAN_FORMATS", "QR_CODE")));

        assertEquals("87.12.85.73", Preferences.getBrokerAddr(mActivityRule.getActivity()));
        String pwdHash = Base64Utils.hashStringToBase64("123456!", new HashAlgorithm());
        assertEquals(pwdHash, Preferences.getAdminPwdHash(mActivityRule.getActivity()));
        assertEquals(1, Preferences.getBoothId(mActivityRule.getActivity()));
    }
}