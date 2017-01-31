package com.actein.mvp.view;

import android.content.Context;

public interface ContextOwner
{
    Context getActivityContext();
    Context getApplicationContext();
}
