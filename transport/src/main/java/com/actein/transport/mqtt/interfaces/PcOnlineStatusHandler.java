package com.actein.transport.mqtt.interfaces;

import com.actein.transport.mqtt.OnlineStatusProtos;

public interface PcOnlineStatusHandler
{
    void onPcOnlineStatusChanged(OnlineStatusProtos.OnlineStatus status);
}
