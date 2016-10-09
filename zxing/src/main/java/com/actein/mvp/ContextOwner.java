package com.actein.mvp;

import android.content.Context;

public interface ContextOwner
{
    Context getActivityContext();
    Context getApplicationContext();
}
