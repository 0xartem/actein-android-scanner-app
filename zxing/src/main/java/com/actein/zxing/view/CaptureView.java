package com.actein.zxing.view;

import com.actein.mvp.ActivityView;

public interface CaptureView extends ActivityView
{
    void onGameRunning();
    void onGameStopped();
    void onGameLoading();
}
