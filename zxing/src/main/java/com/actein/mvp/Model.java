package com.actein.mvp;

public interface Model
{
    void onCreate(boolean isChangingConfiguration);
    void onDestroy(boolean isChangingConfiguration);
}
