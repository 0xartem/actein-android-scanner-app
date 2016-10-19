package com.actein.mvp;

public interface Presenter
{
    void onCreate(boolean isChangingConfiguration);
    void onDestroy(boolean isChangingConfiguration);
}
