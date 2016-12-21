package com.actein.zxing.view;

import com.actein.mvp.ActivityView;

public interface CaptureView extends ActivityView
{
    void restartPreviewAfterDelay(long delayMS);

    void onCountDownStart();
    void onCountDownTick(String timeLeft);
    void onCountDownFinish();

    void onGameRunning();
    void onGameStopped();
    void onGameLoading();

    void changePcOnlineStatus(boolean online);
}
