package com.actein.mvp.model;

public interface CommonObserver
{
    void onError(String message);
    void onInfo(String message);
}
