package com.actein.mvp;

public interface ActivityView extends ContextOwner
{
    void showToast(String message);
    void showToast(String message, int duration);
    void showErrorDialog(String message);
}
