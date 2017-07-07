package com.actein.mvp.view;

import android.content.Context;
import android.view.LayoutInflater;

public interface ContextOwner
{
    Context getActivityContext();
    Context getApplicationContext();
    LayoutInflater getLayoutInflater();
}
